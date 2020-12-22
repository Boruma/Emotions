<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Quest extends Model
{
    protected $fillable = ['Name'];

         //Validator Rules
         public static $rules = [
            'Name' => 'required|min:1|max:50',

        ];
        //Validator Messages
        public static $messages = [
            'Name.required'      => 'Der Name muss angegeben werden.',
        ];
    
       //  public function Target() {
       //         return $this->belongsTo('App\Target','target_ID','target_ID');
      //  }
}
