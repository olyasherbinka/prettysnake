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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asif.snake.R;
import com.asif.snake.SnakeActivity;

public class PopStart extends PopBase {
    Button logout_google;
    TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("OFFEN");
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .5));

        setContentView(R.layout.pop_start);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/spaceranger.ttf");
        //
        TextView highscoreView = findViewById(R.id.highscoreView);
        highscoreView.setTypeface(customFont);
        highscoreView.setText("Highscore:" + SnakeActivity.highscore);
        //
        TextView scoreView = findViewById(R.id.score);
        scoreView.setTypeface(customFont);
        scoreView.setText("Click to Start");
        greeting = findViewById(R.id.greeting);
        greeting.setTypeface(customFont);



        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.finish();
        return super.onTouchEvent(event);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
        //overridePendingTransition(R.anim.hold, R.anim.fade_in);
    }


    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        super.onStart();
    }

    public void closePopUP(View view) {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

   }
