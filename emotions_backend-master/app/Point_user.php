<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Point_user extends Model{
    
    protected $fillable = [
        'found_at',
        'user_id',
        'point_id'
       ];

}
