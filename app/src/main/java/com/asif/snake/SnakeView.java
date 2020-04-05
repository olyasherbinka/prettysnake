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
package com.asif.snake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


import com.asif.snake.game.helper.CustomColor;
import com.asif.snake.game.helper.Mode;
import com.asif.snake.game.objects.Background;
import com.asif.snake.game.objects.Food;
import com.asif.snake.game.objects.Gamefield;
import com.asif.snake.game.objects.ObjectBase;
import com.asif.snake.game.objects.Snake;
import com.asif.snake.game.util.Heading;
import com.asif.snake.game.util.Language;
import com.asif.snake.game.util.Painter;
import com.asif.snake.popup.PopMenu;
import com.asif.snake.popup.PopStart;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.asif.snake.SnakeActivity.setPref;
import static com.asif.snake.game.util.OnTouchUtility.SwipeControll;
import static com.asif.snake.game.util.OnTouchUtility.click;
import static com.asif.snake.game.util.OnTouchUtility.swipeBottomLeftTopRight;
import static com.asif.snake.game.util.OnTouchUtility.swipeBottomRightTopLeft;
import static com.asif.snake.game.util.OnTouchUtility.swipeTopLeftBottomRight;
import static com.asif.snake.game.util.OnTouchUtility.swipeTopRightBottomLeft;


public class SnakeView extends SurfaceView implements Runnable {
    private int deadClicks;
    private static boolean playSounds;
    private static boolean vibrate;
    private static boolean clickControls;
    private static boolean swipeControls;
    private static boolean hControls;
    private static boolean showToast, showScore;
    private static String signatur;
    private static Gamefield gamefield;
    private static int highscore;
    private static long fps = 9;
    private static boolean showStartInfo;
    private final Handler handler;
    private Thread thread = null;
    private Context context;
    private boolean blinking, touched, updateNeeded, dead, justDied, startUp;
    private float xSwipe1;
    private float ySwipe1;
    private long nextFrameTime;
    private int score;
    private volatile boolean isPlaying;
    private final SurfaceHolder surfaceHolder;
    private final Paint paint;
    private Toast toast;
    private Bitmap pauseSignBlack;
    private Bitmap pauseSignWhite;

    Vibrator vibrator;

    private Handler mHandler = new Handler();

    private boolean swipeSettings;
    private boolean alreadyPoped;

    private static SoundPool soundPool;
    private static int eatSound;
    private static int deadSound;
    private static int portalSound;
    private String tag = "PoorDebug";

    /**
     * Constructor
     *
     * @param context
     * @param size
     */
    public SnakeView(Context context, Point size) {
        super(context);
        alreadyPoped = false;
        gamefield = new Gamefield(size.x, size.y);
        deadClicks = 0;
        pauseSignBlack = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause);
        int scale = (int) (gamefield.getSizeX() * 0.08);
        pauseSignBlack = Bitmap.createScaledBitmap(pauseSignBlack, scale, scale, false);
        pauseSignWhite = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pause_white);
        pauseSignWhite = Bitmap.createScaledBitmap(pauseSignWhite, scale, scale, false);
        swipeSettings = false;
        handler = new Handler();
        touched = true;
        updateNeeded = false;
        surfaceHolder = getHolder();
        paint = new Paint();
        startUp = false;
        loadPref();
        newGame();
        if (showStartInfo) {
            startUp = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (showToast) {
                        if (swipeSettings) {
                            Toast.makeText(getContext(), getResources().getString(R.string.starting_info), Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getContext(), getResources().getString(R.string.snake_control_info), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (showToast) {
                    Toast.makeText(getContext(), getResources().getString(R.string.highscore) + " " + highscore, Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            SoundPlayer(getContext());
            vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        } catch (Exception e) {
            //NO SOUND FILES or SDK Error
        }
        //Open Start Menu
        mHandler.postDelayed(mUpdateTimeTask, 420);


    }

    public void SoundPlayer(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        deadSound = soundPool.load(context, R.raw.deadsound, 1);
        portalSound = soundPool.load(context, R.raw.portalsound, 1);
        eatSound = soundPool.load(context, R.raw.eatsound, 1);
    }

    public static void setSignatur(String string) {
        signatur = string;
    }

    public static void setControlByNumber(Integer number) {
        clickControls = false;
        swipeControls = false;
        hControls = false;
        switch (number) {
            case 1:
                clickControls = true;
                return;
            case 2:
                hControls = true;
                return;
            default:
                swipeControls = true;
        }
    }

    /**
     * Opens StartMenu after Delay
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Intent intent = new Intent(getContext(), PopStart.class);
            getContext().startActivity(intent);
        }
    };


    public static void setFPS(int value) {
        fps = value;
    }

    public static boolean getShowStartInfo() {
        return showStartInfo;
    }

    public static void setShowStartInfo(boolean value) {
        showStartInfo = value;
    }

    public static int getHighscore() {
        return highscore;
    }

    public static void setHighscore(int value) {
        highscore = value;
    }

    public static Gamefield getGamefield() {
        return gamefield;
    }

    private void loadPref() {
        SnakeActivity.loadPref();
        booleanAchievements();
    }

    @Override
    public void run() {

        while (isPlaying) {
            // Updates by fps count
            if (updateRequired()) {
                update();
                draw();
            }

        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        loadPref();
        gamefield.checkSameColor(-1);
        setPref();
        alreadyPoped = false;
        if (gamefield.getMode() == Mode.PORTALS || gamefield.getMode() == Mode.COMBINED) {
            gamefield.spawnPortals();
        }
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Resets everything before starting a new game
     */
    private void newGame() {
        loadPref();
        gamefield.newGame();
        score = 0;
        nextFrameTime = System.currentTimeMillis();

    }


    /**
     * gets called if snake ate food: score++, snake.length++ and spawn new food
     */
    private void eatFood() {
        gamefield.getSnake().setLength(gamefield.getSnake().getLength() + 1);
        vibrate(300);

        if (playSounds) {
            try {
                soundPool.play(eatSound, 1.0f, 1.0f, 1, 0, 1.0f);
            } catch (Exception e) {

            }
        }
        gamefield.spawnFood();
        score++;
    }

    /**
     * calls snake methode to move snake and check if snake ate something
     */
    private void moveSnake() {
        gamefield.getSnake().move();
        detectFood();
    }


    /**
     * checks if snake should be dead (already dead, hit border, eaten itself)
     *
     * @return
     */
    private boolean detectDeath() {
        if (gamefield.checkEatenItself()) {

        }
        return gamefield.checkHitBorder() || gamefield.checkEatenItself();
    }

    /**
     * Updates all objects after call
     */
    private void update() {
        //recycleVariables();
        updateNeeded = false;
        if (dead) {
            deadClicks++;
            return;
        }
        moveSnake();
        if (justDied || detectDeath()) {
            vibrate(700);
            if (playSounds) {
                try {
                    soundPool.play(deadSound, 1.0f, 1.0f, 1, 0, 1.0f);
                } catch (Exception e) {

                }
            }
            if (score > highscore) {
                if (showToast) {
                    toastTextNoHandler(getResources().getString(R.string.newHighscore) + ": " + score);
                }
                setHighscore(score);
                setPref();
            } else {
                if (showToast) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getResources().getString(R.string.score) + ": " + score, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            //start again
            touched = false;
            dead = true;
            justDied = false;
            return;
        }
        if (gamefield.getSnake().getLength() > gamefield.getSnake().getxPos().length) {
            toastTextNoHandler(getResources().getString(R.string.win) + " " + score);
        }
        //Check Border
        gamefield.checkBorder();
        //Check Portal
        if (gamefield.checkPortals()) {
            vibrate(100);
            if (playSounds) {
                try {
                    soundPool.play(portalSound, 1.0f, 1.0f, 1, 0, 1.0f);
                } catch (Exception e) {

                }
            }
        }
        detectFood();
        gamefield.resetPortals();


    }

    private void booleanAchievements() {
        System.out.println("PoorDebug booleanAchievements");
        System.out.println("PoorDebug booleanAchievements gotAccount");
        if (vibrate) {
            System.out.println("PoorDebug booleanAchievements gotVibrate");
            /*Games.getAchievementsClient(getContext(), GoogleSignIn.getLastSignedInAccount(getContext()))
                    .unlock(getResources().getString(R.string.achievement_vibrator));*/
        }
        if (playSounds) {
           /* Games.getAchievementsClient(getContext(), GoogleSignIn.getLastSignedInAccount(getContext()))
                    .unlock(getResources().getString(R.string.achievement_sound_lover));*/
        }

    }





    private void vibrate(int lengthInMilliseconds) {
        if (vibrate) {
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(lengthInMilliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(lengthInMilliseconds);
                }
            } catch (Exception e) {
                //SDK Error}
            }
        }
    }

    private void toastTextNoHandler(final String text) {
        if (showToast) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * tests if snake eats food
     */
    private void detectFood() {
        if (gamefield.getSnake().getxPos()[0] == gamefield.getFood().getxPos() && gamefield.getSnake().getyPos()[0] == gamefield.getFood().getyPos()) {
            eatFood();
        }
    }

    /**
     * Draw canvas
     */
    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            //Background
            Painter.drawColor(gamefield.getBackground().getColor(), canvas);
            //Snake
            if (!touched && blinking) {
                blinking = false;
                for (int i = 0; i < gamefield.getSnake().getSize(); i++) {
                    if (i == 0) {
                        //snake head
                        Painter.drawRect(gamefield.getSnake().getxPosTmp()[i] * gamefield.getBlockSize(), gamefield.getSnake().getyPosTmp()[i] * gamefield.getBlockSize(), (gamefield.getSnake().getxPosTmp()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getSnake().getyPosTmp()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getSnake().getHeadColor(), canvas, paint);
                        continue;
                    }
                    Painter.drawRect(gamefield.getSnake().getxPosTmp()[i] * gamefield.getBlockSize(), gamefield.getSnake().getyPosTmp()[i] * gamefield.getBlockSize(), (gamefield.getSnake().getxPosTmp()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getSnake().getyPosTmp()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getSnake().getColor(), canvas, paint);
                }
            } else if (!touched) {
                blinking = true;
            } else {
                for (int i = 0; i < gamefield.getSnake().getSize(); i++) {
                    if (i == 0) {
                        //snake head
                        Painter.drawRect(gamefield.getSnake().getxPos()[i] * gamefield.getBlockSize(), gamefield.getSnake().getyPos()[i] * gamefield.getBlockSize(), (gamefield.getSnake().getxPos()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getSnake().getyPos()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getSnake().getHeadColor(), canvas, paint);
                        continue;
                    }
                    Painter.drawRect(gamefield.getSnake().getxPos()[i] * gamefield.getBlockSize(), gamefield.getSnake().getyPos()[i] * gamefield.getBlockSize(), (gamefield.getSnake().getxPos()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getSnake().getyPos()[i] * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getSnake().getColor(), canvas, paint);
                }
            }
            //Food
            Painter.drawRect(gamefield.getFood().getxPos() * gamefield.getBlockSize(), gamefield.getFood().getyPos() * gamefield.getBlockSize(), (gamefield.getFood().getxPos() * gamefield.getBlockSize()) + gamefield.getBlockSize(), (gamefield.getFood().getyPos() * gamefield.getBlockSize()) + gamefield.getBlockSize(), gamefield.getFood().getColor(), canvas, paint);
            //Portals
            if (gamefield.getMode() == Mode.PORTALS || gamefield.getMode() == Mode.COMBINED) {
                //Draw Portals
                Painter.drawCircle(gamefield.getPortal().getxPos1() * gamefield.getBlockSize() + gamefield.getBlockSize() / 2, gamefield.getPortal().getyPos1() * gamefield.getBlockSize() + gamefield.getBlockSize() / 2, gamefield.getBlockSize() / 2, gamefield.getPortal().getColor1(), canvas, paint);
                Painter.drawCircle(gamefield.getPortal().getxPos2() * gamefield.getBlockSize() + gamefield.getBlockSize() / 2, gamefield.getPortal().getyPos2() * gamefield.getBlockSize() + gamefield.getBlockSize() / 2, gamefield.getBlockSize() / 2, gamefield.getPortal().getColor2(), canvas, paint);
            }
            //Menu Icon
            if (gamefield.getBackground().getColor() == CustomColor.BLACK) {
                canvas.drawBitmap(pauseSignWhite, (float) (gamefield.getSizeX() * 0.025), (float) (gamefield.getSizeX() * 0.025), null);
            } else {
                canvas.drawBitmap(pauseSignBlack, (float) (gamefield.getSizeX() * 0.025), (float) (gamefield.getSizeX() * 0.025), null);
            }
            if (showScore) {
                Painter.drawText("" + score, (int) (gamefield.getSizeX() - gamefield.getSizeX() * 0.1), (int) (gamefield.getSizeY() * 0.05), (float) (gamefield.getSizeX() * .05), gamefield.getSnake().getColor(), canvas, paint);
            }
            if (startUp) {
                //TODO Tutorial
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    /**
     * checks if gamefield needs to get updated
     *
     * @return
     */
    private boolean updateRequired() {
        if (nextFrameTime <= System.currentTimeMillis()) {
            long MILLIS_PER_SECOND = 1000;
            nextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / fps;
            return true;
        }
        return false;
    }

    /**
     * TouchControls
     *
     * @param touchEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {
        startUp = false;
        performClick();
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                xSwipe1 = touchEvent.getX();
                ySwipe1 = touchEvent.getY();
                //Menu
                if (!alreadyPoped && xSwipe1 < gamefield.getSizeX() / 10 + gamefield.getSizeX() / 13 && ySwipe1 < gamefield.getSizeX() / 20 + gamefield.getSizeX() / 13) {
                    alreadyPoped = true;
                    SnakeActivity.score = score;
                    Intent intent = new Intent(getContext(), PopMenu.class);
                    getContext().startActivity(intent);
                }
                //miss restart
                if (!touched && deadClicks > 5) {
                    deadClicks = 0;
                    touched = true;
                    dead = false;
                    newGame();
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                float xSwipe2 = touchEvent.getX();
                float ySwipe2 = touchEvent.getY();
                if (swipeSettings) {
                    //TOP Right to bottom left

                    if (swipeTopRightBottomLeft(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 0.65, gamefield)) {
                        setNextColor(gamefield.getBackground());
                        return true;
                    }
                    //BOTTOM LEFT TO TOP RIGHT
                    if (swipeBottomLeftTopRight(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 0.65, gamefield)) {
                        setNextColor(gamefield.getSnake());
                        return true;
                    }
                    //BOTTOM Right to TOP left
                    if (swipeBottomRightTopLeft(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 0.65, gamefield)) {
                        setNextColor(gamefield.getFood());
                        return true;
                    }
                    //TOP LEFT TO BOTTOM RIGHT
                    if (swipeTopLeftBottomRight(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 0.65, gamefield)) {
                        setNextMode();
                        return true;
                    }
                }
                //If Snake in starting position
                gamefield.getSnake().startSnakeMovementFromStart();
                //just click
                if (clickControls && click(xSwipe1, ySwipe1, xSwipe2, ySwipe2, 10)) {
                    if (!updateNeeded) {
                        gamefield.getSnake().changeDirection();
                        updateNeeded = true;
                    }
                    return true;
                }

                //SwipeSnakeControll
                if (swipeControls) {
                    Heading swiped = SwipeControll(xSwipe1, ySwipe1, xSwipe2, ySwipe2);
                    if (swiped != null && gamefield.getSnake().isLegalMove(swiped)) {
                        gamefield.getSnake().setHeading(swiped);
                    }
                }

                return true;
        }
        return super.onTouchEvent(touchEvent);
    }

    /**
     * Sets next color for the param object
     *
     * @param object
     */
    private void setNextColor(ObjectBase object) {

        setShowStartInfo(false);
        object.setColorByNumber(object.getColor().getNumber() + 1);
        int colorEntryChangePrefernce = 0;
        String toastTestContent = "";
        if (object instanceof Background) {
            colorEntryChangePrefernce = 1;
            toastTestContent = getResources().getString(R.string.backgroundChanged) + " " + Language.getLanguageColorByNumber(getContext(), gamefield.getBackground().getColor().getNumber());
        } else if (object instanceof Food) {
            colorEntryChangePrefernce = 3;
            toastTestContent = getResources().getString(R.string.foodChanged) + " " + Language.getLanguageColorByNumber(getContext(), gamefield.getFood().getColor().getNumber());
        } else if (object instanceof Snake) {
            colorEntryChangePrefernce = 2;
            toastTestContent = getResources().getString(R.string.snakeChanged) + " " + Language.getLanguageColorByNumber(getContext(), gamefield.getSnake().getColor().getNumber());
        }
        gamefield.checkSameColor(colorEntryChangePrefernce);
        toastText(this.getContext(), toastTestContent, Toast.LENGTH_SHORT);
        setPref();
    }

    private void setNextMode() {
        setShowStartInfo(false);
        gamefield.setModeByNumber(gamefield.getMode().getNumber() + 1);
        if (gamefield.getMode() == Mode.PORTALS) {
            gamefield.spawnPortals();
        }
        setPref();
        toastText(this.getContext(), getResources().getString(R.string.mode) + ": " + Language.getModeLanguageByNumber(getContext(), gamefield.getMode().getNumber()), Toast.LENGTH_SHORT);
    }


    private void toastText(Context toastContext, String text, int toastDuration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(toastContext, text, toastDuration);
        if (showToast) {
            toast.show();
        }
    }

    public static boolean isShowToast() {
        return showToast;
    }

    public static void setShowToast(boolean showToast) {
        SnakeView.showToast = showToast;
    }

    public static boolean isShowScore() {
        return showScore;
    }

    public static void setShowScore(boolean showScore) {
        SnakeView.showScore = showScore;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public static void setPlaySounds(boolean value) {
        playSounds = value;
    }

    public static void setVibrate(boolean value) {
        vibrate = value;
    }
}
