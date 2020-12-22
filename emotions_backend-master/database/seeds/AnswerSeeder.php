<?php

use Illuminate\Database\Seeder;
use App\Answer;
use App\Quest;
use App\Quest_user;
class AnswerSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        /*
        for ($i = 0; $i < 100; $i++) {
            Answer::create([
                'text' => rand(-3, 3),
                'TextFielText' => Str::random(20),
                'question_ID' => rand(30, 35),
                'user_ID' => '1',
            ]);
        }
        for ($i = 0; $i < 25; $i++) {
            Quest::create([
                'Name' => Str::random(10),
                'point_id' => 32
            ]);
        }*/

        for ($i = 13; $i < 38; $i++) {
            Quest_user::create([
                'quest_id' => $i,
                'progress' => 0,
                'user_id' => 44,
            ]);
        }
    }
}
