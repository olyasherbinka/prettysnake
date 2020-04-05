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
package com.asif.snake.game.objects;

import com.asif.snake.game.helper.CustomColor;

public class Portal {
    private CustomColor color1;
    private CustomColor color2;
    private int xPos1;
    private int yPos1;
    private int xPos2;
    private int yPos2;
    private boolean used;
    private boolean free;

    public Portal() {
        this.color1 = CustomColor.BLUE;
        this.color2 = CustomColor.YELLOW;
    }

    public Portal(CustomColor color1, CustomColor color2, int xPos1, int yPos1, int xPos2, int yPos2) {
        this.color1 = color1;
        this.color2 = color2;
        this.xPos1 = xPos1;
        this.yPos1 = yPos1;
        this.xPos2 = xPos2;
        this.yPos2 = yPos2;
    }

    public boolean checkLegalPosition() {
        return xPos1 != xPos2 && yPos1 != yPos2 && xPos1 != yPos1 && xPos1 != yPos2 && yPos1 != xPos2;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public CustomColor getColor1() {
        return color1;
    }

    public void setColor1(CustomColor color1) {
        this.color1 = color1;
    }

    public CustomColor getColor2() {
        return color2;
    }

    public void setColor2(CustomColor color2) {
        this.color2 = color2;
    }

    public int getxPos1() {
        return xPos1;
    }

    public void setxPos1(int xPos1) {
        this.xPos1 = xPos1;
    }

    public int getyPos1() {
        return yPos1;
    }

    public void setyPos1(int yPos1) {
        this.yPos1 = yPos1;
    }

    public int getxPos2() {
        return xPos2;
    }

    public void setxPos2(int xPos2) {
        this.xPos2 = xPos2;
    }

    public int getyPos2() {
        return yPos2;
    }

    public void setyPos2(int yPos2) {
        this.yPos2 = yPos2;
    }
}
