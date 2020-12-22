<?php

use Illuminate\Database\Seeder;
use App\Point;

class PointSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        function rand_float($st_num = 0, $end_num = 1, $mul = 1000000)
        {
            if ($st_num > $end_num) return false;
            return mt_rand($st_num * $mul, $end_num * $mul) / $mul;
        }
        for ($i = 0; $i < 50; $i++) {
            $long = rand_float(7.487172846325537, 7.556467968109084); 
            $lat = rand_float(52.136294463711514, 52.209579367766636); 
            Point::create([
                'name' => 'Punkt ' . $i,
                'text' => Str::random(500),
                'longitude' => $long,
                'latitude' => $lat,
                'QRnumber' => '50',
            ]);
        }
    }
}
