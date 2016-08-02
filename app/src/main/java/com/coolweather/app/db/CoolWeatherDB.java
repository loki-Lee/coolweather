package com.coolweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2 0002.
 */
public class CoolWeatherDB {
    public static final String DB_NAME="cool_weather";
    public static final int DB_VERSION=1;
    private  static CoolWeatherDB mCoolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context,DB_NAME,null,DB_VERSION);
        db=dbHelper.getReadableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance (Context context){
        if(mCoolWeatherDB==null){
            mCoolWeatherDB=new CoolWeatherDB(context);
        }
        return mCoolWeatherDB;
    }
    public void saveProvince(Province province){
        if(province!=null){
            ContentValues values=new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     * @return
     */
     public List<Province> loadProvinces(){
         List<Province> provinces=new ArrayList<Province>();
        Cursor cursor= db.query("Province",null,null,null,null,null,null);
         if(cursor.moveToFirst()) {
             do {
             Province province = new Province();
             province.setId(cursor.getInt(cursor.getColumnIndex("id")));
             province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
             province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
             provinces.add(province);
            }while ((cursor.moveToNext()));
         }
         if (cursor!=null){
             cursor.close();
         }
         return provinces;
     }

    /**
     * 将City实例存储到数据库
     * @param city
     */
    public void saveCity(City city){
        if(city!=null){
            ContentValues values=new ContentValues();
            values.put("city_name",city.getCityName());
            values.put(("city_code"),city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City",null,values);
        }
    }

    /**
     * 从数据库读取某省的所有城市的信息
     * @param provinceId
     * @return
     */

    public List<City> loadCities(int provinceId){
        List<City> cities=new ArrayList<City>();
        Cursor cursor=db.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city=new City();
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                cities.add(city);
            }while (cursor.moveToNext());
        }
        if (cursor!=null){
            cursor.close();
        }
        return cities;
    }

    /**
     * 将County实例存储到数据库
     * @param county
     */
    public void saveCounty(County county){
        if(county!=null){
            ContentValues values=new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County",null,values);
        }
    }

    /**
     * 从数据库读取某市下所有城镇的信息
     * @param cityId
     * @return
     */

    public List<County> loadCounties(int cityId){
        List<County> counties=new ArrayList<County>();
        Cursor cursor=db.query("County",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                County county=new County();
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                counties.add(county);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return  counties;
    }

}
