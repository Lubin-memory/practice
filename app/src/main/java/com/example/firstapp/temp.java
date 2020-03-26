package com.example.firstapp;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Scanner;


public class temp extends AppCompatActivity  {
    TextView show;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        show = (TextView) findViewById(R.id.textView2);
        input = (EditText) findViewById(R.id.inputText);
        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click", "On click...");

                String temp = input.getText().toString();
                show.setText("华氏温度为：" + (Integer.parseInt(temp) * 9) / 5 + 32);
            }
        });
    }
}



