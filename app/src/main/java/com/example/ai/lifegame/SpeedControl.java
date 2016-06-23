package com.example.ai.lifegame;

/**
 * Created by AI on 23.06.16.
 */
public class SpeedControl {

    private static final int minSpeed = 300;
    private static final int maxSpeed = 3000;

    private int speed = 1000;

    public int getSpeed() {
        return speed;
    }
    public int getProgress() {
        return Math.round(this.speed * 100 / (maxSpeed - minSpeed));
    }

    public void setSpeed(int progress){
        this.speed = Math.round(progress * (maxSpeed - minSpeed) / 100);
    }
}
