package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.UserMapper;
import com.delivery.main.common.persistence.service.UserService;
import com.delivery.main.common.persistence.template.modal.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public Page<HashMap<String, Object>> getUserList(Page<HashMap<String, Object>> pager) {
        Page<HashMap<String, Object>> page = new Page<>(pager.getCurrent(),pager.getSize());
        return page.setRecords(userMapper.getUserList(page));
    }
}
