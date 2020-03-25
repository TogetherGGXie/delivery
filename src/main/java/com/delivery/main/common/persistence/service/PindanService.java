package com.delivery.main.common.persistence.service;

import com.delivery.main.common.persistence.template.modal.Pindan;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author null123
 * @since 2020-03-18
 */
public interface PindanService extends IService<Pindan> {
    Integer insertPindan(Pindan pindan);
}
