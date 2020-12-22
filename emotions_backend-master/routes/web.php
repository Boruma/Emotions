<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Auth::routes();

Route::get('/', 'Auth\PointController@index');
Route::resource('points', 'Auth\PointController');
Route::resource('questions', 'Auth\Questioncontroller');
Route::resource('login', 'Auth\AuthController');
Route::resource('quests', 'Auth\Questcontroller');
Route::post('signup', 'Auth\AuthController@signupUI');
Route::get('logout', 'Auth\AuthController@logoutUser');
Route::get('showqr/{id}', 'Auth\PointController@showqr');


Route::get('test', [ 'as' => 'test', 'uses' => 'Auth\AuthController@logoutUser']);