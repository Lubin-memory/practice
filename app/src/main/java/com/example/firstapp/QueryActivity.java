package com.example.firstapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class QueryActivity extends ListActivity implements Runnable {
    EditText queryContent;
    ListView listView;
    Button btn;
    ArrayList<HashMap<String, String>> listResult;
    String TAG="QueryActivity";
    Handler handler;

    private int msgWhat = 7;

    private List<HashMap<String, String>>  listItems;
    private ArrayList<HashMap<String, String>>  listItems2;// 存放标题、网址
    private ArrayList<HashMap<String, String>>  listIResult;
    private SimpleAdapter listItemAdapter; // 适配器



    final Date today=Calendar.getInstance().getTime();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    final String todayStr = sdf.format(today);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_query);
        initListView();
        initListResult();

        listView = (ListView) findViewById(R.id.mylist2);
        btn=(Button)findViewById(R.id.button3);
        queryContent=(EditText) findViewById(R.id.edit_query);


        this.setListAdapter(listItemAdapter);


        Thread t = new Thread(this); // 创建新线程
        t.start(); // 开启线程


        SharedPreferences sharedPreferences=getSharedPreferences("myquery", Activity.MODE_PRIVATE);
         String updateDate=sharedPreferences.getString("update_date","");
        //获得当前系统时间
        int days=caculateTotalTime(updateDate, todayStr);
        if(days<=7){
            Log.i(TAG, "onCreate: 需要更新");
          Thread thread = new Thread(this);
            thread.start();
        }else{
            Log.i(TAG, "onCreate: 不需要更新");
        }

        handler=new Handler(){
            public void handleMessage(Message msg) {
                if(msg.what == msgWhat){
                    List<HashMap<String, String>> rateList = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(QueryActivity.this, rateList, // listItems数据源
                            R.layout.list_item, // ListItem的XML布局实现
                            new String[] { "ItemTitle","ItemDetail" },
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

                //跳转到标题对应的网页
                Uri uri = Uri.parse(detailStr);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                 startActivity(intent);


            }
        });




    }




    @Override
    public void run() {
        Log.i("thread","run.....");
        boolean marker = false;

        try {
            Document doc = Jsoup.connect("https://it.swufe.edu.cn/index/tzgg.htm").get();
                Log.i("title=",doc.title());
             Elements scripts=doc.getElementsByTag("script");
             int i=0;
             for(Element script:scripts){
                 Log.i(TAG, "run:script["+i+"]"+script);
                 i++;
             }
            Element script = scripts.get(10);
            Elements titles = script.getElementsByAttribute("title");
            Elements details = script.getElementsByAttribute("href");


        HashMap<String, String> map = new HashMap<String, String>();
            //保存获取的数据
            SharedPreferences sharedPreferences=getSharedPreferences("myquery", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
        for(Element title:titles ){
            map.put("ItemTitle", convertElemToSVG(title));
            Log.i("td",title + "=>" + title);
            editor.putString("ItemTitle",convertElemToSVG(title));
            editor.apply();
        }
        for(Element detail:details ){
            map.put("ItemDetail", convertElemToSVG(detail));
            Log.i("td",detail + "=>" + detail);
            editor.putString("ItemDetail",convertElemToSVG(detail));
            editor.apply();
        }
            listItems2 = new ArrayList<HashMap<String, String>>();
           listItems2.add(map);
            marker = true;
        } catch (MalformedURLException e) {
            Log.e("www" +
                    ".0", e.toString());
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


        msg.obj = listItems;
        handler.sendMessage(msg);

        Log.i("thread","sendMessage.....");


        //保存更新的日期
        SharedPreferences sharedPreferences=getSharedPreferences("myquery", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("update_date",todayStr);
        Log.i(TAG,"OnItemlick:updateDate="+todayStr);
        editor.apply();

    }








    //初始化ListView
    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 20; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Title： " + i); // 标题文字
            map.put("ItemDetail", "Detail： " + i); // 网址
        listItems.add(map);
        }
    }

    private void initListResult() {
        listResult = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Title： " + i); // 标题文字
            map.put("ItemDetail", "Detail： " + i); // 网址
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listResult, // listItems数据源
                R.layout.list_item, // ListItem的XML布局实现
                new String[] { "ItemTitle","ItemDetail" },
                new int[] { R.id.itemTitle,R.id.itemDetail}//上下一一对应
        );
    }

     public void onClick(View btn){
        //获取用户输入
        String str=queryContent.getText().toString();
        float r=0;
        if(str.length()>0) {

        handler = new Handler() {
        public void handleMessage(Message msg) {
        if (msg.what == msgWhat) {
            List<HashMap<String, String>> titleList = (List<HashMap<String, String>>) msg.obj;

        for (Map<String, String> m : titleList){
          for (String k : m.keySet()){
             String titleResult = compileKeyWord(k, queryContent.getText().toString());
             String detailResult=m.get(titleResult);
              HashMap<String, String> map = new HashMap<String, String>();
              map.put("ItemTitle", titleResult);
              map.put("ItemDetail", detailResult);
              listResult.add(map);
        } } } }
        };

        }else{//给用户提示信息
            Toast.makeText(this,"请输入查询内容",Toast.LENGTH_SHORT).show();//消息提示  LENGTH_SHORT显示时间短，
        }
}

    //关键字匹配
    public String compileKeyWord(String word, String keyWord) {
        Pattern pn = Pattern.compile(keyWord+"\\w|\\w"+keyWord+"\\w|\\w"+keyWord);
        Matcher mr = null;
        mr = pn.matcher(word);
        if (mr.find())  {
            return word;
        } else {
            return "匹配失败";
        }
    }


    //计算两个日期的间隔数
    public static int caculateTotalTime(String startTime,String endTime) {
        SimpleDateFormat formatter =   new SimpleDateFormat( "yyyy-MM-dd");
        Date date1=null;
        Date date = null;
        Long l = 0L;
        try {
            date = formatter.parse(startTime);

            long ts = date.getTime();
            date1 =  formatter.parse(endTime);
            long ts1 = date1.getTime();
            l = (ts - ts1) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l.intValue();
    }

    // 将element转换成字符串
    public  String convertElemToSVG(Element element) {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        try {
            transformer.transform(new DOMSource((Node) element), new StreamResult(buffer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        String elementStr = buffer.toString();
        return elementStr;
    }



}
