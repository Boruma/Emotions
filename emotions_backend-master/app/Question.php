<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Question extends Model
{
    protected $fillable = ['Text','Emotion'];

    public function answers(){
        return $this->hasMany('App\Answer','answer_ID','answer_ID');
    }

    public function points(){
        return $this->hasMany('App\Point','point_ID','point_ID');
    }
}
