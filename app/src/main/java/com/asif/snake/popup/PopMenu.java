/*
        Simple Snake Android Application
        Copyright (C) 2020  Anton "PoorSkill" Kesy

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.asif.snake.popup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.asif.snake.R;
import com.asif.snake.SnakeActivity;

public class PopMenu extends PopBase {
    Button achievements_button;
    Button leaderboard_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .3));

        setContentView(R.layout.pop_menu);


        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/spaceranger.ttf");
        //
        TextView highscoreView = findViewById(R.id.highscoreView);
        highscoreView.setTypeface(customFont);
        highscoreView.setText("Highscore:" + SnakeActivity.highscore);
        //
        TextView scoreView = findViewById(R.id.score);
        scoreView.setTypeface(customFont);
        scoreView.setText("Score:" + SnakeActivity.score);

    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
        //overridePendingTransition(R.anim.hold, R.anim.fade_in);
    }



    public void closePopUP(View view) {
        this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
