package com.example.ai.lifegame;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by AI on 26.05.16.
 */
public class LifeCycle {
    //массив текущих данных о состоянии игрового поля
    private Integer[] array;
    //массив данных для нового шага в развитии жизни
    private Integer[] newArray;
    //размер игрового поля
    private int size;
    //признак того, что влюч
    private boolean cycle = false;

    //инит класса
    LifeCycle(int initSize) {
        this.size = initSize;
    }


    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    public boolean getCycle() {
        return this.cycle;
    }

    public Integer[] getArray() {
        return this.array;
    }

    public void setItem(int item, int id) {
        this.array[id] = item;
    }

    private void setNewItem(int item, int id) {
        this.newArray[id] = item;
    }

    //установка первичного заполнения массива, если rand==true, то будет рандомное заполнение
    public void setNewStartArray(boolean rand) {
        int num;
        this.array = new Integer[this.size * this.size];
        Random r = new Random();
        for (int i = 0; i < this.size * this.size; i++) {
            if (rand) {
                num = r.nextInt(2);
            } else {
                num = 0;
            }
            this.array[i] = num;
        }
    }

    //тоглер переключения значения элемента.
    // используется при самостоятельном заполнении
    public void toggleItem(int id) {
        int num;
        if (this.array[id] == 1) {
            num = 0;
        } else {
            num = 1;
        }
        this.setItem(num, id);
    }

    //в засисимости от значения элемента массива,
    // возвращаем цвет, которым окрасить клетку
    public int getColor(int id) {
        int clr;
        if (this.array[id] == 1) {
            clr = Color.BLACK;
        } else {
            clr = Color.WHITE;
        }
        return clr;
    }

    /**вся сложная бизнесс логика начинается отсюда**/

    //проверка каждого элемента массива
    private int checkElement(int id) {
        int count;
        //если это углы
        if (id == 0 || id == (this.size - 1) || id == (this.size * this.size - 1) || id == (this.size * (this.size - 1))) {
            count = this.checkAngles(id);
        //если это верхняя или нижняя строка
        } else if (id / this.size < 1 || id / this.size >= (this.size - 1)) {
            count = this.checkFirstAndLastRow(id);
        //если это первый или последний столбец
        } else if (id % this.size == 0 || (id + 1) % this.size == 0) {
            count = checkFirstAndLAstColumn(id);
        //клетка расположена не в зонах исключений
        } else {
            count = checkAnotherVariants(id);
        }

        return count;
    }

    //проверка углов
    private int checkAngles(int id) {
        int count = 0;
        int helpCount1 = 1, helpCount2 = 1;
        if (id == this.size * (this.size - 1)) {
            helpCount2 = -1;
        } else if (id == (this.size - 1)) {
            helpCount1 = -1;
        } else if (id == (this.size * this.size - 1)) {
            helpCount1 = -1;
            helpCount2 = -1;
        }

        if (this.array[id + helpCount1] == 1) {
            count++;
        }
        if (this.array[id + this.size * helpCount2] == 1) {
            count++;
        }
        if (this.array[id + this.size * helpCount2 + helpCount1] == 1) {
            count++;
        }
        if (cycle) {
            count += checkCycleAnles(id);
        }
        return count;
    }

    //дополнительные проверки углов, если включен режим цикличного поля
    private int checkCycleAnles(int id) {
        int count = 0;
        int helpCount3 = 1, helpCount4 = 1, helpCount5 = -1;
        int helpCount6 = 1, helpCount7 = 1, helpCount8 = 1;
        if (id == this.size - 1) {
            helpCount3 = -1;
            helpCount4 = 0;
            helpCount5 = 1;
            helpCount7 = -1;
            helpCount8 = -1;
        } else if (id == this.size * this.size - 1) {
            helpCount3 = -1;
            helpCount4 = -1;
            helpCount5 = 1;
            helpCount6 = -1;
            helpCount8 = -1;
        } else if (id == this.size * (this.size - 1)) {
            helpCount4 = 0;
            helpCount6 = -1;
            helpCount7 = 1;
        }

        if (this.array[id + (this.size - 1) * helpCount3] == 1) {
            count++;
        }
        if (this.array[id + 2 * this.size * helpCount4 + helpCount5] == 1) {
            count++;
        }
        if (this.array[id + this.size * (this.size - 1) * helpCount6] == 1) {
            count++;
        }
        if (this.array[id + this.size * (this.size - 1) * helpCount6 + helpCount7] == 1) {
            count++;
        }
        if (this.array[id + this.size * (this.size - 1) * helpCount6 + (this.size - 1) * helpCount8] == 1) {
            count++;
        }
        return count;
    }

    //проверка первой и последней строки
    private int checkFirstAndLastRow(int id) {
        int count = 0;
        count += this.checkOnThisRow(id);
        if (id / this.size < 1) {
            count += checkNextRow(id);
        } else {
            count += checkPreviousRow(id);
        }
        if (cycle) {
            count += checkCycleRow(id);
        }
        return count;

    }

    //проверка первой и последней строки, если включен режим цикличного поля
    private int checkCycleRow(int id) {
        int count = 0;
        if (id / this.size < 1) {
            count += checkAnotherRow(id, 1);
        } else {
            count += checkAnotherRow(id, -1);
        }
        return count;
    }

    //математика для предыдущего метода
    private int checkAnotherRow(int id, int helper) {
        int count = 0;
        if (this.array[id + this.size * (this.size - 1) * helper] == 1) {
            count++;
        }
        if (this.array[id + 1 + this.size * (this.size - 1) * helper] == 1) {
            count++;
        }
        if (this.array[id - 1 + this.size * (this.size - 1) * helper] == 1) {
            count++;
        }
        return count;
    }

    //проверка первой и последней колонки
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
        if (cycle) {
            if (this.array[id - helpCount] == 1) {
                count++;
            }
            if (this.array[id + this.size * helpCount - helpCount] == 1) {
                count++;
            }
            if (this.array[id + 2 * this.size * helpCount - helpCount] == 1) {
                count++;
            }
        }
        return count;
    }

    //проверка всех "нормальных" вариантов
    private int checkAnotherVariants(int id) {
        int count = 0;

        count += this.checkOnThisRow(id);
        count += this.checkNextRow(id);
        count += this.checkPreviousRow(id);
        return count;
    }

    //проверка элементов в одной строке с проверяемым
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

    //в следующей строке
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

    //в предыдущей строке
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

    //в текущем столбце
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

    //метод, вызываемый для молучения нового состояния "жизни" на поле
    public void newCycle() {
        int i, count;
        this.newArray = new Integer[this.size * this.size];
        for (i = 0; i < this.size * this.size; i++) {
            count = this.checkElement(i);
            if (count == 3 || (this.array[i] == 1 && count == 2)) {
                this.setNewItem(1, i);
            } else {
                this.setNewItem(0, i);
            }
        }
        this.array = this.newArray;
    }
}
