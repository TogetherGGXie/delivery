package com.delivery.main.common.persistence.service;

import com.delivery.main.common.persistence.template.modal.Comment;
import com.baomidou.mybatisplus.service.IService;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
public interface CommentService extends IService<Comment> {

    List<HashMap<String, Object>> getComments(Integer restaurantId);
}
