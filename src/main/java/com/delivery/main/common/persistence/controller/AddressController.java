package com.delivery.main.common.persistence.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.AddressService;
import com.delivery.main.common.persistence.template.modal.Address;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @ApiOperation("添加地址")
    @RequestMapping(value = "admin/address", method= RequestMethod.POST)
    @ResponseBody
    public Result addAddress(@RequestParam String gender,
                             @RequestParam String name,
                             @RequestParam String phone,
                             @RequestParam HashMap<String, String> addressInfo,
                             HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"登录状态失效");
        }else{
            Address address = new Address();
            address.setPhone(phone);
            address.setUserId(user.getUserId());
            address.setName(name);
            address.setGender(gender.equals("female") ? 0 : 1);
            address.setProvince(addressInfo.get("province"));
            address.setCity(addressInfo.get("city"));
            address.setDistrict(addressInfo.get("district"));
            address.setDetail(addressInfo.get("detail"));
            address.setCreateTime(new Date());
            boolean insertAddress = addressService.insert(address);
            if(insertAddress){
                return new Result(200,"添加收货地址成功");
            }else {
                return new Result(-1,"添加地址失败，参数有误");
            }
        }


    }

    @ApiOperation("删除地址")
    @RequestMapping(value = "admin/deleteAddress",method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteAddress(@RequestParam("address_id") Integer address_id,HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"登录状态失效");
        }else {
            boolean deleteAddress = addressService.deleteById(address_id);
            if (deleteAddress) {
                return new Result(200, "删除收获地址成功");
            } else {
                return new Result(-1, "删除收获地址失败");
            }
        }
    }

    @ApiOperation("查询地址")
    @RequestMapping(value = "admin/all_address",method = RequestMethod.GET)
    @ResponseBody
    public Result queryAddress(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"登录状态失效");
        }else{
            Integer userId = user.getUserId();
            List<Address> listAddress = addressService.selectList(new EntityWrapper<Address>().eq("user_id", userId));
            return new Result(200,"查询成功",listAddress);
        }

    }

}

