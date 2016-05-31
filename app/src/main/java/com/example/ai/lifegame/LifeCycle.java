package com.example.ai.lifegame;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by AI on 26.05.16.
 */
public class LifeCycle {
    private Integer[] array;
    private Integer[] newArray;
    private int id;
    private int item;
    private int size;

    LifeCycle(int initSize) {
        this.size = initSize;
    }

    public Integer[] getArray() {
        return this.array;
    }

    public int getItem(int id) {
        return this.array[id];
    }

    public void setItem(int item, int id) {
        this.array[id] = item;
    }
    private void setNewItem(int item, int id) {
        this.newArray[id] = item;
    }

    public void setNewStartArray(boolean rand) {
        int num;
        this.array = new Integer[this.size * this.size];
        Random r = new Random();
        for (int i = 0; i < this.size * this.size; i++) {
            if (rand) {
                num = r.nextInt(1);
            } else {
                num = 0;
            }
            this.array[i] = num;
        }
    }

    public void toggleItem(int id) {
        int num;
        if (this.array[id] == 1) {
            num = 0;
        } else {
            num = 1;
        }
        this.setItem(num, id);
    }

    public int getColor(int id) {
        int clr;
        if (this.array[id] == 1) {
            clr = Color.BLACK;
        } else {
            clr = Color.WHITE;
        }
        return clr;
    }

    private int checkElement(int id) {
        int count;
        if (id == 0 || id == (this.size - 1) || id == (this.size * this.size - 1) || id == (this.size * (this.size - 1))) {
            count = this.checkAngles(id);
        } else if (id / this.size < 1 || id / this.size >= (this.size - 1)) {
            count = this.checkFirstAndLastRow(id);
        } else if (id % this.size == 0 || (id + 1) % this.size == 0) {
            count = checkFirstAndLAstColumn(id);
        } else {
            count = checkAnotherVariants(id);
        }

        return count;
    }

    private int checkAngles(int id) {
        int count = 0;
        int helpCount1 = 1, helpCount2 = 1, helpCount3 = 0, helpCount4 = 1;
        if (id == this.size * (this.size - 1)) {
            helpCount2 = this.size - 2;
        } else if (id == (this.size - 1)) {
            helpCount1 = -1;
            helpCount2 = 2;
            helpCount3 = -1;
            helpCount4 = -2;
        } else if (id == (this.size * this.size - 1)) {
            helpCount1 = -1;
            helpCount2 = this.size - 2;
            helpCount3 = -1;
            helpCount4 = -2;
        }

        if (this.array[id + helpCount1] == 1) {
            count++;
        }
        if (this.array[this.size * helpCount2 + helpCount3] == 1) {
            count++;
        }
        if (this.array[this.size * helpCount2 + helpCount4] == 1) {
            count++;
        }
        return count;
    }

    private int checkFirstAndLastRow(int id) {
        int count = 0;
        count += this.checkOnThisRow(id);
        if (id / this.size < 1) {
            count += checkNextRow(id);
        } else {
            count += checkPreviousRow(id);
        }
        return count;

    }

    private int checkFirstAndLAstColumn(int id) {
        int count = 0, helpCount = 1;
        count += this.checkOnThisColumn(id);
        if ((id + 1) % this.size == 0) {
            helpCount = -1;
        }
        if (this.array[id + helpCount] == 1) {
            count++;
        }
        if (this.array[id - this.size + helpCount] == 1) {
            count++;
        }
        if (this.array[id + this.size + helpCount] == 1) {
            count++;
        }
        return count;
    }

    private int checkAnotherVariants(int id) {
        int count = 0;

        count += this.checkOnThisRow(id);
        count += this.checkNextRow(id);
        count += this.checkPreviousRow(id);
        return count;
    }

    private int checkOnThisRow(int id) {
        int count = 0;

        if (this.array[id - 1] == 1) {
            count++;
        }
        if (this.array[id + 1] == 1) {
            count++;
        }
        return count;
    }

    private int checkNextRow(int id) {
        int count = 0;
        if (this.array[id + this.size] == 1) {
            count++;
        }
        if (this.array[id + 1 + this.size] == 1) {
            count++;
        }
        if (this.array[id - 1 + this.size] == 1) {
            count++;
        }
        return count;
    }

    private int checkPreviousRow(int id) {
        int count = 0;
        if (this.array[id - this.size] == 1) {
            count++;
        }
        if (this.array[id + 1 - this.size] == 1) {
            count++;
        }
        if (this.array[id - 1 - this.size] == 1) {
            count++;
        }
        return count;
    }

    private int checkOnThisColumn(int id) {
        int count = 0;
        if (this.array[id - this.size] == 1) {
            count++;
        }
        if (this.array[id + this.size] == 1) {
            count++;
        }
        return count;
    }

    public void newCycle()
    {
        int i;
        for (i=0; i<this.size*this.size; i++)
        {
           if (this.checkElement(i)==3) {
               this.setNewItem(1,i);
           }else {
               this.setNewItem(0,i);
           }
        }
        this.array = this.newArray;
    }
}
