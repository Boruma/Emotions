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



use Illuminate\Support\Facades\Response;

use App\Leaderboard;
use App\User;
use App\Point;
use App\Images;
use App\Point_user;
use App\Quest_user;
use App\Target;
use App\Quest;
use Carbon\Carbon;

use Illuminate\Http\Request;
use Redirect;
use PDF;
use Validator;

class LeaderboradController extends Controller
{
  public function __construct()
  {
    $this->middleware('auth');
  }

  //Create a new Point_user and set assinged Quest to finished
  public function updateLeaderborad($uid, $pid)
  {
    if ((User::where('id', $uid)->exists())  && (Point::where('id', $pid)->exists())) {

      $point_user = new Point_user();
      $point_user->user_id = $uid;
      $point_user->point_id = $pid;
      $point_user->found_at = Carbon::now();
      $point_user->save();

      $quests = Quest_user::where('user_id', $uid)->get()->toArray();
      $collection3 = collect();

      foreach ($quests as $entry) {
        $obj = Quest_user::findOrFail($entry['id']);
        $entry['questobj'] = Quest::findOrFail($entry['quest_id']);
        $entry['questobj']['pointobj'] = Point::findOrFail($entry['questobj']['point_id']);
        if ($entry['questobj']['point_id'] == $pid) {
          $collection3->push($entry);
          switch ($obj['progress']) {
            case "0":
              $obj['progress'] = $obj['progress'] + 2;
              break;
            case "1":
              $obj['progress'] = $obj['progress'] + 1;
              break;
            case "2":
              break;
          }
          $obj->save();
        }
      }
      return response()->json(["message" => "Point added to User successfully", "point_user" => $point_user,  "Coll3" => $collection3], 200);  //"Quests" => $quests,
    } else {
      return response()->json([
        "message" => "Point not added to User"
      ], 404);
    }
  }
}
