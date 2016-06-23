package com.example.ai.lifegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    SpeedControl speedControl;
    WorkFieldControl workFieldControl;
    GridView gvMain;
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

        //находим гридвью и звполняем его
        gvMain = (GridView) findViewById(R.id.sandBox);
        workFieldControl = new WorkFieldControl(gvMain, this);

        //регулятор скорости и обработка его изменений
        speedControl = new SpeedControl();
        speedBar = (SeekBar) findViewById(R.id.speedBar);
        speedBar.setProgress(speedControl.getProgress());
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speedControl.setSpeed(speedBar.getProgress());
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
        cycleSwitch.setChecked(workFieldControl.isCycled());
        cycleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                workFieldControl.setCycleWorkField(isChecked);
            }
        });

        //обработка клика на любой ячейке гридвью
        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                workFieldControl.toggleCell(i);
            }
        });

        //кнопка очистки
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workFieldControl.clearField(false);
            }
        });

        //кнопка рандома
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workFieldControl.clearField(true);
            }
        });

        //кнопка следующего шага
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workFieldControl.nextStep();
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
                workFieldControl.helpAlert();
            }
        });
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

    //процесс для автоматического обновления поля, при запуске старт-стоп
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            while (!stop) {
                workFieldControl.gridView.post(new Runnable() {
                    @Override
                    public void run() {
                        workFieldControl.nextStep();
                    }
                });
                try {
                    Thread.sleep(speedControl.getSpeed()); // Waits for 1 second (1000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };
}
