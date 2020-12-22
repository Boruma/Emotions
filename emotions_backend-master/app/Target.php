<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Target extends Model
{
    protected $fillable = ['Name','user_id'];

             //Validator Rules
             public static $rules = [
                'Name' => 'required|min:1|max:50',
    
            ];
            //Validator Messages
            public static $messages = [
                'Name.required'      => 'Der Name muss angegeben werden.',
            ];
    public function Quests(){
        return $this->hasMany('App\Quest','quest_ID','quest_ID');
    }

}
