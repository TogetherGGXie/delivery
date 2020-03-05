package com.delivery.main.common.persistence.service.serviceImpl;

import com.delivery.main.common.persistence.template.modal.Location;
import com.delivery.main.common.persistence.dao.LocationMapper;
import com.delivery.main.common.persistence.service.LocationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2020-03-05
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements LocationService {

}
