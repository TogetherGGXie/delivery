package com.delivery.main.common.persistence.controller;


import com.delivery.main.common.persistence.service.LocationService;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2020-03-05
 */
@RestController
public class LocationController {
    @Autowired
    private LocationService locationService;

    private static final String key ="7f4ffae4074e8b8e4d147190527a4b72";

    @ApiOperation("关键字搜索地方")
    @ResponseBody
    @RequestMapping(value = "suggestion" ,method = RequestMethod.GET)
    public Result findAddressByKeyWord(@RequestParam(value = "keyword" ,required = false) String keyWord, HttpServletRequest request){
            StringBuffer s = new StringBuffer();
            s.append("key=" + key + "&keywords=");
            if (keyWord.contains(" ")) {
                String[] str = keyWord.split(" ");
                for (int i = 0; i < str.length; i++) {
                    if (i == 0) {
                        s.append(str[i]);
                    } else {
                        s.append("+" + str[i]);
                    }
                }
            } else {
                s.append(keyWord);
            }
//            s.append("&city=" + city);
            s.append("&offset=10&page=1");
            String around = sendPost("http://restapi.amap.com/v3/place/text", s.toString());
//            logger.info(around);
        System.out.println(around);
        JSONObject obj = new JSONObject().fromObject(around);
        JSONArray jsonArray = (JSONArray) obj.get("pois");
        System.out.println(jsonArray);

        List<HashMap<String,Object>> list = new LinkedList<>();
        for(Object i : jsonArray){
            HashMap<String,Object> hashMap = new HashMap<String,Object>();
            System.out.println(i);
            Map entry = (Map)i;
            System.out.println(entry+"========");
            Object title = entry.get("name");
            Object address = entry.get("address");
            hashMap.put("title",title);
            hashMap.put("address",address);
            list.add(hashMap);
        }

            return new Result(200,"获取成功",list);

    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
//            logger.info("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    @ApiOperation("根据lat，lng获取详情位置")
    @ResponseBody
    @RequestMapping(value = "detailLocation" ,method = RequestMethod.GET)
    public Result findAddressByLat(@RequestParam(value = "lat" ,required = false) String lat,
                                   @RequestParam(value = "lng" ,required = false) String lng,
                                   HttpServletRequest request) throws IOException {
        StringBuilder resultData = new StringBuilder();
        StringBuilder https = new StringBuilder("http://restapi.amap.com/v3/geocode/regeo?key=");
//经纬度地址
        StringBuilder localhost = new StringBuilder("&location="+lng+","+lat);
        StringBuilder httpsTail = new StringBuilder("&poitype=&radius=&extensions=base&batch=true");
        String url = https.append(key).append(localhost).append(httpsTail).toString();
    //拼接出来的地址
    //System.out.println(https1.append(key).append(localhost1).append(httpsTail).toString());
    // String url ="http://restapi.amap.com/v3/geocode/regeo?key=自己申请的key&location=116.310003,39.991957&poitype=&radius=&extensions=base&batch=true&roadlevel=";
            URL myURL = null;
            URLConnection httpsConn = null;
            try {
                myURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStreamReader insr = null;
            BufferedReader br = null;
            try {
                httpsConn = myURL.openConnection();// 不使用代理
                if (httpsConn != null) {
                    insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                    br = new BufferedReader(insr);
                    String data = null;
                    while ((data = br.readLine()) != null) {
                        resultData.append(data);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (insr != null) {
                    insr.close();
                }
                if (br != null) {
                    br.close();
                }
            }
            if (resultData.toString().indexOf("regeocodes") == 0) {
                return null;
            }
            String str = JSONObject.fromObject(resultData.toString()).getString("regeocodes");
//            String formatted_address = String.valueOf(str.indexOf("formatted_address"));
//            return formatted_address;
//        System.out.println(str);
//
    //城市切割
//            String[] strr = str.split("\",\"city\":\"");
//            if (strr.length < 2 && strr.length == 1) {
//                //直辖市
//                String[] sr = str.split("\"province\":\"");
//                String[] srr = sr[1].split("\",\"city");
//                return srr[0];
//            }
    //非直辖市
//            String[] strrr = strr[1].split("\",\"citycode\":");
//            return strrr[0];
//
        JSONArray jsonArray = new JSONArray().fromObject(str);
        Object formatted_address = jsonArray.getJSONObject(0).get("formatted_address");
        System.out.println(formatted_address);
        String s = formatted_address.toString();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("address",formatted_address);
        return new Result(200,"获取位置成功",hashMap);

    }


    @ApiOperation("根据地址获取经纬度")
    @ResponseBody
    @RequestMapping(value = "detailLocations" ,method = RequestMethod.POST)
    public String getLngLat(@RequestParam(value = "address",required = false) String address) {
        StringBuffer json = new StringBuffer();
        try {
            URL u = new URL("http://restapi.amap.com/v3/geocode/geo?address="+address+"&output=JSON&key=7f4ffae4074e8b8e4d147190527a4b72");
            URLConnection yc = u.openConnection();
            //读取返回的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(),"UTF-8"));
            String inputline = null;
            while((inputline=in.readLine())!=null){
                json.append(inputline);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonStr=json.toString();
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);

        if(jsonObject.getJSONArray("geocodes").size()>0) {
            return jsonObject.getJSONArray("geocodes").getJSONObject(0).get("location").toString();
        } else {
            return null;
        }
    }
}

