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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
            List<String> pinIdList =new LinkedList<String>();
            for (Pindan i:pindanList){
                String pinId = i.getPinId();
                pinIdList.add(pinId);
            }
            HashSet hashSet = new HashSet(pinIdList);
            pindanList.clear();
            pindanList.addAll(hashSet);
            List<HashMap<String,Object>> resPinList = new LinkedList<>();
            for(Pindan i:pindanList){
                String pinId = i.getPinId();
                List<Pindan> groupPinList = pindanService.selectList(new EntityWrapper<Pindan>().eq("pinId", pinId));
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("time","time");
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
        return hashMap;
    }

    public HashMap getUserBuildAssemble(Integer uId){
        HashMap<String,List> hashMap = new HashMap<>();
        List<Pindan> listByPinId = pindanService.selectList(new EntityWrapper<Pindan>().like("pinId", String.valueOf(uId)));
        hashMap.put("UserBuildAssembleList",listByPinId);
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
                              @RequestParam HashMap<String, String> buildPinlist,
                              HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new Result(-1, "用户登录已失效");
        } else {
            Integer userId = user.getUserId();
            String time = buildPinlist.get("time");
            String userName = buildPinlist.get("name");
            String userIcon = buildPinlist.get("user_icon");
            StringBuilder pinId = new StringBuilder();
            pinId.append(restaurantId).append(userId).append(time);
            Pindan pd = new Pindan();
            String pinIdNew = pinId.toString();
            pd.setRestaurantId(restaurantId);
            pd.setPinId(pinIdNew);
            pd.setUserIcon(userIcon);
            pd.setUsername(userName);
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

