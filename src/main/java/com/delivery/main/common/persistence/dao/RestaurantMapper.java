package com.delivery.main.common.persistence.dao;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.delivery.main.common.persistence.template.modal.Restaurant;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
public interface RestaurantMapper extends BaseMapper<Restaurant> {

    @Select("select * from restaurant order by create_time")
    List<HashMap<String, Object>> getRestaurantList(Pagination pagination);

}
