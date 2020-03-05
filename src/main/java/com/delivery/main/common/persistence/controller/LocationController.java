package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.LocationService;
import com.delivery.main.common.persistence.template.modal.Location;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @ApiOperation("关键字搜索地方")
    @ResponseBody
    @RequestMapping(value = "suggestion" ,method = RequestMethod.GET)
    public Result findAddressByKeyWord(@RequestParam(value = "keyword" ,required = false) String keyWord, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"登录状态失效");
        }else{
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
    @RequestMapping(value = "detailLocation" ,method = RequestMethod.GET)
    public Result findAddressByLat(@RequestParam(value = "lat" ,required = false) String lat,
                                   @RequestParam(value = "lng" ,required = false) String lng,
                                   HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"登录状态失效");
        }else{
        Location location = locationService.selectOne(new EntityWrapper<Location>().eq("lat", lat).eq("lng", lng));
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
}

