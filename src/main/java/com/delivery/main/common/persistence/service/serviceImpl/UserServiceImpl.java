package com.delivery.main.common.persistence.service.serviceImpl;

import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.common.persistence.dao.UserMapper;
import com.delivery.main.common.persistence.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
