package com.delivery.main.common.persistence.dao;

import com.delivery.main.common.persistence.template.modal.Pindan;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author null123
 * @since 2020-03-18
 */
@Mapper
public interface PindanMapper extends BaseMapper<Pindan> {
     Integer insertPindan(Pindan pindan);
}
