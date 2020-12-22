<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Response;

use App\Question;
use App\Answer;
use Illuminate\Http\Request;
use Validator;
use Carbon\Carbon;

class Questioncontroller extends Controller
{
  public function __construct()
  {
    $this->middleware('auth');
  }

  public function getAllQuestions()
  {
    $question = Question::get()->toArray();
    usort($question, function ($a, $b) {
      return strcmp($a['id'], $b['id']);
    });
    return response()->json($question, 200);
  }



  public function createQuestion(Request $request)
  {
    $validator = Validator::make($request->toArray(), Question::$rules, Question::$messages);
    if ($validator->passes()) {
      $question = new Question;
      $question->text =  $request->text;
      $question->Emotion =  $request->Emotion;
      $question->point_id =  $request->point_id;
      $question->TimeActive =  $request->TimeActive;
      $question->save();

      return response()->json([
        "message" => "question created"
      ], 201);
    } else {
      return response()->json(['error' => $validator->errors()], 400);
    }
  }

  public function getquestion($id)
  {
    if (question::where('id', $id)->exists()) {
      $question = question::where('id', $id)->get()->toArray();
      return response()->json($question, 200);
    } else {
      return response()->json([
        "message" => "question not found"
      ], 404);
    }
  }

  public function updatequestion(Request $request, $id)
  {
    if (question::where('id', $id)->exists()) {
      $question = question::find($id);
      $question->text = is_null($request->text) ? $question->text : $request->text;
      $question->save();

      return response()->json([
        "message" => "question updated successfully"
      ], 200);
    } else {
      return response()->json([
        "message" => "question not found"
      ], 404);
    }
  }

  public function deletequestion($id)
  {
    if (question::where('id', $id)->exists()) {
      $question = question::find($id);
      $question->delete();

      return response()->json([
        "message" => "question deleted"
      ], 202);
    } else {
      return response()->json([
        "message" => "question not found"
      ], 404);
    }
  }

  //Alle Questions zu diesem Punkt
  public function getAllQuestionsToPoint($id)
  {
    $point = Question::where('point_ID', $id)->get()->toArray();

    $collection = collect();
    $collection2 = collect();
    $collection3 = collect();
    foreach ($point as $entry) {
      switch ($entry['TimeActive']) {
        case "0-8":
          $collection->push($entry);
          break;
        case "8-16":
          $collection2->push($entry);
          break;
        case "16-24":
          $collection3->push($entry);
          break;
      }
    }

    if (count($point) == 0) {
      return response()->json($point, 201);
    }

    //Convert to Berlin Timezone 
    $now = Carbon::now()->addHours(2);;
    $startTri1 = Carbon::createFromTimeString('00:00');
    $endTri1 = Carbon::createFromTimeString('8:00');

    $startTri2 = Carbon::createFromTimeString('08:00');
    $endTri2 = Carbon::createFromTimeString('16:00');

    $startTri3 = Carbon::createFromTimeString('16:00');
    $endTri3 = Carbon::createFromTimeString('24:00');


    if ($now->between($startTri1, $endTri1)) {
      return response()->json($collection, 200);
    }

    if ($now->between($startTri2, $endTri2)) {
      return response()->json($collection2, 200);
    }

    if ($now->between($startTri3, $endTri3)) {
      return response()->json($collection3, 200);
    }
  }

  /**************/
  /*UI METHODS */
  /*************/


  public function index()
  {
    $questions = Question::all();
    return view('questions.index', compact('questions'));
  }

  public function create()
  {
    return view('questions.create');
  }

  public function store(Request $request)
  {

    $this->validate(request(), [
      'text' => 'required|min:1|max:50',
      'Emotion' => 'required|min:1|max:50',
      'point_id' => 'required|min:1|max:50',
    ]);
    $question = new Question;
    $question->text =  $request->text;
    $question->Emotion =  $request->Emotion;
    $question->point_id =  $request->point_id;
    $question->TimeActive =  $request->TimeActive;
    $question->save();

    return redirect('/questions')->with('success', 'Question saved!');
  }

  public function edit($id)
  {
    $question = Question::find($id);
    if($question != null){
      return view('questions.edit', compact('question'));
    }else{
      return response()->view('errors.' . '404', [], 404);
    }
  }

  public function update(Request $request, $id)
  {
    if (Question::where('id', $id)->exists()) {
      $question = Question::find($id);
      $question->Text = is_null($request->Text) ? $question->Text : $request->Text;
      $question->Emotion = is_null($request->Emotion) ? $question->Emotion : $request->Emotion;
      $question->point_id = is_null($request->point_id) ? $question->point_id : $request->point_id;
      $question->TimeActive = is_null($request->TimeActive) ? $question->TimeActive : $request->TimeActive;
      $question->save();
      return redirect('/questions')->with('success', 'Question updated!');
    }
  }

  public function destroy($id)
  {
    if (Question::where('id', $id)->exists()) {
      $question = Question::find($id);
      $question->delete();
      return redirect('/questions')->with('success', 'Question deleted!');
    }
  }
}
