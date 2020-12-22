<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;

use Illuminate\Support\Facades\Response;

use App\Target;
use App\Quest;
use App\Quest_user;
use App\User;
use Auth;
use Illuminate\Http\Request;
use Validator;

class Questcontroller extends Controller
{
  public function __construct()
  {
    $this->middleware('auth');
  }
  

  public function getAllQuests()
  {
    $quests = Quest::get()->toArray();
    usort($quests, function ($a, $b) {
      return strcmp($a['Name'], $b['Name']);
    });
    return response()->json($quests, 200);
  }


  public function createQuest(Request $request)
  {
    $validator = Validator::make($request->toArray(), Quest::$rules, Quest::$messages);
    if ($validator->passes()) {
      $quest = new Quest;
      $quest->Name =  $request->Name;
      $quest->point_id  = $request->point_id;
      $quest->save();

      $this->addQuestToAllUsers($quest);
      return response()->json([
        "message" => "Quest created"
      ], 201);
    } else {
      return response()->json(['error' => $validator->errors()], 400);
    }
  }

  public function addQuestToAllUsers($quest){
    $user = User::get()->toArray();
    $quests = Quest::get()->toArray();
    foreach($user as $entry){
        $quest_user = new Quest_user();
        $quest_user->user_id = $entry['id'];
        $quest_user->quest_id = $quest['id'];
        $quest_user['progress'] = 0;
        $quest_user->save();
      }
  }

  public function getQuest($id){
    if (Quest::where('id', $id)->exists()) {
      $quest = Quest::where('id', $id)->get()->toArray();
      return response()->json($quest, 200);
    } else {
      return response()->json([
        "message" => "Quest not found"
      ], 404);
    }
  }

  public function updateQuest(Request $request, $id){
    if (Quest::where('id', $id)->exists()) {
      $quest = Quest::find($id);
      $quest->Name = is_null($request->Name) ? $quest->Name : $request->Name;
      $quest->point_id = is_null($request->point_id) ? $quest->point_id : $request->point_id;
      $quest->save();

      return response()->json([
        "message" => "Quest updated successfully"
      ], 200);
    } else {
      return response()->json([
        "message" => "Quest not found"
      ], 404);
    }
  }

  public function deleteQuest($id)
  {
    if (Quest::where('id', $id)->exists()) {
      $quest = Quest::find($id);
      //Delete all Questusers when a Quest is deleted 
      $quests_users = Quest_user::where('quest_id', $id)->get()->toArray();
      foreach($quests_users as $entry){
          $entry->delete();
      }
      $quest->delete();
      return response()->json([
        "message" => "Quest deleted"
      ], 202);
    } else {
      return response()->json([
        "message" => "Quest not found"
      ], 404);
    }
  }


  public function getAllTargetsToUser($id){
    $targets = Target::where('user_id', $id)->get()->toArray();
    $Quest_users = Quest_user::where('user_id', $id)->get()->toArray();
    $collection2 = collect();
    $collection = collect();
    $unique = null;
    foreach($Quest_users as $entry){
      $obj = Quest::findOrFail($entry['quest_id']); 
      $collection2->push($obj);
      $unique = $collection2->unique();
    }
    foreach($Quest_users as $entry){
      $entry['questobj'] = Quest::findOrFail($entry['quest_id']);
      $collection->push($entry);
    }
    return response()->json(["targets" => $Quest_users, "quests" => $collection], 200);
  }



  /**************/
  /*UI METHODS */
  /*************/


  public function index(){
    $quests = Quest::all();
    if (Auth::check()) {
      // The user is logged in
      return view('quests.index', compact('quests'));
    } else {
      Log::emergency(Auth::user());
      return redirect()->intended('/login')->getTargetUrl();
    }
  }

  public function create(){
    return view('quests.create');
  }

  public function store(Request $request){
    $this->validate(request(), [
      'Name' => 'required|min:1|max:50'
    ]);
    $quest = new Quest;
    $quest->Name =  $request->Name;
    $quest->point_id = $request->point_id;
    $quest->save();
    $this->addQuestToAllUsers($quest);
    return redirect('/quests')->with('success', 'Quest saved!');
  }

  public function edit($id){
    $quest = Quest::find($id);
    if($quest != null){
      return view('quests.edit', compact('quest'));
    }else{
      return response()->view('errors.' . '404', [], 404);
    }
  }


  public function update(Request $request, $id){
    $this->validate(request(), [
      'Name' => 'required|min:1|max:50'
    ]);
    if (Quest::where('id', $id)->exists()) {
      $quest = Quest::find($id);
      $quest->Name = is_null($request->Name) ? $quest->Name : $request->Name;
      $quest->point_id = is_null($request->point_id) ? $quest->point_id : $request->point_id;
      $quest->save();
      return redirect('/quests')->with('success', 'Quest updated!');
    }
  }

  public function destroy($id)
  {
    if (Quest::where('id', $id)->exists()) {
      $quest = Quest::find($id);
      $quest->delete();

      //Delete all Questusers when a Quest is deleted 
      $quests_users = Quest_user::where('quest_id', $id)->get()->toArray();
      foreach($quests_users as $entry){
        $entry->delete();
      }

      return redirect('/quests')->with('success', 'Quest deleted!');
    }
  }
}
