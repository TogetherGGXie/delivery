package com.delivery.main.common.persistence.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.delivery.main.common.persistence.template.modal.User;
import com.baomidou.mybatisplus.service.IService;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
public interface UserService extends IService<User> {

    Page<HashMap<String, Object>> getUserList(Page<HashMap<String, Object>> page);
}
