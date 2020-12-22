<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Point extends Model
{
     //Validator Rules
     public static $rules = [
        'name' => 'required|min:1|max:50',
        'text' => 'required|min:1|max:50',
        'longitude' => 'required|min:1|max:50',
        'latitude' => 'required|min:1|max:50',
        'QRnumber' => 'required|min:1|max:50',
    ];
    //Validator Messages
    public static $messages = [
        'name.required'      => 'Der Name muss angegeben werden.',
        'text.required'      => 'Der Text muss angegeben werden.',
        'longitude.required' => 'Der Breitengrad muss angegeben werden.',
        'latitude.required'  => 'Der Längengrad muss angegeben werden.',
        'latitude.required'  => 'Die QR-Nummer muss angegeben werden.',
    ];
    
    //PrimaryKey
    protected $primaryKey = 'id';

    
    protected $fillable = [
        'id',
        'name',
        'text',
        'number',
        'QRnumber',
        'longitude',
        'latitude',
       ];


    public function questions(){
        return $this->belongsTo('App\Question','question_ID','question_ID');
    }

    public function user(){
        return $this->belongsToMany('App\User','user_ID','user_ID');
    }

}
