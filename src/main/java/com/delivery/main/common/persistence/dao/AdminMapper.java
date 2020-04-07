package com.delivery.main.common.persistence.dao;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.delivery.main.common.persistence.template.modal.Admin;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {


    @Select("SELECT \n" +
            "\tadmin.admin_id, \n" +
            "\tadmin.admin_name, \n" +
            "\tadmin.password,\n" +
            "\tadmin.create_time,\n" +
            "\tadmin.admin_type,\n" +
            "\tadmin.city,\n" +
            "\tadmin.restaurant_id,\n" +
            "\trestaurant.name,\n" +
            "\tadmin.avatar\n" +
            "FROM\n" +
            "\t`admin` left join `restaurant`\n" +
            "\t on admin.restaurant_id = restaurant.restaurant_id\n" +
            "\t where admin.status = 1 and admin.admin_type = 1\n" +
            "\tORDER BY\n" +
            "\tadmin.create_time")
    @Results(id="AdminListResultMap",value={
            @Result(property = "adminId", column = "admin_id"),
            @Result(property = "adminName", column = "admin_name"),
            @Result(property = "password", column = "password"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "restaurantId", column = "restaurant_id"),
            @Result(property = "restaurantName", column = "name"),
            @Result(property = "avatar", column = "avatar"),
    })
    List<HashMap<String, Object>> getAdminList(Pagination pagination);
}
