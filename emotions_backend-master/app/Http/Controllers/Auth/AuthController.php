<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Carbon\Carbon;
use App\User;
use App\Leaderboard;
use App\Quest;
use App\Quest_user;
use Redirect;
use Log;
use App\Notifications\SignupActivate;
use App\Notifications\SignupActivateBackend;
use Illuminate\Support\Facades\Validator;
use Illuminate\Foundation\Validation\ValidatesRequests;


class AuthController extends Controller
{

    //Singup via API
    public function signup(Request $request)
    {
        $request->validate([
            'name' => 'required|string',
            'email' => 'required|string|email|unique:users',
            'password' => 'required|string',
            'password_confirmation' => 'required|string'
        ]);
        $user = new User([
            'name' => $request->name,
            'email' => $request->email,
            'password' => bcrypt($request->password),
            'activation_token' => str_random(60)
        ]);
        $user->save();
        $quests = Quest::get()->toArray();
        if (count($quests) != 0) {
            foreach ($quests as $entry) {
                $quest_user = new Quest_user();
                $quest_user->user_id = $user['id'];
                $quest_user->quest_id = $entry['id'];
                $quest_user['progress'] = 0;
                $quest_user->save();
            }
        }
        $user->notify(new SignupActivate($user));
        return response()->json([
            'message' => 'Successfully created user!'
        ], 201);
    }


    //Singup via Webclient
    public function signupUI(Request $request)
    {
        $request->validate([
            'name' => 'required|string|min:1|max:25',
            'email' => 'required|string|email|unique:users',
            'password' => 'required|string',
            'password_confirmation' => 'required|string|required_with:password|same:password'
        ]);
        $user = new User([
            'name' => $request->name,
            'email' => $request->email,
            'password' => bcrypt($request->password),
            'activation_token' => str_random(60)
        ]);
        $user->save();
        $user->notify(new SignupActivateBackend($user));
        return redirect('/login');
    }



    public function login(Request $request)
    {
        $request->validate([
            'email' => 'required|string|email',
            'password' => 'required|string'
        ]);
        $credentials = request(['email', 'password']);
        $credentials['active'] = 1;

        if (!Auth::attempt($credentials))
            return response()->json([
                'message' => 'Unauthorized'
            ], 401);
        $user = $request->user();
        $tokenResult = $user->createToken('Personal Access Token');
        $token = $tokenResult->token;
        if ($request->remember_me)
            $token->expires_at = Carbon::now()->addWeeks(1);
        $token->save();
        return response()->json([
            'access_token' => $tokenResult->accessToken,
            'token_type' => 'Bearer',
            'expires_at' => Carbon::parse(
                $tokenResult->token->expires_at
            )->toDateTimeString()
        ]);
    }
    //Logout Backend
    public function logoutUser()
    {
        Auth::logout();
        return redirect('/login');
    }
    //Logout API
    public function logout(Request $request)
    {
        $request->user()->token()->revoke();
        return response()->json([
            'message' => 'Successfully logged out'
        ]);
    }

    // Get the authenticated User
    public function user($mail)
    {
        if (User::where('email', $mail)->exists()) {
            $user = User::where('email', $mail)->get()->first();
            return response()->json($user, 200);
        } else {
            return response()->json([
                "message" => "user not found"
            ], 404);
        }
    }


    public function signupActivate($token) {
        $user = User::where('activation_token', $token)->first();
        if (!$user) {
            return response()->json([
                'message' => 'This activation token is invalid.'
            ], 404);
        }
        $user->active = true;
        $user->activation_token = '';
        $user->save();
        return view('login.pw');
    }

    public function signupActivateBackend($token)
    {
        $user = User::where('activation_token', $token)->first();
        if (!$user) {
            return response()->json([
                'message' => 'This activation token is invalid.'
            ], 404);
        }
        $user->active = true;
        $user->activation_token = '';
        $user->save();
        return redirect('/login');
    }

    //Add additional Userdata 
    public function addAditionalUserData(Request $request, $mail)
    {
        if (User::where('email', $mail)->exists()) {
            $user = User::where('email', $mail)->get()->first();
            $user->age = $request->age;
            $user->education = $request->education;
            $user->sex = $request->sex;
            $user->country = $request->country;
            $user->points = 0;
            $user->save();
            return response()->json(
                $user,
                202
            );
        } else {
            return response()->json([
                "message" => "User not found"
            ], 404);
        }
    }
    public function updateUserData(Request $request, $mail)
    {
        if (User::where('email', $mail)->exists()) {
            $user = User::where('email', $mail)->get()->first();
            $user->name = $request->name;
            $user->age = $request->age;
            $user->education = $request->education;
            $user->sex = $request->sex;
            $user->country = $request->country;
            $user->save();

            return response()->json($user, 202);
        } else {
            return response()->json([
                "message" => "User not updated"
            ], 404);
        }
    }

    public function getAllUsers()
    {
        $user = User::get()->toArray();
        return response()->json($user, 200);
    }

    public function index()
    {
        return view('login.login');
    }

    public function create()
    {
        return view('login.create');
    }

    public function store(Request $request)
    {

        //Error messages
        $messages = [
            "email.required" => "Email is required",
            "email.email" => "Email is not valid",
            "email.exists" => "Email doesn't exists",
            "password.required" => "Password is required",
            "password.min" => "Password must be at least 6 characters"
        ];

        // validate the form data
        $validator = Validator::make($request->all(), [
            'email' => 'required|email|exists:users,email',
            'password' => 'required|min:6'
        ], $messages);

        $json = $this->login($request);
        $message = (string)json_encode($json, true);
        $unauth = json_decode($message, true);
        $fehler = "PW NICHT RICHTIG";
        $fehler2 = "Unauthorized";
        Log::emergency($unauth);
        if (strpos($message, 'message') !== false) {
            if (strcmp($unauth['original']['message'], $fehler2) == 0) {
                return Redirect::back()
                    ->withInput()
                    ->withErrors([
                        'password' => 'Incorrect password!'
                    ]);
                Log::emergency($fehler);
            }
        }

        if (strpos($message, 'access_token') !== false) {
            if ($unauth['original']['access_token'] != null) {
                //    Log::emergency($unauth['original']['access_token']);
                Log::emergency(Auth::user());
                //Set a cookie with the access token for the API to get the data for the points
                setcookie("access_token_bearer", $unauth['original']['access_token']);
                return Redirect::to('/points')->with('success', 'Logged in!');
            }
        }
    }
}
