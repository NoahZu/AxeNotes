package noahzu.github.io.axenotes.entity;

import com.amap.api.location.AMapLocation;

/**
 * Created by Administrator on 2016/3/8.
 */
public class City {

    String country;
    String province;
    String city;
    String district;
    String street;
    String streetNum;
    String cityCode;


    public City(String country, String province, String city, String district, String street, String streetNum, String cityCode) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
        this.streetNum = streetNum;
        this.cityCode = cityCode;
    }

    public City(AMapLocation amapLocation){
        this.country = amapLocation.getCountry();//国家信息
        this.province = amapLocation.getProvince();//省信息
        this.city = amapLocation.getCity();//城市信息
        this.district = amapLocation.getDistrict();//城区信息
        this.street = amapLocation.getStreet();//街道信息
        this.streetNum = amapLocation.getStreetNum();//街道门牌号信息
        this.cityCode = amapLocation.getCityCode();//城市编码
    }
    public String getSimpleLocation(){
        return province+"省"+city+"市"+district+street+"街道";
    }

    public String getDetailLocation(){
        return "";
    }
}
