package com.delivery.main.common.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.delivery.main.common.persistence.template.modal.User;
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
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT \n" +
            "\tuser_id, \n" +
            "\tusername, \n" +
            "\tphone, \n" +
            "\tpassword,\n" +
            "\topen_id,\n" +
            "\tgender,\n" +
            "\tavatar,\n" +
            "\tcreate__time,\n" +
            "\tstatus,\n" +
            "FROM\n" +
            "\t`user`\n" +
            "\tORDER BY\n" +
            "\tcreate_time")
    @Results(id="UserListResultMap",value={
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "password", column = "password"),
            @Result(property = "openId", column = "open_id"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "create__time", column = "create__time"),
            @Result(property = "status", column = "status"),
    })
    List<HashMap<String, Object>> getUserList(Pagination pagination);
}
