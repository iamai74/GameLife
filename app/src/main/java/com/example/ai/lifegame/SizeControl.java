package com.example.ai.lifegame;

/**
 * Created by AI on 23.06.16.
 */
public class SizeControl {

    private static final int minSize = 3;
    private static final int maxSize = 13;

    private int size = 7;

    public int getSize() {
        return size;
    }
    public int getSizeProgress() {
        return Math.round(this.size * 100 / (maxSize - minSize));
    }

    public void setSize(int progress){
        this.size = Math.round(progress * (maxSize - minSize) / 100) + 3;
    }
}
