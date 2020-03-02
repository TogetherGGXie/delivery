package com.delivery.main.common.persistence.service.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.delivery.main.common.persistence.dao.CommentMapper;
import com.delivery.main.common.persistence.service.CommentService;
import com.delivery.main.common.persistence.template.modal.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private  CommentMapper commentMapper;

    @Override
    public List<HashMap<String, Object>> getComments(Integer restaurantId) {
        return commentMapper.getComments(restaurantId);
    }
}
