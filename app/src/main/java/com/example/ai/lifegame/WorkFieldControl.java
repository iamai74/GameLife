package com.example.ai.lifegame;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

/**
 * Created by AI on 23.06.16.
 */
public class WorkFieldControl {
    public GridView gridView;
    private LifeCycle cycle;
    private ArrayAdapter<Integer> adapter;
    private AppCompatActivity mainActivity;

    private int size  = 10;

    WorkFieldControl(GridView gView, AppCompatActivity activity) {
        this.gridView = gView;
        this.mainActivity = activity;
        this.startWorkField(activity);
    }

    public void setSize (int s) {
        this.size = s;
    }

    public void startWorkField (AppCompatActivity mainActivity)
    {
        this.cycle = new LifeCycle(this.size);
        this.cycle.setNewStartArray(false);
        this.adapter = new ArrayAdapter<Integer>(mainActivity, R.layout.item, R.id.tvText, cycle.getArray());
        this.gridView.setAdapter(this.adapter);
        this.gridView.setNumColumns(this.size);
    }

    public void setCycleWorkField(boolean cycleWorkField) {
        this.cycle.setCycle(cycleWorkField);
    }

    private void updateWorkField () {
        int i;
        for (i = 0; i < size * size; i++) {
            View cell = this.gridView.getChildAt(i);
            cell.setBackgroundColor(this.cycle.getColor(i));
        }
    }

    public void nextStep(){
        this.cycle.newCycle();
        this.updateWorkField();
    }

    public void clearField(boolean random) {
        this.cycle.setNewStartArray(random);
        this.updateWorkField();
    }

    public void toggleCell(int i){
        View cell = this.gridView.getChildAt(i);
        this.cycle.toggleItem(i);
        cell.setBackgroundColor(this.cycle.getColor(i));
    }
    public boolean isCycled() {
        return this.cycle.getCycle();
    }

    public void helpAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mainActivity);
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
}
