package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.PindanMapper;
import com.delivery.main.common.persistence.service.PindanService;
import com.delivery.main.common.persistence.template.modal.Pindan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2020-03-18
 */
@Service
public class PindanServiceImpl extends ServiceImpl<PindanMapper, Pindan> implements PindanService {
    @Resource
    private PindanMapper pindanMapper;

    @Override
    public Integer insertPindan(Pindan pindan){
      return pindanMapper.insertPindan(pindan);
    }
}
