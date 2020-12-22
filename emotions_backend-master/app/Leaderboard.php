<?php

namespace App;

use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Passport\HasApiTokens;
use Illuminate\Database\Eloquent\SoftDeletes;

class Leaderboard extends Authenticatable
{
    use Notifiable,HasApiTokens, SoftDeletes;


    protected $fillable = [
        'points', 'position'
    ];


    public function user(){
        return $this->belongsTo('App\User');
    }
}
