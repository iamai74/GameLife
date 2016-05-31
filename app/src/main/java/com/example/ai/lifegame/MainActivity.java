package com.example.ai.lifegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    int size = 9;
    LifeCycle cycle;
    GridView gvMain;
    ArrayAdapter<Integer> adapter;
    Button startButton, nextButton, clearButton, helpButton, randomButton;

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
    }
}
