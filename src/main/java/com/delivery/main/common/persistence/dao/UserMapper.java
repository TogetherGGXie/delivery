package com.delivery.main.common.persistence.dao;

import com.delivery.main.common.persistence.template.modal.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}