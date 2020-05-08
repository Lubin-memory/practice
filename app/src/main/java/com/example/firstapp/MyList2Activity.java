package com.example.firstapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable{
    private String TAG="MyList2:";
    Handler handler;
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter; // 适配器
    private int msgWhat = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.list_item);
        initListView();

        this.setListAdapter(listItemAdapter);
//        MyAdapter myAdapter = new MyAdapter(this,R.layout.list_item,listItems);
//        this.setListAdapter(myAdapter);
        Thread t = new Thread(this); // 创建新线程
        t.start(); // 开启线程


        handler=new Handler(){
            public void handleMessage(Message msg) {
                if(msg.what == msgWhat){
                    List<HashMap<String, String>> rateList = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(MyList2Activity.this, rateList, // listItems数据源
                            R.layout.list_item, // ListItem的XML布局实现
                            new String[] { "ItemTitle", "ItemDetail" },
                            new int[] { R.id.itemTitle, R.id.itemDetail });
                    setListAdapter(adapter);
                    Log.i("handler","reset list...");
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG,"OnItemlick:parent"+parent);
                Log.i(TAG,"OnItemlick:view="+view);
                Log.i(TAG,"OnItemlick:id="+id);

                //从map中获取数据
                HashMap<String,String> map= (HashMap<String, String>) getListView().getItemAtPosition(position);
               String titleStr= map.get("ItemTitle");
                String detailStr= map.get("ItemDetail");
                Log.i(TAG,"OnItemlick:titleStr="+titleStr);
                Log.i(TAG,"OnItemlick:detailStr="+detailStr);
                //从view中获取数据
                TextView title=(TextView) view.findViewById(R.id.itemTitle);
                TextView detail=(TextView) view.findViewById(R.id.itemDetail);
               String title2= (String) title.getText();
                String detail2= (String) title.getText();
                Log.i(TAG,"OnItemlick:title2="+title2);
                Log.i(TAG,"OnItemlick:detail2="+detail2);

                //打开新的页面，传入参数

//                Intent rateCalc = new Intent(this,RateCalcActivity.class);
//                rateCalc.putExtra("title",titleStr);
//                rateCalc.putExtra("rate",Float.parseFloat(detailStr));
//                startActivity(rateCalc)
            }
        });
    }
    @Override
    public void run() {
        Log.i("thread","run.....");
        boolean marker = false;
        List<HashMap<String, String>> rateList = new ArrayList<HashMap<String, String>>();

        try {
            Document doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();
            Elements tbs = doc.getElementsByClass("tableDataTable");
            Element table = tbs.get(0);
            Elements tds = table.getElementsByTag("td");
            for (int i = 6; i < tds.size(); i+=6) {
                Element td = tds.get(i);
                Element td2 = tds.get(i+3);
                String str1 = td.text();
                String val = td2.text();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", str1);
                map.put("ItemDetail", val);

                rateList.add(map);
                Log.i("td",str1 + "=>" + val);
            }
            marker = true;
        } catch (MalformedURLException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage();
        msg.what = msgWhat;
        if(marker){
            msg.arg1 = 1;
        }else{
            msg.arg1 = 0;
        }

        msg.obj = rateList;
        handler.sendMessage(msg);

        Log.i("thread","sendMessage.....");
    }

    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate： " + i); // 标题文字
            map.put("ItemDetail", "detail" + i); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems数据源
                R.layout.list_item, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }//上下一一对应
        );
    }

}
