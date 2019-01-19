package cn.edu.pku.wangluyue.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtil {
    private LocationManager locationManager;
    private Context context;
    public LocationUtil(Context context)
    {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
    }
    //获取当前城市
    public String getCurrentLocation()
    {
        //Log.d("myWeather", "权限不够");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "权限不够", Toast.LENGTH_LONG).show();
            return null;
        }

        Location currentLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        if(currentLocation == null)
        {
            Toast.makeText(context,"无法获取当前位置", Toast.LENGTH_SHORT).show();
            return null;
        }
        //根据经纬度获取城市名称
        return getCityByLocation(currentLocation);
    }

    private String getCityByLocation(Location location)
    {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addresses.size()>0)//找到城市信息
            {
                Address address = addresses.get(0);
                String city = address.getLocality();
                city = city.replace("([市县乡州村])","");
                Toast.makeText(context,String.format("当前位置 %s",city),Toast.LENGTH_SHORT).show();
                return city;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
