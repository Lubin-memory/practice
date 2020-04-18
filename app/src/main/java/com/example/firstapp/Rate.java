package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Rate extends AppCompatActivity implements Runnable{
    private float dollarRate=6.7f;
    private   float euroRate=1/11f;
    private   float wonRate=5f;
    String TAG="RateActivity";
    Handler handler;
    EditText rmb;
    TextView  show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=(EditText) findViewById(R.id.rmb);
        show=(TextView) findViewById(R.id.showOut);
        //获取sp里保存的数据
        SharedPreferences sharedPreferences=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        //默认一个配置文件  SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate=sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate=sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate=sharedPreferences.getFloat("won_rate",0.0f);
        Log.i(TAG,"onCreate:sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate:sp euroRate="+euroRate);
        Log.i(TAG,"onCreate:sp wonRate="+wonRate);
        //开启子线程,实现当前对象的多线程
        Thread t=new Thread(this);
        t.start();

        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what==5){
                    Bundle bdl=(Bundle)msg.obj;
                    dollarRate=bdl.getFloat("dollar-rate");
                    euroRate=bdl.getFloat("euro-rate");
                    wonRate=bdl.getFloat("won-rate");

                    Log.i(TAG, "handleMessage: dollarRate:" + dollarRate);
                    Log.i(TAG, "handleMessage: euroRate:" + euroRate);
                    Log.i(TAG, "handleMessage: wonRate:" + wonRate);
                    Toast.makeText(Rate.this, "汇率已更新", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

    }

    public void onClick(View btn){
        //获取用户输入

        String str=rmb.getText().toString();
        float r=0;
        if(str.length()>0) {
            r = Float.parseFloat(str);
        }else{//给用户提示信息
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();//消息提示  LENGTH_SHORT显示时间短，
        }

        if(btn.getId()==R.id.btn_dollar){
            // val=r * 6.7f;//加f完成强制转换
            show.setText(String.format("%.2f",r*dollarRate));
        }else if(btn.getId()==R.id.btn_euro){
            //   val=r * (1/11.0f);//加f完成强制转换
            show.setText(String.format("%.2f",r*euroRate));
        }else{
            //   val=r * 500;//加f完成强制转换
            show.setText(String.format("%.2f",r*wonRate));
        }

    }
    public void openOne(View v){
        openConfig();

        //打开一个页面activity
//        Log.i("open","openOne");
//        Intent hello=new Intent(this,SecondApp.class);//第一个参数是当前的上下文对象，即需要编辑下个窗口是由哪个窗口打开的，
//        //第二个参数是需要打开窗口的类名
//       // startActivity(hello);
//        //打开一个网页
//        Uri uri = Uri.parse("http://www.baidu.com");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//       // startActivity(intent);
//
//        //点击按钮转到拨号页面
//        Intent intent2 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+"87092110"));
//        startActivity(intent2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();

        }
        return  super.onOptionsItemSelected(item);

    }

    private void openConfig() {
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);

        Log.i(TAG, "openOne:dollar_rate_key=" + dollarRate);
        Log.i(TAG, "openOne:euro_rate_key=" + euroRate);
        Log.i(TAG, "openOne:won_rate_key=" + wonRate);
        startActivityForResult(config, 1);//requestCode
    }

    @Override
    public void onActivityResult(int requestcCode,int resultCode,Intent data){
        if(requestcCode==1&&resultCode==2) {
//            bundle.putFloat("key_dollar", newDollar);
//            bundle.putFloat("key_euro", newEuro);
//            bundle.putFloat("key_won", newWon);

            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult: dollarRate="+dollarRate);
            Log.i(TAG,"onActivityResult: euroRate="+euroRate);
            Log.i(TAG,"onActivityResult: wonRate="+wonRate);
            //将新设置的汇率保存到sp里
            SharedPreferences sharedPreferences=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"数据已保存到sp里");

        }
        super.onActivityResult(requestcCode,resultCode,data);
    }

    @Override
    public void run() {
        Log.i(TAG,"run.....");
        for (int i=0;i<6;i++){
            Log.i(TAG,"run:i="+i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



//       获取网络数据
//        URL url=null;
//        try{
//            url=new URL("http://www.usd-cny.com/icbc.htm");
//            HttpURLConnection http=(HttpURLConnection)url.openConnection();//获得链接
//           InputStream in = http.getInputStream();
//           String html=inputStream2String(in);
//           Document doc = Jsoup.parse(html);
//           Log.i(TAG,"run:html="+html);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //用于保存获取的汇率
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            String url = "http://www.usd-cny.com/bankofchina.htm";
            doc = Jsoup.connect(url).get();
            Log.i(TAG, "run: " + doc.title());
            Elements tables = doc.getElementsByTag("table");

            Element table6 = tables.get(5);
            //Log.i(TAG, "run: table6=" + table6);
            //获取TD中的数据
            Elements tds = table6.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td1.text();
                String val = td2.text();

                Log.i(TAG, "run: " + str1 + "==>" + val);

                float v = 100f / Float.parseFloat(val);
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate", v);
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate", v);
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate", v);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //bundle中保存所获取的汇率
        //获取美msg对象，用于返回主线程
        Message msg=handler.obtainMessage(5);
        // msg.what=5;
        //msg.obj="Hello fron run()...";
        msg.obj=bundle;
        handler.sendMessage(msg);
    }
    private String inputStream2String(InputStream inputStream)throws IOException{
        final int bufferSize=1024;
        final char[] buffer=new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        Reader in=new InputStreamReader(inputStream,"gb2312");
        while (true){
            int rsz=in.read(buffer,0, buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}