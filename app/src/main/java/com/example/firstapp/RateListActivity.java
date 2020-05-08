package com.example.firstapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
   String data[] = {"one", "two", "three"};//数组的弊端：长度是固定的
    Handler handler;
    private  int what;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //  ***必须得注释掉下面这句话，因为父类已经包含一个布局，不需要再通过页面去填充
        //  setContentView(R.layout.activity_rate_list);
        List<String> list1=new ArrayList<String>();
        for (int i=1;i<100;i++){
            list1.add("item"+i);

        }
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list1) ;
            //android.R.layout.simple_list_item_1中的R表示Android提供的资源,simple_list_item_1中只有一个TextView对象
        setListAdapter(adapter);
        //setListAdapter（）由父类ListActivity提供
        //表示当前对象由adapter管理

        Thread t=new Thread(this);
        t.start();


        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==7){
                    List<String> list2=( List<String >)msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this, android.R.layout.simple_list_item_1, list2) ;

                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public  void  run(){
        //获取网络数据，放入list中带回到主线程中
        Log.i("thread","run.....");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> rateList = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();

            Elements tbs = doc.getElementsByClass("tableDataTable");
            Element table = tbs.get(0);

            Elements tds = table.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i+=8) {
                Element td = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td.text();
                String val = td2.text();
                rateList.add(str1 + "=>" + val);

                Log.i("td",str1 + "=>" + val);
            }

        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(7);

        msg.obj = rateList;//返回的不再是Bundle，而是list
        handler.sendMessage(msg);

        Log.i("thread","sendMessage.....");
    }

}
