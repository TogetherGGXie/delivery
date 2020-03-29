package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.PindanService;
import com.delivery.main.common.persistence.template.modal.Pindan;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author null123
 * @since 2020-03-18
 */
@RestController
public class PindanController {
    @Autowired
    private PindanService pindanService;
    @ApiOperation("查询店铺拼单")
    @RequestMapping(value = "/assemble/allAssemble/{id}", method= RequestMethod.GET)
    @ResponseBody
    public Result queryOrders(@PathVariable Integer id ,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new Result(-1, "用户登录已失效");
        } else {
            Integer uId = user.getUserId();
            List<Pindan> pindanList = pindanService.selectList(new EntityWrapper<Pindan>().eq("restaurantId", id));
            List<HashMap<String,Object>> pinIdList = new LinkedList<>();
            for (Pindan i:pindanList){
                String pinId = i.getPinId();
                String time = i.getTime();
                HashMap<String,Object> hashMap= new HashMap<>();
                hashMap.put("pinId",pinId);
                hashMap.put("time",time);
                pinIdList.add(hashMap);
            }
            HashSet hashSet = new HashSet(pinIdList);
            pinIdList.clear();
            pinIdList.addAll(hashSet);
            List<HashMap<String,Object>> resPinList = new LinkedList<>();
            for(HashMap<String,Object> i:pinIdList){
                String pinId = String.valueOf(i.get("pinId"));
                String time = String.valueOf(i.get("time"));
                List<Pindan> groupPinList = pindanService.selectList(new EntityWrapper<Pindan>().eq("pinId", pinId));
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("time",time);
                hashMap.put("pinId",pinId);
                hashMap.put("groupPinList",groupPinList);
                resPinList.add(hashMap);
            }
            HashMap<String,List> pindanMap = new HashMap<>();
            pindanMap.put("resPinList",resPinList);
            List<HashMap<String,List>> pindans = new LinkedList<>();
            HashMap userGroup = getUserGroup();
            HashMap UserBuildAssembleList = getUserBuildAssemble(uId);
            pindans.add(pindanMap);
            pindans.add(userGroup);
            pindans.add(UserBuildAssembleList);
            return new Result(200,"获取列表成功",pindans);
        }
    }

    public HashMap getUserGroup(){
        HashMap<String,List> hashMap = new HashMap<>();
        List<Pindan> list = new LinkedList<>();
        hashMap.put("resPinList",list);
        return hashMap;
    }

    public HashMap getUserBuildAssemble(Integer uId){
        HashMap<String,List> hashMap = new HashMap<>();
        List<Pindan> listByPinId = pindanService.selectList(new EntityWrapper<Pindan>().like("pinId", String.valueOf(uId)));
        hashMap.put("resPinList",listByPinId);
        Object object = new Object();

        return hashMap;
    }


    @ApiOperation("退出拼单")
    @RequestMapping(value = "/assemble/deleteAssemble", method= RequestMethod.GET)
    @ResponseBody
    public Result deleteAssemble(@RequestParam Integer id ,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new Result(-1, "用户登录已失效");
        } else {
            Pindan pindan = pindanService.selectOne(new EntityWrapper<Pindan>().eq("id", id));
            boolean deleteAssemble = pindanService.delete(new EntityWrapper<Pindan>().eq("id", id));
            if(deleteAssemble){
                String pinId = pindan.getPinId();
                if(pindan.getGroupStatus().equals("1") ){
                    List<Pindan> pindanList = pindanService.selectList(new EntityWrapper<Pindan>().eq("pinId", pinId));
                    for (Pindan i : pindanList) {
                        i.setGroupStatus("0");
                    }
                    pindanService.updateBatchById(pindanList);
                }
                return new Result(200,"退出成功");
            }else {
                return new Result(-1,"退出失败");
            }

        }
    }



    @ApiOperation("用户发起一个新的拼单组")
    @RequestMapping(value = "/assemble/buildAssemble", method = RequestMethod.POST)
    @ResponseBody
    public Result buildAssemble(@RequestParam Integer restaurantId,
                              @RequestParam String time,
                              @RequestParam String pinId,
                              @RequestParam String name,
                              @RequestParam String user_icon,
                              HttpServletRequest request) throws ParseException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new Result(-1, "用户登录已失效");
        } else {
            Integer userId = user.getUserId();
            System.out.println(time);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String pinIdTime = df.format(new Date());
            String newTime = pinIdTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
            Pindan pd = new Pindan();
            String pinIdNew = String.valueOf(restaurantId) +String.valueOf(userId) + newTime;
            pd.setRestaurantId(restaurantId);
            pd.setPinId(pinIdNew);
            pd.setStatus("0");
            pd.setGroupStatus("0");
            pd.setTime(time);
            pd.setUserIcon(user_icon);
            pd.setUsername(name);
            pd.setUserId(userId);
            boolean insertPd = pindanService.insert(pd);
            if (insertPd) {
                Pindan pinId1 = pindanService.selectOne(new EntityWrapper<Pindan>().eq("pinId", pinIdNew));
                Integer id = pinId1.getId();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", String.valueOf(id));
                hashMap.put("pinId", pinIdNew);
                return new Result(200, "发起拼单成功", hashMap);
            } else {
                return new Result(-1, "发起拼单失败");
            }
        }
    }

    @ApiOperation("加入一个已经存在的拼单组")
    @RequestMapping(value = "/assemble/joinAssemble", method = RequestMethod.POST)
    @ResponseBody
    public Result joinAssemble(@RequestParam Integer restaurantId,
                              @RequestParam String pinId,
                              @RequestParam String time,
                              @RequestParam HashMap<String, String> userInfo,
                              HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new Result(-1, "用户登录已失效");
        } else {
            String userName = userInfo.get("name");
            String userIcon = userInfo.get("user_icon");
            Pindan pd = new Pindan();
            pd.setRestaurantId(restaurantId);
            pd.setUserIcon(userIcon);
            pd.setUsername(userName);
            pd.setPinId(pinId);
            pd.setTime(time);
            pindanService.insert(pd);
            Integer id = pd.getId();
            List<Pindan> pindanList = pindanService.selectList(new EntityWrapper<Pindan>().eq("pinId", pinId));
            if (pindanList.size() == 6) {
                for (Pindan i : pindanList) {
                    i.setGroupStatus("1");
                }
                pindanService.updateBatchById(pindanList);
            }

            return new Result(200, "加入一个拼单组成功", id);
        }
    }
}

