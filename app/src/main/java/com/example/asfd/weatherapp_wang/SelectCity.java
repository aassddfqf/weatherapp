package com.example.asfd.weatherapp_wang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.wangluyue.app.MyApplication;
import cn.edu.pku.wangluyue.bean.City;

public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView mList;
    private List<City> cityList;
    ArrayList<City> filterDateList;
    String[] data =new String[3000];
    String[] data_id =new String[3000];
    //private ClearEditText

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        initViews();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                Intent i = new Intent();
                i.putExtra("cityCode", "101020100");
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.title_list:
                break;
            default:
                break;
        }
    }
    protected void initViews(){
        //mClearEditText = (ClearEditText)findViewById(R.id.title_list)
        mList = (ListView) findViewById(R.id.title_list);
        MyApplication myApplication = (MyApplication)getApplication();
       // System.out.println("测试测试测试测试一一一一一");
        cityList =  myApplication.getCityList();
        //System.out.println("测试测试测试测试二二二");
       // System.out.println(cityList.size());

        int i = 0;
        for(City city : cityList)
        {
            data[i]=city.getCity();
            data_id[i++]=(String)city.getNumber();
            //System.out.println(data_id[i-1]);
        }
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,data);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent tx = new Intent();
                String city = data_id[i];
                System.out.println(city);
                tx.putExtra("cityCode", city);
                Toast.makeText(SelectCity.this,"你单击了:"+i, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, tx);
                finish();
            }
        });

    }
}
