<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;

use Illuminate\Support\Facades\Response;

use App\Target;
use App\Quest;
use Illuminate\Http\Request;
use Validator;
//
class Targetcontroller extends Controller
{
  public function __construct()
  {
    $this->middleware('auth');
  }
  
  public function getAllTargets()
  {
    $targets = Target::get()->toArray();
    usort($targets, function ($a, $b) {
      return strcmp($a['Name'], $b['Name']);
    });
    return response()->json($targets, 200);
  }


  public function createTarget(Request $request)
  {
    $validator = Validator::make($request->toArray(), Target::$rules, Target::$messages);
    if ($validator->passes()) {
      $target = new Target;
      $target->Name =  $request->Name;
      $target->user_id =  $request->user_id;
      $target->save();

      return response()->json([
        "message" => "Target created"
      ], 201);
    } else {
      return response()->json(['error' => $validator->errors()], 400);
    }
  }

  public function getTarget($id)
  {
    if (Target::where('id', $id)->exists()) {
      $target = Target::where('id', $id)->get()->toArray();
      return response()->json($target, 200);
    } else {
      return response()->json([
        "message" => "Target not found"
      ], 404);
    }
  }

  public function updateTarget(Request $request, $id)
  {
    if (Target::where('id', $id)->exists()) {
      $target = Target::find($id);
      $target->Name = is_null($request->Name) ? $target->Name : $request->Name;
      $target->user_id = is_null($request->Name) ? $target->user_id : $request->Name;
      $target->save();

      return response()->json([
        "message" => "Target updated successfully"
      ], 200);
    } else {
      return response()->json([
        "message" => "Target not found"
      ], 404);
    }
  }

  public function deleteTarget($id)
  {
    if (Target::where('id', $id)->exists()) {
      $target = Target::find($id);
      $target->delete();

      return response()->json([
        "message" => "Target deleted"
      ], 202);
    } else {
      return response()->json([
        "message" => "Target not found"
      ], 404);
    }
  }
}
