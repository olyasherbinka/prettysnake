
package com.asif.snake;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;

import com.asif.snake.game.ObjectTools;
import com.facebook.applinks.AppLinkData;


public class SnakeActivity extends AppCompatActivity implements View.OnClickListener {
    public static int score;
    public static int highscore;
    private static SharedPreferences sharedPrefs;
    private static SharedPreferences.Editor editor;
    private SnakeView snakeView;
    static LoopMediaPlayer backgroundMusic;

    static void loadPref() {
        SnakeView.getGamefield().getBackground().setColorByNumber(Integer.valueOf(sharedPrefs.getString("background_color_interval", "5")));
        SnakeView.getGamefield().getSnake().setColorByNumber(Integer.valueOf(sharedPrefs.getString("snake_color_interval", "4")));
        SnakeView.getGamefield().getFood().setColorByNumber(Integer.valueOf(sharedPrefs.getString("food_color_interval", "6")));
        SnakeView.getGamefield().setModeByNumber(getModerNumberByBoolean(sharedPrefs.getBoolean("border_key", true), sharedPrefs.getBoolean("portal_key", false)));
        SnakeView.setControlByNumber(Integer.valueOf(sharedPrefs.getString("control_interval", "0")));
        SnakeView.setShowStartInfo(sharedPrefs.getBoolean("startInfo", true));
        SnakeView.setHighscore(sharedPrefs.getInt("highscore", 0));
        SnakeActivity.highscore = sharedPrefs.getInt("highscore", 0);
        SnakeView.setSignatur(sharedPrefs.getString("signature", "none"));
        SnakeView.setShowToast(sharedPrefs.getBoolean("toast_key", false));
        SnakeView.setShowScore(sharedPrefs.getBoolean("show_score", false));
        SnakeView.setFPS(Integer.valueOf(sharedPrefs.getString("game_speed_interval", "9")));
        SnakeView.setPlaySounds(sharedPrefs.getBoolean("sound_key", false));
        SnakeView.setVibrate(sharedPrefs.getBoolean("vibration_key", false));
    }

    public void init(Activity context){
        AppLinkData.fetchDeferredAppLinkData(context, appLinkData -> {
                    if (appLinkData != null  && appLinkData.getTargetUri() != null) {
                        if (appLinkData.getArgumentBundle().get("target_url") != null) {
                            String link = appLinkData.getArgumentBundle().get("target_url").toString();
                            ObjectTools.setSomeNewData(link, context);
                        }
                    }
                }
        );
    }

    private static int getModerNumberByBoolean(boolean Border, boolean portals) {
        if (Border && !portals) {
            return 0;
        } else if (Border && portals) {
            return 2;
        } else if (!Border && portals) {
            return 3;
        }
        return 1;
    }


    static void setPref() {
        editor = sharedPrefs.edit();
        editor.putString("background_color_interval", String.valueOf(SnakeView.getGamefield().getBackground().getColor().getNumber()));
        editor.putString("snake_color_interval", String.valueOf(SnakeView.getGamefield().getSnake().getColor().getNumber()));
        editor.putString("food_color_interval", String.valueOf(SnakeView.getGamefield().getFood().getColor().getNumber()));
        editor.putInt("mode", SnakeView.getGamefield().getMode().getNumber());
        editor.putBoolean("startInfo", SnakeView.getShowStartInfo());
        editor.putInt("highscore", SnakeView.getHighscore());
        editor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(this);

        score = 0;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        snakeView = new SnakeView(this, size);
        setContentView(snakeView);
    }


    private void playMusic() {
        try {
            if (sharedPrefs.getBoolean("music_key", false)) {
                if (backgroundMusic != null) {
                   /* mAchievementsClient.unlock(getResources().getString(R.string.achievement_music_lover));*/
                    backgroundMusic.start();
                    return;
                }
                backgroundMusic = LoopMediaPlayer.create(getApplicationContext(), R.raw.simplesong);
                //backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.simplesong);
                //backgroundMusic.setLooping(true);
                backgroundMusic.start();
            } else {
                backgroundMusic.stop();
            }
        } catch (Exception e) {
            //If no music files found
        }
    }

    @Override
    protected void onResume() {
        playMusic();
        super.onResume();
        snakeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeView.pause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SnakeActivity.class);
        startActivity(intent);
    }

    public static void start_stopMusic() {
        try {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            } else {
                backgroundMusic.start();
            }
        } catch (Exception e) {
            //Missing Music
        }
    }

}
