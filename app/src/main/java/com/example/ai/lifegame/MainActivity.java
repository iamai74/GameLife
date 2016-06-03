package com.example.ai.lifegame;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private static final int minSpeed = 300;
    private static final int maxSpeed = 3000;

    int size = 10;
    int speed = 1000;
    LifeCycle cycle;
    GridView gvMain;
    ArrayAdapter<Integer> adapter;
    Button startButton, nextButton, clearButton, helpButton, randomButton;
    SeekBar speedBar;
    Switch cycleSwitch;


    private boolean stop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //находим все кнопки и меняем надписи на них
        startButton = (Button) findViewById(R.id.startStopButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        helpButton = (Button) findViewById(R.id.helpButton);
        randomButton = (Button) findViewById(R.id.randomButton);

        helpButton.setText(String.valueOf('?'));
        startButton.setText("Start");
        nextButton.setText("Next");
        clearButton.setText("Clear");
        randomButton.setText("Random");

        //создаем новый объект цикла жизни, в котором вся бизнесс логика
        cycle = new LifeCycle(size);
        cycle.setNewStartArray(false);

        //находим гридвью и звполняем его
        adapter = new ArrayAdapter<Integer>(this, R.layout.item, R.id.tvText, cycle.getArray());
        gvMain = (GridView) findViewById(R.id.sandBox);
        gvMain.setAdapter(adapter);
        gvMain.setNumColumns(size);

        //регулятор скорости и обработка его изменений
        speedBar = (SeekBar) findViewById(R.id.speedBar);
        speedBar.setProgress(Math.round(speed * 100 / (maxSpeed - minSpeed)));
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed = Math.round(speedBar.getProgress() * (maxSpeed - minSpeed) / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //переключатель в режим работы цикличного поля
        cycleSwitch = (Switch) findViewById(R.id.cycleSwitch);
        cycleSwitch.setChecked(cycle.getCycle());
        cycleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                cycle.setCycle(isChecked);
            }
        });

        //обработка клика на любой ячейке гридвью
        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View cell = gvMain.getChildAt(i);
                cycle.toggleItem(i);
                cell.setBackgroundColor(cycle.getColor(i));
            }
        });

        //кнопка очистки
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycle.setNewStartArray(false);
                updateWorkField();
            }
        });

        //кнопка рандома
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycle.setNewStartArray(true);
                updateWorkField();
            }
        });

        //кнопка следующего шага
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycle.newCycle();
                updateWorkField();
            }
        });

        //кнопка старт-стоп
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop = !stop;
                toggleWorkField(stop);
                Thread myThread = new Thread(myRunnable);
                myThread.start();
            }
        });

        //кнопка помощи
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpAlert();
            }
        });
    }

    //алерт при нажатии на помощь
    private void helpAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("About")
                .setMessage("Game \"LIFE\" app for android. \nAuthor: Eugene Zhigunov.")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //тоглим состояние рабочего пространства при нажатии на страт-стоп
    private void toggleWorkField(boolean start) {
        if (start) {
            startButton.setText("start");
        } else {
            startButton.setText("stop");

        }
        nextButton.setEnabled(start);
        randomButton.setEnabled(start);
        clearButton.setEnabled(start);
        helpButton.setEnabled(start);
        gvMain.setEnabled(start);
        cycleSwitch.setEnabled(start);
    }

    //перерисовка рабочего поля
    private void updateWorkField() {
        int i;
        for (i = 0; i < size * size; i++) {
            View cell = gvMain.getChildAt(i);
            cell.setBackgroundColor(cycle.getColor(i));
        }
    }

    //процесс для автоматического обновления поля, при запуске старт-стоп
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            while (!stop) {
                cycle.newCycle();
                gvMain.post(new Runnable() {
                    @Override
                    public void run() {
                        updateWorkField();
                    }
                });
                try {
                    Thread.sleep(speed); // Waits for 1 second (1000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };
}
