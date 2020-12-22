<?php

use Illuminate\Database\Seeder;
use App\Question;
class QuestionSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        for ($i = 0; $i < 25; $i++) {
            Question::create([
                'text' => Str::random(10),
                'Emotion' => Str::random(15),
                'point_id' => 32,
                'TimeActive' => '16-24'
            ]);
        }
    }
}
