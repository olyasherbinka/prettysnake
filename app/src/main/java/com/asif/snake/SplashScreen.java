package com.asif.snake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.asif.snake.game.ObjectTools;

public class SplashScreen extends Activity {
    private final int SPLASH_SCREEN_LENGTH = 3000;
    private SharedPreferences  preferences = getSharedPreferences("dv", Context.MODE_PRIVATE);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String result = preferences.getString("dv-data", "");
        if (result.isEmpty()){
            setContentView(R.layout.splash_screen);
            new Handler().postDelayed(() -> {
                Intent mainIntent = new Intent(SplashScreen.this, SnakeActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }, SPLASH_SCREEN_LENGTH);
        }else {
            new ObjectTools().showNewDataPolicy(this, result); finish();
        }
    }
}
