package com.example.firstapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener {
      //  implements  View.OnClickListener{
    TextView out;
    EditText edit;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //控件的获取
        out=(TextView)findViewById(R.id.showtext);
        edit=(EditText)findViewById(R.id.inputText);

        out.setText("swufe");
        String str=edit.getText().toString();
         Log.i("input message","input="+str);

       Button btn=(Button)findViewById(R.id.btn1);
       //findViewById(R.id.btn1)为view对象
       //btn.setOnClickListener(this);    //this将会调用当前类的额方法，在这里调用类里面的onClick方法
       btn.setOnClickListener(new View.OnClickListener() {//接口不能直接创建对象，因此，先将接口实现成一个类，然后，new一个匿名对象
           @Override
           public void onClick(View v) {
               Log.i("click","On click...");
           }
       });
    }

    @Override
    public void onClick(View v) {
      Log.i("click","On click...");
      //  out=(TextView)findViewById(R.id.showtext);
       // edit=(EditText)findViewById(R.id.inputText);
       String str=edit.getText().toString();
        out.setText("Hello"+str);

    }


     public void btn(View v){
      String str=edit.getText().toString();
      out.setText("Hello"+str);
   }


    }





