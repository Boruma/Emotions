<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;

use Illuminate\Support\Facades\Response;

use App\Question;
use App\Answer;
use Illuminate\Http\Request;
use Validator;

class Answercontroller extends Controller
{
  public function __construct()
  {
    $this->middleware('auth');
  }
  public function getAllAnswers()
  {
    $answer = Answer::get()->toArray();
    usort($answer, function ($a, $b) {
      return strcmp($a['Text'], $b['Text']);
    });
    return response()->json($answer, 200);
  }

  public function createAnswer(Request $request)
  {
    $validator = Validator::make($request->toArray(), Answer::$rules, Answer::$messages);
    if ($validator->passes()) {

      $answer = new Answer;
      $answer->text =  $request->text;
      $answer->TextFielText   =  $request->TextFielText;
      $answer->question_ID = $request->question_ID;
      $answer->user_ID = $request->user_ID;
      $answer->save();

      return response()->json([
        "message" => "Answer created"
      ], 201);
    } else {
      return response()->json(['error' => $validator->errors()], 400);
    }
  }

  public function getAnswer($id)
  {
    if (Answer::where('id', $id)->exists()) {
      $answer = Answer::where('id', $id)->get()->toArray();
      return response()->json($answer, 200);
    } else {
      return response()->json([
        "message" => "answer not found"
      ], 404);
    }
  }

  public function updateAnswer(Request $request, $id)
  {
    if (answer::where('id', $id)->exists()) {
      $answer = answer::find($id);
      $answer->text = is_null($request->text) ? $answer->text : $request->text;
      $answer->save();

      return response()->json([
        "message" => "answer updated successfully"
      ], 200);
    } else {
      return response()->json([
        "message" => "answer not found"
      ], 404);
    }
  }

  public function deleteAnswer($id)
  {
    if (answer::where('id', $id)->exists()) {
      $answer = answer::find($id);
      $answer->delete();

      return response()->json([
        "message" => "answer deleted"
      ], 202);
    } else {
      return response()->json([
        "message" => "answer not found"
      ], 404);
    }
  }

  public function getAllAnswersToQuestion($id)
  {
    $question = Answer::where('question_ID', $id)->get()->toArray();
    if (count($question) == 0) {
      return response()->json($question, 201);
    }
    return response()->json($question, 200);
  }


  public function getAllAnswersToAllQuestions($id)
  {
    $questions = Question::where('point_id', $id)->get()->toArray();
    $collection = collect([]);
    foreach ($questions as $entry) {
      $question = Question::find($entry['id']);
      $answers = Answer::where('question_ID', $entry['id'])->get()->toArray();
      $question['answers'] = $answers;
      $collection->push($question);
    }
    return response()->json($collection, 200);
  }
}
