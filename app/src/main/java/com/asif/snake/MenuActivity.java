package com.asif.snake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        setContentView(R.layout.menu_activity);
        TextView highscoreView = findViewById(R.id.highscoreView);
        highscoreView.setText("Highscore:" + AchifActivity.highscoreNum);
        TextView scoreView = findViewById(R.id.score);
        scoreView.setText("Score:" + AchifActivity.scoreNum);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.finish();
        return super.onTouchEvent(event);
    }
}
