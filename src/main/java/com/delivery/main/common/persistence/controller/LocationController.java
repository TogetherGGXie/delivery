package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.LocationService;
import com.delivery.main.common.persistence.template.modal.Location;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

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
    @RequestMapping(value = "suggestion" ,method = RequestMethod.POST)
    public Result findAddressByKeyWord(@RequestParam(value = "keyword" ,required = false) String keyWord, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"登录状态失效");
        }else{
            System.out.println("测试------------------");
            List<Location> location = locationService.selectList(new EntityWrapper<Location>().like("title", keyWord));
//            List<HashMap<String,String>> newLocation = new ArrayList<HashMap<String,String>>();
//            HashMap<String,String> hashMap = new HashMap<>();
//            for(Location loc : location){
//                String title = loc.getTitle();
//                String address = loc.getAddress();
//                hashMap.put("title",title);
//                hashMap.put("address",address);
//                newLocation.add(hashMap);
//            }
            return new Result(200,"获取位置信息成功",location);
        }
    }

    @ApiOperation("根据lat，lng获取详情位置")
    @ResponseBody
    @RequestMapping(value = "detailLocation" ,method = RequestMethod.POST)
    public String findAddressByLat(@RequestParam(value = "lat" ,required = false) String lat,
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
        //城市切割
        String[] strr = str.split("\",\"city\":\"");
        if (strr.length < 2 && strr.length == 1) {
            //直辖市
            String[] sr = str.split("\"province\":\"");
            String[] srr = sr[1].split("\",\"city");
            return srr[0];
        }
        //非直辖市
        String[] strrr = strr[1].split("\",\"citycode\":");
        return strrr[0];
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

