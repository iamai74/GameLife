package com.example.ai.lifegame;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    int size = 9;
    LifeCycle cycle;
    GridView gvMain;
    ArrayAdapter<Integer> adapter;
    Button startButton, nextButton, clearButton, helpButton, randomButton;


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

        cycle = new LifeCycle(size);
        cycle.setNewStartArray(false);
        adapter = new ArrayAdapter<Integer>(this, R.layout.item, R.id.tvText, cycle.getArray());
        gvMain = (GridView) findViewById(R.id.sandBox);
        gvMain.setAdapter(adapter);
        gvMain.setNumColumns(size);

        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View cell = gvMain.getChildAt(i);
                cycle.toggleItem(i);
                cell.setBackgroundColor(cycle.getColor(i));
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycle.setNewStartArray(false);
                updateWorkField();
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycle.setNewStartArray(true);
                updateWorkField();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycle.newCycle();
                updateWorkField();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop=!stop;
                toggleWorkField(stop);
                Thread myThread = new Thread(myRunnable);
                myThread.start();
            }
        });
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpAlert();
            }
        });
    }

    private void helpAlert()
    {
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

    private void toggleWorkField(boolean start)
    {
        if (start){
           startButton.setText("start");
        }else {
            startButton.setText("stop");

        }
        nextButton.setEnabled(start);
        randomButton.setEnabled(start);
        clearButton.setEnabled(start);
        helpButton.setEnabled(start);
        gvMain.setEnabled(start);
    }
    private void updateWorkField()
    {
        int i;
        for (i=0; i<size*size; i++)
        {
            View cell = gvMain.getChildAt(i);
            cell.setBackgroundColor(cycle.getColor(i));
        }
    }

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
                    Thread.sleep(1000); // Waits for 1 second (1000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };
}
