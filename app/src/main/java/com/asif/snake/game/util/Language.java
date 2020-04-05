
package com.asif.snake.game.util;

import android.content.Context;

import com.asif.snake.R;


public final class Language {

    public static String getModeLanguageByNumber(Context context, int number) {
        switch (number) {
            case 0:
                return context.getResources().getString(R.string.mode_normal);
            case 1:
                return context.getResources().getString(R.string.mode_no_border);
            case 2:
                return context.getResources().getString(R.string.mode_portal);
            case 3:
                return context.getResources().getString(R.string.mode_combined);
        }
        return context.getResources().getString(R.string.mode_normal);
    }

    public static String getLanguageColorByNumber(Context context, int number) {
        switch (number) {
            case 1:
                return context.getResources().getString(R.string.color_grey);
            case 2:
                return context.getResources().getString(R.string.color_black);
            case 3:
                return context.getResources().getString(R.string.color_white);
            case 4:
                return context.getResources().getString(R.string.color_green);
            case 5:
                return context.getResources().getString(R.string.color_blue);
            case 6:
                return context.getResources().getString(R.string.color_red);
            case 7:
                return context.getResources().getString(R.string.color_gold);
            case 8:
                return context.getResources().getString(R.string.color_yellow);
            case 9:
                return context.getResources().getString(R.string.color_magenta);
            case 10:
                return context.getResources().getString(R.string.color_darkslategrey);
            case 11:
                return context.getResources().getString(R.string.color_maroon);
            case 12:
                return context.getResources().getString(R.string.color_orange_red);
            case 13:
                return context.getResources().getString(R.string.color_lime_green);
            case 14:
                return context.getResources().getString(R.string.color_deep_sky_blue);
            case 15:
                return context.getResources().getString(R.string.color_medium_slate_blue);
        }
        return context.getResources().getString(R.string.color_black);
    }
}
