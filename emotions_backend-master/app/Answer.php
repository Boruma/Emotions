<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Answer extends Model
{
    protected $fillable = ['Text','question_ID','user_ID'];

         //Validator Rules
         public static $rules = [
            'text' => 'required|min:1|max:50',

        ];
        //Validator Messages
        public static $messages = [
            'text.required'      => 'Der Text muss angegeben werden.',
        ];
    
         public function question() {
                return $this->belongsTo('App\Question','question_ID','question_ID');
        }
}
