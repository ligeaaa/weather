package com.enjoy.weather.way;

import com.enjoy.weather.locationConstruction.LData;
import com.enjoy.weather.locationConstruction.Location;
import com.enjoy.weather.weatherConstruction.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GetLocation {
    public static String location;

    public GetLocation(String name) throws UnsupportedEncodingException {
        String encodedText = URLEncoder.encode(name, "UTF-8");
        location = encodedText;
    }

    public LData getiD() throws IOException, ParseException {
        //构建客户端
        CloseableHttpClient client = HttpClients.createDefault();
        //构建一个请求对象
        String url = "https://geoapi.qweather.com/v2/city/lookup?key=88822706f0da4625b35725bf95e225be&location=" + location;
//        System.out.println(url+"__________");
        HttpGet doGet = new HttpGet(url);
        //发送请求，服务器会响应我们一个json字符串
        CloseableHttpResponse response =client.execute(doGet);
        //获取响应的实体对象
        String jsonStr = EntityUtils.toString(response.getEntity());
//        System.out.println(jsonStr);
        //System.out.println(jsonStr);
        //将json转为data类型
        ObjectMapper mapper = new ObjectMapper();
        LData ldata = mapper.readValue(jsonStr, LData.class);
        return ldata;
    }
}
