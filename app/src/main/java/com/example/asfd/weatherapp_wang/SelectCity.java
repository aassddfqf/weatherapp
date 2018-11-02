package com.example.asfd.weatherapp_wang;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.wangluyue.app.MyApplication;
import cn.edu.pku.wangluyue.bean.City;
import cn.edu.pku.wangluyue.util.PinYin;
//查询城市activity
public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView mList;
    private TextView mcitySelect;
    private List<City> cityList;
    private SearchView searchView;
    private ArrayAdapter<String>adapter;
    ArrayList<City> filterDateList;
    String[] data =new String[3000];
    String[] data_id =new String[3000];
    //private ClearEditText
    private ArrayList<String> mSearchResult = new ArrayList<>(); //搜索结果，只放城市名
    private Map<String,String> nameToCode = new HashMap<>();  //城市名到编码
    private Map<String,String> nameToPinyin = new HashMap<>(); //城市名到拼音





    //初始化
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        initViews();
    }

    @Override//单击返回事件
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
    //初始化城市列表
    protected void initViews(){
        //mClearEditText = (ClearEditText)findViewById(R.id.title_list)
        mList = (ListView) findViewById(R.id.title_list);
        MyApplication myApplication = (MyApplication)getApplication();
        mcitySelect = (TextView) findViewById(R.id.title_name);
        cityList =  myApplication.getCityList();
        System.out.println("测试测试测试测试二二二");
       // System.out.println(cityList.size());
        int i = 0;
        for(City city : cityList)
        {
            data[i]=city.getCity();
            data_id[i++]=(String)city.getNumber();
            //System.out.println(data_id[i-1]);
        }
        //System.out.println("测试测试测试测试33333333");
        adapter = new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,data);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override//单击选择城市事件
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent tx = new Intent();
                String city = data_id[i];
                System.out.println(city);
                tx.putExtra("cityCode", city);//传递citycode的值回主进程
                Toast.makeText(SelectCity.this,"你单击了:"+i, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, tx);
                finish();
            }
        });
        //System.out.println("测试测试测试测试44444");
        searchView = (SearchView)findViewById(R.id.search);
        searchView.setIconified(true); //需要点击搜索图标，才展开搜索框
        searchView.setQueryHint("请输入城市名称或拼音");
        //System.out.println("测试测试测试测试55555");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) { //搜索栏不空时，执行搜索
                    if (mSearchResult != null) //清空上次搜索结果
                         mSearchResult.clear();
                    //遍历 nameToPinyin 的键值（它包含所有城市名）
                    for (String str : nameToPinyin.keySet()) {
                        if (str.contains(newText)||nameToPinyin.get(str).contains(newText)) {
                            mSearchResult.add(str);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                //实际不执行，文本框一变化就自动执行搜索
                Toast.makeText(SelectCity.this, "检索中", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //System.out.println("测试测试测试测试66666");
        myApplication = MyApplication.getInstance();
        ArrayList<City> mCityList = (ArrayList<City>) myApplication.getCityList();
        String strName;
        String strNamePinyin;
        String strCode;
        for (City city : mCityList) {
            strCode = city.getNumber();
            strName = city.getCity();
            strNamePinyin = PinYin.converterToSpell(strName); //城市名解析成拼音

            nameToCode.put(strName, strCode); //城市名到城市编码
            nameToPinyin.put(strName,strNamePinyin); //城市名到拼音
            mSearchResult.add(strName); //初始状态包含全部城市
        }
        adapter = new ArrayAdapter<> (SelectCity.this, android.R.layout.simple_list_item_1, mSearchResult);
        mList.setAdapter(adapter); //接上适配器
    }
}
