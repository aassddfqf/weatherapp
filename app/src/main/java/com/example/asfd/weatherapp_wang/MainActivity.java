package com.example.asfd.weatherapp_wang;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.wangluyue.bean.City;
import cn.edu.pku.wangluyue.bean.GuideAdapter;
import cn.edu.pku.wangluyue.util.LocationUtil;
import cn.edu.pku.wangluyue.util.NetUtil;
import cn.edu.pku.wangluyue.bean.TodayWeather;
//添加单击时间
public class MainActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener
{

    private static final int UPDATE_TODAY_WEATHER = 1;

    private ImageView mCitySelect;
    private ImageView mUpdateBtn;//为更新按钮imageView添加单击事件
    private ImageView mLocation;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;//文本信息
    private ImageView weatherImg, pmImg;//图片信息
    //调用todayweather更新天气
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    private GuideAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.iv1,R.id.iv2};
    private TextView week_today,temperature,climate,wind,week_today1,temperature1,climate1,wind1 ,week_today2,temperature2,climate2,wind2;
    private TextView week_today3,temperature3,climate3,wind3,week_today4,temperature4,climate4,wind4,week_today5,temperature5,climate5,wind5;
    private ImageView weather_img,weather_img1,weather_img2,weather_img3,weather_img4,weather_img5;

    //activity在被创建时调用，初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }
        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        mLocation = (ImageView) findViewById(R.id.title_location);
        mLocation.setOnClickListener(this);
        initViews();//初始化滑动页面
        initDots();//初始化小圆点
        initView();//初始化界面控件
    }

    void initDots() {
        dots = new ImageView[views.size()];
        Log.d("myWeather", "网络OK");
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.sixday1, null));
        views.add(inflater.inflate(R.layout.sixday2, null));
        vpAdapter = new GuideAdapter(this,views);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int a = 0; a < ids.length; a++) {
            if (a == position) {
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            } else {
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        //
        week_today=views.get(0).findViewById(R.id.week_today);
        temperature=views.get(0).findViewById(R.id.temperature);
        climate=views.get(0).findViewById(R.id.climate);
        weather_img = views.get(0).findViewById(R.id.weather_img);
        wind = views.get(0).findViewById(R.id.wind);
        //
        week_today1=views.get(0).findViewById(R.id.week_today1);
        temperature1=views.get(0).findViewById(R.id.temperature1);
        climate1=views.get(0).findViewById(R.id.climate1);
        weather_img1 = views.get(0).findViewById(R.id.weather_img1);
        wind1=views.get(0).findViewById(R.id.wind1);
        //
        week_today2=views.get(0).findViewById(R.id.week_today2);
        temperature2=views.get(0).findViewById(R.id.temperature2);
        climate2=views.get(0).findViewById(R.id.climate2);
        weather_img2 = views.get(0).findViewById(R.id.weather_img2);
        wind2=views.get(0).findViewById(R.id.wind2);
        //
        week_today3=views.get(1).findViewById(R.id.week_today);
        temperature3=views.get(1).findViewById(R.id.temperature);
        climate3=views.get(1).findViewById(R.id.climate);
        weather_img3 = views.get(1).findViewById(R.id.weather_img);
        wind3= views.get(1).findViewById(R.id.wind);
        //
        week_today4=views.get(1).findViewById(R.id.week_today1);
        temperature4=views.get(1).findViewById(R.id.temperature1);
        climate4=views.get(1).findViewById(R.id.climate1);
        weather_img4 = views.get(1).findViewById(R.id.weather_img1);
        wind4 = views.get(1).findViewById(R.id.wind1);
        //
        week_today5=views.get(1).findViewById(R.id.week_today2);
        temperature5=views.get(1).findViewById(R.id.temperature2);
        climate5=views.get(1).findViewById(R.id.climate2);
        weather_img5 = views.get(1).findViewById(R.id.weather_img2);
        wind5=views.get(1).findViewById(R.id.wind2);
    }

    @Override
    //单击事件
    public void onClick(View view) {
        //进入选择城市页面
        if (view.getId() == R.id.title_city_manager){
            Intent i  = new Intent(this, SelectCity.class);
            //startActivity(i);
            startActivityForResult(i,1);

        }
        //刷新
        if (view.getId() == R.id.title_update_btn) {
            //从sharedpreferences中读取城市id
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
        if(view.getId()==R.id.title_location){
            LocationUtil locationUtil = new LocationUtil(MainActivity.this);
            String city = locationUtil.getCurrentLocation();
            if(city == null)
            {
                Log.d("myWeather", "定位失败");
                return;
            }
            cn.edu.pku.wangluyue.app.MyApplication app =(cn.edu.pku.wangluyue.app.MyApplication) getApplication();
            List<City> cityentitles = app.getCityList();

            for(City Cities : cityentitles){
                if (Cities.getCity().equals(city))
                {
                    String citycode =Cities.getNumber();
                    queryWeatherCode(citycode);
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode = data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为" + newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else{
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * * @param cityCode
     */
    //查询该id城市网络数据
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {

                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
//                    System.out.println("我的线程：正在执行！" );
                    InputStream in = con.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    Log.d("myWeather", address);
                    while ((str = reader.readLine()) != null) {

                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        //传递更新的信息到UI
                        Log.d("myWeather", todayWeather.toString());
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    //分析XMl文件
    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为文档标签开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                todayWeather.setWind1(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind2(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind3(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind4(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind5(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 5) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                todayWeather.setWeek_today1(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_today2(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_today3(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_today4(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_today5(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWeek_today(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                todayWeather.setTemperatureH1(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                todayWeather.setTemperatureL1(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureH2(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL2(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureH3(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL3(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureH4(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL4(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureH5(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL5(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("high_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureH(xmlPullParser.getText().substring(2).trim());
                            } else if (xmlPullParser.getName().equals("low_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL(xmlPullParser.getText().substring(2).trim());
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                todayWeather.setClimate1(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 1){
                                eventType = xmlPullParser.next();
                                todayWeather.setClimate2(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setClimate3(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 3){
                                eventType = xmlPullParser.next();
                                todayWeather.setClimate4(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type") && typeCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setClimate5(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setClimate(xmlPullParser.getText());
                            }
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }
    void updateTodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:" + todayWeather.getFengli());
        //天气图标更新
        if(todayWeather.getType().equals("多云")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if(todayWeather.getType().equals("暴雪")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if(todayWeather.getType().equals("大暴雨")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if(todayWeather.getType().equals("大雪")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if(todayWeather.getType().equals("大雨")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if(todayWeather.getType().equals("雷阵雨")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if(todayWeather.getType().equals("雷阵雨冰雹")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if(todayWeather.getType().equals("晴")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        if(todayWeather.getType().equals("特大暴雨")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getType().equals("雾")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        if(todayWeather.getType().equals("小雪")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if(todayWeather.getType().equals("小雨")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getType().equals("阴")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        if(todayWeather.getType().equals("雨夹雪")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if(todayWeather.getType().equals("阵雪")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if(todayWeather.getType().equals("阵雨")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if(todayWeather.getType().equals("中雪")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if(todayWeather.getType().equals("中雨")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getType().equals("沙尘暴")) weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);

        //pm2.5更新
        //System.out.println(todayWeather.getPm25());
        //Log.d("myWeather", todayWeather.getPm25());
        if(todayWeather.getPm25() != null) {
            if (todayWeather.getQuality().equals("优"))
                pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
            if (todayWeather.getQuality().equals("良"))
                pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
            if (todayWeather.getQuality().equals("轻度污染"))
                pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
            if (todayWeather.getQuality().equals("中度污染"))
                pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
            if (todayWeather.getQuality().equals("重度污染"))
                pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
            if (todayWeather.getQuality().equals("严重污染"))
                pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }
        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();

        wind.setText(todayWeather.getWind());
        wind1.setText(todayWeather.getWind1());
        wind2.setText(todayWeather.getWind2());
        wind3.setText(todayWeather.getWind3());
        wind4.setText(todayWeather.getWind4());
        wind5.setText(todayWeather.getWind5());
        week_today.setText(todayWeather.getWeek_today());
       // Log.d("myWeather",todayWeather.getWeek_today());
        week_today1.setText(todayWeather.getWeek_today1());
        week_today2.setText(todayWeather.getWeek_today2());
        week_today3.setText(todayWeather.getWeek_today3());
        week_today4.setText(todayWeather.getWeek_today4());
        week_today5.setText(todayWeather.getWeek_today5());
        climate.setText(todayWeather.getClimate());
        climate1.setText(todayWeather.getClimate1());
        climate2.setText(todayWeather.getClimate2());
        climate3.setText(todayWeather.getClimate3());
        climate4.setText(todayWeather.getClimate4());
        climate5.setText(todayWeather.getClimate5());
        temperature.setText(todayWeather.getTemperatureH() + "~" + todayWeather.getTemperatureL());
        temperature1.setText(todayWeather.getTemperatureH1() + "~" + todayWeather.getTemperatureL1());
        temperature2.setText(todayWeather.getTemperatureH2() + "~" + todayWeather.getTemperatureL2());
        temperature3.setText(todayWeather.getTemperatureH3() + "~" + todayWeather.getTemperatureL3());
        temperature4.setText(todayWeather.getTemperatureH4() + "~" + todayWeather.getTemperatureL4());
        temperature5.setText(todayWeather.getTemperatureH5() + "~" + todayWeather.getTemperatureL5());


        if(todayWeather.getClimate().equals("多云")) weather_img.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if(todayWeather.getClimate().equals("暴雪")) weather_img.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if(todayWeather.getClimate().equals("大暴雨")) weather_img.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if(todayWeather.getClimate().equals("大雪")) weather_img.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if(todayWeather.getClimate().equals("大雨")) weather_img.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if(todayWeather.getClimate().equals("雷阵雨")) weather_img.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if(todayWeather.getClimate().equals("雷阵雨冰雹")) weather_img.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if(todayWeather.getClimate().equals("晴")) weather_img.setImageResource(R.drawable.biz_plugin_weather_qing);
        if(todayWeather.getClimate().equals("特大暴雨")) weather_img.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getClimate().equals("雾")) weather_img.setImageResource(R.drawable.biz_plugin_weather_wu);
        if(todayWeather.getClimate().equals("小雪")) weather_img.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if(todayWeather.getClimate().equals("小雨")) weather_img.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getClimate().equals("阴")) weather_img.setImageResource(R.drawable.biz_plugin_weather_yin);
        if(todayWeather.getClimate().equals("雨夹雪")) weather_img.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if(todayWeather.getClimate().equals("阵雪")) weather_img.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if(todayWeather.getClimate().equals("阵雨")) weather_img.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if(todayWeather.getClimate().equals("中雪")) weather_img.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if(todayWeather.getClimate().equals("中雨")) weather_img.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getClimate().equals("沙尘暴")) weather_img.setImageResource(R.drawable.biz_plugin_weather_shachenbao);

        if(todayWeather.getClimate1().equals("多云")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if(todayWeather.getClimate1().equals("暴雪")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if(todayWeather.getClimate1().equals("大暴雨")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if(todayWeather.getClimate1().equals("大雪")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if(todayWeather.getClimate1().equals("大雨")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if(todayWeather.getClimate1().equals("雷阵雨")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if(todayWeather.getClimate1().equals("雷阵雨冰雹")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if(todayWeather.getClimate1().equals("晴")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_qing);
        if(todayWeather.getClimate1().equals("特大暴雨")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getClimate1().equals("雾")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_wu);
        if(todayWeather.getClimate1().equals("小雪")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if(todayWeather.getClimate1().equals("小雨")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getClimate1().equals("阴")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_yin);
        if(todayWeather.getClimate1().equals("雨夹雪")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if(todayWeather.getClimate1().equals("阵雪")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if(todayWeather.getClimate1().equals("阵雨")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if(todayWeather.getClimate1().equals("中雪")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if(todayWeather.getClimate1().equals("中雨")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getClimate1().equals("沙尘暴")) weather_img1.setImageResource(R.drawable.biz_plugin_weather_shachenbao);

        if(todayWeather.getClimate2().equals("多云")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if(todayWeather.getClimate2().equals("暴雪")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if(todayWeather.getClimate2().equals("大暴雨")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if(todayWeather.getClimate2().equals("大雪")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if(todayWeather.getClimate2().equals("大雨")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if(todayWeather.getClimate2().equals("雷阵雨")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if(todayWeather.getClimate2().equals("雷阵雨冰雹")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if(todayWeather.getClimate2().equals("晴")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_qing);
        if(todayWeather.getClimate2().equals("特大暴雨")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getClimate2().equals("雾")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_wu);
        if(todayWeather.getClimate2().equals("小雪")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if(todayWeather.getClimate2().equals("小雨")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getClimate2().equals("阴")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_yin);
        if(todayWeather.getClimate2().equals("雨夹雪")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if(todayWeather.getClimate2().equals("阵雪")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if(todayWeather.getClimate2().equals("阵雨")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if(todayWeather.getClimate2().equals("中雪")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if(todayWeather.getClimate2().equals("中雨")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getClimate2().equals("沙尘暴")) weather_img2.setImageResource(R.drawable.biz_plugin_weather_shachenbao);

        if(todayWeather.getClimate3().equals("多云")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if(todayWeather.getClimate3().equals("暴雪")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if(todayWeather.getClimate3().equals("大暴雨")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if(todayWeather.getClimate3().equals("大雪")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if(todayWeather.getClimate3().equals("大雨")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if(todayWeather.getClimate3().equals("雷阵雨")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if(todayWeather.getClimate3().equals("雷阵雨冰雹")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if(todayWeather.getClimate3().equals("晴")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_qing);
        if(todayWeather.getClimate3().equals("特大暴雨")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getClimate3().equals("雾")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_wu);
        if(todayWeather.getClimate3().equals("小雪")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if(todayWeather.getClimate3().equals("小雨")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getClimate3().equals("阴")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_yin);
        if(todayWeather.getClimate3().equals("雨夹雪")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if(todayWeather.getClimate3().equals("阵雪")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if(todayWeather.getClimate3().equals("阵雨")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if(todayWeather.getClimate3().equals("中雪")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if(todayWeather.getClimate3().equals("中雨")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getClimate3().equals("沙尘暴")) weather_img3.setImageResource(R.drawable.biz_plugin_weather_shachenbao);

        if(todayWeather.getClimate4().equals("多云")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if(todayWeather.getClimate4().equals("暴雪")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if(todayWeather.getClimate4().equals("大暴雨")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if(todayWeather.getClimate4().equals("大雪")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if(todayWeather.getClimate4().equals("大雨")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if(todayWeather.getClimate4().equals("雷阵雨")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if(todayWeather.getClimate4().equals("雷阵雨冰雹")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if(todayWeather.getClimate4().equals("晴")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_qing);
        if(todayWeather.getClimate4().equals("特大暴雨")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getClimate4().equals("雾")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_wu);
        if(todayWeather.getClimate4().equals("小雪")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if(todayWeather.getClimate4().equals("小雨")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getClimate4().equals("阴")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_yin);
        if(todayWeather.getClimate4().equals("雨夹雪")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if(todayWeather.getClimate4().equals("阵雪")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if(todayWeather.getClimate4().equals("阵雨")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if(todayWeather.getClimate4().equals("中雪")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if(todayWeather.getClimate4().equals("中雨")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getClimate4().equals("沙尘暴")) weather_img4.setImageResource(R.drawable.biz_plugin_weather_shachenbao);

        if(todayWeather.getClimate5().equals("多云")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if(todayWeather.getClimate5().equals("暴雪")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if(todayWeather.getClimate5().equals("大暴雨")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if(todayWeather.getClimate5().equals("大雪")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if(todayWeather.getClimate5().equals("大雨")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if(todayWeather.getClimate5().equals("雷阵雨")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if(todayWeather.getClimate5().equals("雷阵雨冰雹")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if(todayWeather.getClimate5().equals("晴")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_qing);
        if(todayWeather.getClimate5().equals("特大暴雨")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if(todayWeather.getClimate5().equals("雾")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_wu);
        if(todayWeather.getClimate5().equals("小雪")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if(todayWeather.getClimate5().equals("小雨")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if(todayWeather.getClimate5().equals("阴")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_yin);
        if(todayWeather.getClimate5().equals("雨夹雪")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if(todayWeather.getClimate5().equals("阵雪")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if(todayWeather.getClimate5().equals("阵雨")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if(todayWeather.getClimate5().equals("中雪")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        if(todayWeather.getClimate5().equals("中雨")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if(todayWeather.getClimate5().equals("沙尘暴")) weather_img5.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
    }
}

