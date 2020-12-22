<?php

Route::group([
    'namespace' => 'Auth',
    'middleware' => 'api',
    'prefix' => 'auth'
], function () {
    Route::post('login', 'AuthController@login');
    Route::post('signup', 'AuthController@signup');
    Route::get('signup/activate/{token}', 'AuthController@signupActivate');
    Route::get('signup/activateBackend/{token}', 'AuthController@signupActivateBackend');

    Route::group([
        'middleware' => 'auth:api'
    ], function() {
        //API Auth
        Route::get('logout', 'AuthController@logout');
        Route::get('user/{id}', 'AuthController@user');
        Route::post('user/{id}', 'AuthController@addAditionalUserData');
        Route::post('user/update/{id}', 'AuthController@updateUserData');
        Route::get('user/all/getAll', 'AuthController@getAllUsers');
        //Points
        Route::get('points', 'PointController@getAllPoints');
        Route::get('points/{id}', 'PointController@getPoint');
        Route::post('points/create', 'PointController@createPoint');
        Route::put('points/{id}', 'PointController@updatePoint');
        Route::delete('points/{id}','PointController@deletePoint');
        Route::get('points/QR/{id}','PointController@generateQRForPoint');
        Route::post('points/toUser/{uid}/{pid}', 'PointController@addPointToUSer');
        Route::get('store_image/fetch_image/{id}', 'PointController@fetch_image');
        Route::get('store_image/fetch_image/toPoint/{id}', 'PointController@fetch_imageToPoint');
        Route::get('store_image/fetch_image/PointImage/toPoint/{id}', 'PointController@fetch_PointImageToPoint');
        //Leaderbaord
        Route::get('points/leaderboard/7', 'PointController@getTop7DaysLeaderboard');
        Route::get('points/leaderboard/1', 'PointController@getTop1DayLeaderboard');
        Route::get('points/leaderboard/All', 'PointController@getTopAllTimeLeaderboard');
        Route::get('leaderboard', 'LeaderboradController@getLeaderboard');
        Route::post('leaderboard/{id1}/{id2}', 'PointController@updateLeaderborad');
        //Answers
        Route::get('answer', 'AnswerController@getAllAnswers');
        Route::get('answer/{id}', 'AnswerController@getAnswer');
        Route::post('answer/create', 'AnswerController@createAnswer');
        Route::put('answer/{id}', 'AnswerController@updateAnswer');
        Route::delete('answer/{id}','AnswerController@deleteAnswer');
        Route::get('answer/toQuestion/{id}', 'AnswerController@getAllAnswersToQuestion');
        Route::get('answer/alltoQuestion/{id}', 'AnswerController@getAllAnswersToAllQuestions');
        //Questions
        Route::get('questions', 'Questioncontroller@getAllQuestions');
        Route::get('questions/{id}', 'Questioncontroller@getquestion');
        Route::post('questions/create', 'Questioncontroller@createQuestion');
        Route::put('questions/{id}', 'Questioncontroller@updatequestion');
        Route::delete('questions/{id}','Questioncontroller@deletequestion');
        Route::get('questions/toPoint/{id}', 'Questioncontroller@getAllQuestionsToPoint');
        //Targets
        Route::get('target', 'Targetcontroller@getAllTargets');
        Route::get('target/{id}', 'Targetcontroller@getTarget');
        Route::post('target/create', 'Targetcontroller@createTarget');
        Route::put('target/{id}', 'Targetcontroller@updateTarget');
        Route::delete('target/{id}','Targetcontroller@deleteTarget');
        //Quests
        Route::get('quest', 'Questcontroller@getAllQuests');
        Route::get('quest/{id}', 'Questcontroller@getQuest');
        Route::post('quest/create', 'Questcontroller@createQuest');
        Route::put('quest/{id}', 'Questcontroller@updateQuest');
        Route::delete('quest/{id}','Questcontroller@deleteQuest');
        Route::get('quest/toUser/{id}', 'Questcontroller@getAllTargetsToUser');
    });
});
