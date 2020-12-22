<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Quest_user extends Model{
    
    protected $fillable = [
        'user_id',
        'quest_id',
        'progress'
       ];

}
