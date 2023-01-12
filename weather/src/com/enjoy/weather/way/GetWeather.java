package com.enjoy.weather.way;

import com.enjoy.weather.weatherConstruction.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class GetWeather {
    public static String id;

    public GetWeather(String thisId){
        id = thisId;
    }

    public Data getData() throws IOException, ParseException {
        //构建客户端
        CloseableHttpClient client = HttpClients.createDefault();
        //构建一个请求对象
        String url = "https://devapi.qweather.com/v7/weather/3d?key=88822706f0da4625b35725bf95e225be&location=" + id;
        HttpGet doGet = new HttpGet(url);
        //发送请求，服务器会响应我们一个json字符串
        CloseableHttpResponse response =client.execute(doGet);
        //获取响应的实体对象
        String jsonStr = EntityUtils.toString(response.getEntity());

        //System.out.println(jsonStr);
        //将json转为data类型
        ObjectMapper mapper = new ObjectMapper();
        Data data = mapper.readValue(jsonStr, Data.class);

        return data;
    }
}
