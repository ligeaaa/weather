package com.enjoy.weather.main;

import com.enjoy.weather.locationConstruction.Location;
import com.enjoy.weather.set.Parameters;
import com.enjoy.weather.way.GetLocation;
import com.enjoy.weather.weatherConstruction.Daily;
import com.enjoy.weather.weatherConstruction.Data;
import com.enjoy.weather.set.JdbcUtils;
import com.enjoy.weather.way.GetWeather;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Scanner;

public class WeatherS {

    public static void main(String[] args) throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        //初始化主键参数
        Parameters parameters = new Parameters(0,0,0,0);
        //插入三个初始城市
        upd("北京",parameters);
        upd("上海",parameters);
        upd("福州",parameters);

        int flag = sc.nextInt();
        while(flag != 0){
            System.out.println("输入0表示结束程序");
            System.out.println("如果想更新某地数据，请输入1");
            System.out.println("如果想查询某地三日天气，请输入2");
            System.out.println("自带北京、上海、福州的数据");
            if(flag == 1){
                System.out.println("请输入要更新的城市的中文名，不要有错别字哦");
                System.out.println("可以是任意城市");
                String name = sc.next();
                upd(name,parameters);
                System.out.println("更新成功！");
            }else if(flag == 2){
                System.out.println("请输入要查询的城市的中文名，不要有错别字哦");
                System.out.println("需要是更新过数据的城市哦，不然没有结果哦");
                String name = sc.next();
                sel(name);
                System.out.println("查询成功！");
            }else{
                System.out.println("请输入0或1或2哦");
            }
            flag = sc.nextInt();
        }
        System.out.println("结束！");
    }

    //传入name，name是城市的名字，即可将这个城市三天的数据存入数据库。parameters是主键使用到的参数
    public static Parameters upd(String name,Parameters parameters) throws IOException, ParseException {
        //获取id地址以及他的json，并将其转化为类
        GetLocation getLocation = new GetLocation(name);
        Location[] location = getLocation.getiD().getLocation();
        String id = location[0].getId();
        GetWeather getWeather = new GetWeather(id);
        Data data = getWeather.getData();

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;


        try {
            conn = JdbcUtils.getConnection();
            //检查是否已经存在该城市，若有则删除
            //麻了，不知道外键出了什么问题，也不报错也不怎么样，就是不起作用，只能用这种麻烦的方式写了，麻
            String delete0 = "DELETE FROM Daily WHERE did IN (SELECT did FROM DATAToDaily WHERE Times IN (SELECT Times FROM DATA WHERE name=?));";
            st = conn.prepareStatement(delete0);
            st.setString(1,name);
            st.executeUpdate();

            String delete1 = "DELETE FROM SOURCES WHERE sid IN (SELECT sid FROM DATAToSOURCES WHERE Times IN (SELECT Times FROM DATA WHERE name=?));";
            st = conn.prepareStatement(delete1);
            st.setString(1,name);
            st.executeUpdate();

            String delete2 = "DELETE FROM LICENSE WHERE lid IN (SELECT lid FROM DATAToLICENSE WHERE Times IN (SELECT Times FROM DATA WHERE name=?));";
            st = conn.prepareStatement(delete2);
            st.setString(1,name);
            st.executeUpdate();


            String delete3 = "DELETE FROM DATAToDaily WHERE Times IN (SELECT Times FROM DATA WHERE name=?);";
            st = conn.prepareStatement(delete3);
            st.setString(1,name);
            st.executeUpdate();

            String delete4 = "DELETE FROM DATAToSOURCES WHERE Times IN (SELECT Times FROM DATA WHERE name=?);";
            st = conn.prepareStatement(delete4);
            st.setString(1,name);
            st.executeUpdate();

            String delete5 = "DELETE FROM DATAToLICENSE WHERE Times IN (SELECT Times FROM DATA WHERE name=?);";
            st = conn.prepareStatement(delete5);
            st.setString(1,name);
            st.executeUpdate();

            String sql0 = "DELETE FROM data WHERE Name = ?";
            st = conn.prepareStatement(sql0);
            st.setString(1,name);
            st.executeUpdate();

            //插入Data表
            String sql1 = "insert into data(Times,Name,code,updateTime,pfxLink) values (?,?,?,?,?);";
            st = conn.prepareStatement(sql1);
            st.setInt(1,++parameters.times);
            st.setString(2,name);
            st.setString(3,data.getCode());
            st.setString(4,data.getUpdateTime());
            st.setString(5,data.getFxLink());
            st.executeUpdate();

            //插入DATAToDaily与Daily
            Daily[] daily = data.getDaily();
            for(int i = 0;i < daily.length;i++) {
                String sql2 = "insert into DATAToDaily(Times,did) values (?,?);";
                st = conn.prepareStatement(sql2);
                st.setInt(1,parameters.times);
                st.setInt(2,++parameters.did);
                st.executeUpdate();

                String sql3 = "insert into daily values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                st = conn.prepareStatement(sql3);
                st.setInt(1,parameters.did);
                st.setString(2,daily[i].getFxDate());
                st.setString(3,daily[i].getSunrise());
                st.setString(4,daily[i].getSunset());
                st.setString(5,daily[i].getMoonrise());
                st.setString(6,daily[i].getMoonset());
                st.setString(7,daily[i].getMoonPhase());
                st.setString(8,daily[i].getMoonPhaseIcon());
                st.setString(9,daily[i].getTempMax());
                st.setString(10,daily[i].getTempMin());
                st.setString(11,daily[i].getIconDay());
                st.setString(12,daily[i].getTextDay());
                st.setString(13,daily[i].getIconNight());
                st.setString(14,daily[i].getTextNight());
                st.setString(15,daily[i].getWind360Day());
                st.setString(16,daily[i].getWindDirDay());
                st.setString(17,daily[i].getWindScaleDay());
                st.setString(18,daily[i].getWindSpeedDay());
                st.setString(19,daily[i].getWind360Night());
                st.setString(20,daily[i].getWindDirNight());
                st.setString(21,daily[i].getWindScaleNight());
                st.setString(22,daily[i].getWindSpeedNight());
                st.setString(23,daily[i].getHumidity());
                st.setString(24,daily[i].getPrecip());
                st.setString(25,daily[i].getPressure());
                st.setString(26,daily[i].getVis());
                st.setString(27,daily[i].getCloud());
                st.setString(28,daily[i].getUvIndex());
                st.executeUpdate();
            }

            //输入DATAToSOURCES
            String[] sources = data.getRefer().getSources();
            for (int i = 0; i < sources.length; i++) {
                String sql4 = "insert into DATAToSOURCES values (?,?);";
                st = conn.prepareStatement(sql4);
                st.setInt(1,parameters.times);
                st.setInt(2,++parameters.sid);
                st.executeUpdate();

                String sql5 = "insert into SOURCES values (?,?);";
                st = conn.prepareStatement(sql5);
                st.setInt(1,parameters.sid);
                st.setString(2,sources[i]);
                st.executeUpdate();
            }

            //输入DATAToLICENSE
            String[] license = data.getRefer().getLicense();
            for (int i = 0; i < license.length; i++) {
                String sql6 = "insert into DATAToLICENSE values (?,?);";
                st = conn.prepareStatement(sql6);
                st.setInt(1,parameters.times);
                st.setInt(2,++parameters.lid);
                st.executeUpdate();

                String sql7 = "insert into LICENSE values (?,?);";
                st = conn.prepareStatement(sql7);
                st.setInt(1,parameters.lid);
                st.setString(2,license[i]);
                st.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(conn,st,rs);
        }

        return parameters;
    }

    public static void sel(String name) throws IOException, ParseException {
        GetLocation getLocation = new GetLocation(name);
        Location[] location = getLocation.getiD().getLocation();
        String id = location[0].getId();
        GetWeather getWeather = new GetWeather(id);
        Data data = getWeather.getData();

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();
            //检查是否已经存在该城市，若有则删除
            //麻了，不知道外键出了什么问题，也不报错也不怎么样，就是不起作用，只能用这种麻烦的方式写了，麻
            String sql = "        SELECT fxDate,tempMax,tempMin,textDay\n" +
                    "        FROM data,datatodaily,daily\n" +
                    "        WHERE data.Times = datatodaily.Times\n" +
                    "        and datatodaily.did = daily.did\n" +
                    "        and data.Name = ?";
            st = conn.prepareStatement(sql);
            st.setString(1,name);
            rs = st.executeQuery();
            while(rs.next()){
                System.out.println("fxDate:"+rs.getString("fxDate"));
                System.out.println("tempMax:"+rs.getString("tempMax"));
                System.out.println("tempMin:"+rs.getString("tempMin"));
                System.out.println("textDay:"+rs.getString("textDay"));
                System.out.println("-----------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(conn,st,rs);
        }




    }
}
