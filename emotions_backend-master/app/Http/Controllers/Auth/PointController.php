<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use QrCode;
use Image;
use Storage;
use BaconQrCode\Renderer\ImageRenderer;
use BaconQrCode\Renderer\Image\ImagickImageBackEnd;
use BaconQrCode\Renderer\RendererStyle\RendererStyle;
use BaconQrCode\Writer;
use Carbon\Carbon;


use Illuminate\Support\Facades\Response;

use App\Point;
use App\User;
use App\Point_user;
use App\ImagesPoint;
use App\Images;
use App\Leaderboard;
use App\Quest_user;
use App\Target;
use App\Quest;
use Illuminate\Http\Request;
use Redirect;
use PDF;
use Validator;
use Auth;

class PointController extends Controller
{

  public function __construct()
  {
    $this->middleware('auth');
  }
  public function getAllPoints()
  {
    $points = Point::get()->toArray();
    usort($points, function ($a, $b) {
      return strcmp($a['name'], $b['name']);
    });
    return response()->json($points, 200);
  }

  public function createPoint(Request $request)
  {
    $validator = Validator::make($request->toArray(), Point::$rules, Point::$messages);
    if ($validator->passes()) {

      $point = new Point;
      $point->name = $request->name;
      $point->text =  $request->text;
      $point->QRnumber = $request->QRnumber;
      $point->longitude = $request->longitude;
      $point->latitude = $request->latitude;
      $point->save();

      $Pointimage = Image::make($request->photo);
      Response::make($Pointimage->encode('jpeg'));
      $form_data_pointImage = array(
        'assigned_Point'  => $point->id,
        'enctype' => "multipart/form-data",
        'Image' => $Pointimage
      );
      ImagesPoint::create($form_data_pointImage);


      $image_file = \QrCode::format('png')
        ->merge('/Applications/XAMPP/xamppfiles/htdocs/Bachelorarbeit/app/QR-Codes/logo.png', 0.2, true)
        ->size(400)->errorCorrection('H')
        ->generate($point->id);

      $image = Image::make($image_file);

      Response::make($image->encode('jpeg'));

      $form_data = array(
        'assigned_Point'  => $point->id,
        'enctype' => "multipart/form-data",
        'QR_Code_Image' => $image
      );

      Images::create($form_data);

      return response()->json([
        "message" => "Point created"
      ], 201);
    } else {
      return response()->json(['error' => $validator->errors()], 400);
    }
  }


  public function getPoint($id) {
    if (Point::where('id', $id)->exists()) {
      $point = Point::where('id', $id)->get()->first();
      return response()->json($point, 200);
    } else {
      return response()->json([
        "message" => "Point not found"
      ], 404);
    }
  }

  public function updatePoint(Request $request, $id)
  {
    if (Point::where('id', $id)->exists()) {
      $point = Point::find($id);
      $point->name = is_null($request->name) ? $point->name : $request->name;
      $point->text = is_null($request->text) ? $point->text : $request->text;
      $point->QRnumber = is_null($request->QRnumber) ? $point->QRnumber : $request->QRnumber;
      $point->longitude = is_null($request->longitude) ? $point->longitude : $request->longitude;
      $point->latitude = is_null($request->latitude) ? $point->latitude : $request->latitude;
      $point->save();

      return response()->json([
        "message" => "Point updated successfully"
      ], 200);
    } else {
      return response()->json([
        "message" => "Point not found"
      ], 404);
    }
  }

  public function deletePoint($id)
  {
    if (Point::where('id', $id)->exists()) {
      $point = Point::find($id);
      $point->delete();

      return response()->json([
        "message" => "Point deleted"
      ], 202);
    } else {
      return response()->json([
        "message" => "Point not found"
      ], 404);
    }
  }

  public function generateQRForPoint($id)
  {
    if (Point::where('id', $id)->exists()) {
      $point = Point::find($id);

      $image_file = \QrCode::format('png')
        ->merge('/Applications/XAMPP/xamppfiles/htdocs/Bachelorarbeit/app/QR-Codes/logo.png', 0.2, true)
        ->size(400)->errorCorrection('H')
        ->generate($point->id);

      $image = Image::make($image_file);

      Response::make($image->encode('jpeg'));

      $form_data = array(
        'assigned_Point'  => $point->id,
        'enctype' => "multipart/form-data",
        'QR_Code_Image' => $image
      );

      Images::create($form_data);

      return response()->json([
        "message" => "Point QR generated"
      ], 202);
    } else {
      return response()->json([
        "message" => "Point QR not generated"
      ], 404);
    }
  }


  function fetch_image($image_id)
  {
    if (Images::where('id', $image_id)->exists()) {
      $image = Images::findOrFail($image_id);
      $image_file = Image::make($image->QR_Code_Image);
      $response = Response::make($image_file->encode('png'));
      $response->header('Content-Type', 'image/png');
      return $response;
    } else {
      return response()->json([
        "message" => "Image not found"
      ], 404);
    }
  }

  function fetch_imageToPoint($id)
  {
    if (Images::where('assigned_point', $id)->exists()) {
      $image = Images::where('assigned_point', $id)->get()->first();
      $image_file = Image::make($image->QR_Code_Image);
      $response = Response::make($image_file->encode('png'));
      $response->header('Content-Type', 'image/png');
      return $response;
    } else {
      return response()->json([
        "message" => "Image not found"
      ], 404);
    }
  }

  function fetch_PointImageToPoint($id)
  {
    if (ImagesPoint::where('assigned_point', $id)->exists()) {
      $image = ImagesPoint::where('assigned_point', $id)->get()->first();
      $image_file = Image::make($image->Image);
      $response = Response::make($image_file->encode('png'));
      $response->header('Content-Type', 'image/png');
      return $response;
    } else {
      return response()->json([
        "message" => "Image for Point not found"
      ], 404);
    }
  }

  function addPointToUSer($userid, $pointid)
  {
    if (User::where('id', $userid)->exists() && Point::where('id', $pointid)->exists()) {
      $user = User::findOrFail($userid);
      $point = Point::findOrFail($pointid);

      $user_point = new Point_user;
      $user_point['user_id'] = $user->id;
      $user_point['point_id'] = $point->id;
      $user_point['found_at'] = Carbon::now();
      $user_point->save();

      return response()->json([
        "message" => "updated successfully", $user, $point, $user_point
      ], 200);
    } else {
      return response()->json([
        "message" => "Point or User not found"
      ], 404);
    }
  }


  function getTop7DaysLeaderboard(){
    $leaderboard = Point_user::whereDate('found_at', '>', Carbon::now()->subDays(7))->get()->toArray();
    $collection = collect([]);
    $collection_points = collect([]);
    foreach ($leaderboard as $entry) {
      $user = User::find($entry['user_id']);
      if ($collection->contains($user) == false) {
        $collection->push($user);
      }
    }
    foreach ($collection as $userentry) {
      $userentry->points_7 = 0;
      foreach ($leaderboard as $entry) {
        if ($entry['user_id'] == $userentry->id) {
          $point = Point::find($entry['point_id']);

          $userentry->points_7 = $userentry->points_7 + $point['QRnumber'];
          $userentry->save();
        }
      }
    }
    return response()->json($collection, 200);
  }

  function getTop1DayLeaderboard(){
    $leaderboard = Point_user::whereDate('found_at', '>', Carbon::now()->subDays(1))->get()->toArray();
    $collection = collect([]);
    $collection_points = collect([]);
    foreach ($leaderboard as $entry) {
      $user = User::find($entry['user_id']);
      if ($collection->contains($user) == false) {
        $collection->push($user);
      }
    }
    foreach ($collection as $userentry) {
      $userentry->points_1 = 0;
      foreach ($leaderboard as $entry) {
        if ($entry['user_id'] == $userentry->id) {
          $point = Point::find($entry['point_id']);
          $userentry->points_1 = $userentry->points_1 + $point['QRnumber'];
          $userentry->save();
        }
      }
    }
    return response()->json($collection, 200);
  }

  function getTopAllTimeLeaderboard(){
    $leaderboard = Point_user::get()->toArray();
    $collection = collect([]);
    $collection_points = collect([]);
    foreach ($leaderboard as $entry) {
      $user = User::find($entry['user_id']);
      if ($collection->contains($user) == false) {
        $collection->push($user);
      }
    }
    foreach ($collection as $userentry) {
      $userentry->points = 0;
      foreach ($leaderboard as $entry) {
        if ($entry['user_id'] == $userentry->id) {
          $point = Point::find($entry['point_id']);
          $userentry->points = $userentry->points + $point['QRnumber'];
          $userentry->save();
        }
      }
    }
    return response()->json($collection, 200);
  }

  public function updateLeaderborad($uid,$pid){
    if((User::where('id', $uid)->exists())  && (Point::where('id', $pid)->exists())){

      $point_user = new Point_user();
      $point_user->user_id = $uid;
      $point_user->point_id = $pid;
      $point_user->found_at = Carbon::now();
      $point_user->save();

      $quests = Quest_user::where('user_id', $uid)->get()->toArray();
      $collection3 = collect();
      
      foreach($quests as $entry){
        $obj = Quest_user::findOrFail($entry['id']);
        $entry['questobj'] = Quest::findOrFail($entry['quest_id']);
        $entry['questobj']['pointobj'] = Point::findOrFail($entry['questobj']['point_id']);
        if($entry['questobj']['point_id'] == $pid){
          $collection3->push($entry);
          switch ($obj['progress']) {
            case "0":
              $obj['progress'] = $obj['progress'] +2;
              break;
            case "1":
              $obj['progress'] = $obj['progress'] +1;
              break;
            case "2":
              break;
          }
         $obj->save();
        }
      }
      return response()->json(["message" => "Point added to User successfully", "point_user" => $point_user,  "Coll3" => $collection3 ], 200);  //"Quests" => $quests,
    }else{
      return response()->json([
        "message" => "Point not added to User"
      ], 404);
    }
  }



  /**************/
  /*UI METHODS */
  /*************/


  public function index(){
    $points = Point::all();
    if (Auth::check()) {
      // The user is logged in...
      return view('points.index', compact('points'));
    } else {
      Log::emergency(Auth::user());
      return redirect()->intended('/login')->getTargetUrl();
    }
  }

  public function create(){
    return view('points.create');
  }

  public function store(Request $request){
    $this->validate(request(), [
      'name' => 'required|min:1|max:50',
      'text' => 'required|min:1|max:1000',
      'longitude' => 'required|min:1|max:50',
      'latitude' => 'required|min:1|max:50',
      'QRnumber' => 'required|min:1|max:50',
      'photo' => 'required|mimes:jpeg,png|max:2048',
    ]);

    $point = new Point;
    $point->name = $request->name;
    $point->text =  $request->text;
    $point->QRnumber = $request->QRnumber;
    $point->longitude = $request->longitude;
    $point->latitude = $request->latitude;
    $point->save();

    $Pointimage = Image::make($request->photo);
    Response::make($Pointimage->encode('png'));
    $form_data_pointImage = array(
      'assigned_Point'  => $point->id,
      'enctype' => "multipart/form-data",
      'Image' => $Pointimage
    );
    ImagesPoint::create($form_data_pointImage);
   

    $image_file = \QrCode::format('png')
      ->merge('/Applications/XAMPP/xamppfiles/htdocs/Bachelorarbeit/app/QR-Codes/logo.png', 0.2, true)
      ->size(400)->errorCorrection('H')
      ->generate($point->id);

    $image = Image::make($image_file);


    Response::make($image->encode('jpeg'));

    $form_data = array(
      'assigned_Point'  => $point->id,
      'enctype' => "multipart/form-data",
      'QR_Code_Image' => $image
    );
    Images::create($form_data);

    return redirect('/points')->with('success', 'Point saved!');
  }

  public function edit($id){
    $point = Point::find($id);
    if($point != null){
      return view('points.edit', compact('point'));
    }else{
      return response()->view('errors.' . '404', [], 404);
    }
  }

  public function show($id){
    $point = Point::find($id);
    if($point != null){
      return view('points.details', compact('point'));
    }else{
      return response()->view('errors.' . '404', [], 404);
    }
  }


  public function showqr($id)
  {
    $point = Point::find($id);
    if($point != null){
      return view('points.qr-codes', compact('point'));
    }else{
      return response()->view('errors.' . '404', [], 404);
    }
    
  }

  public function update(Request $request, $id){
    $this->validate(request(), [
      'name' => 'required|min:1|max:50',
      'text' => 'required|min:1|max:500',
      'longitude' => 'required|min:1|max:50',
      'latitude' => 'required|min:1|max:50',
      'QRnumber' => 'required|min:1|max:50',
    ]);

    if (Point::where('id', $id)->exists()) {
      $point = Point::find($id);
      $point->name = is_null($request->name) ? $point->name : $request->name;
      $point->text = is_null($request->text) ? $point->text : $request->text;
      $point->QRnumber = is_null($request->QRnumber) ? $point->QRnumber : $request->QRnumber;
      $point->longitude = is_null($request->longitude) ? $point->longitude : $request->longitude;
      $point->latitude = is_null($request->latitude) ? $point->latitude : $request->latitude;
      $point->save();
      return redirect('/points')->with('success', 'Point updated!');
    }
  }

  public function destroy($id)
  {
    if (Point::where('id', $id)->exists()) {
      $point = Point::find($id);
      $point->delete();
      return redirect('/points')->with('success', 'Point deleted!');
    }
  }
}
