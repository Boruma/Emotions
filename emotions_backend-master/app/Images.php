<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Images extends Model
{
    protected $fillable = ['assigned_Point', 'QR_Code_Image'];
}
