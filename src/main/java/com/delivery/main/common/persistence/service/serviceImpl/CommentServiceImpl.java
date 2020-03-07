package com.delivery.main.common.persistence.service.serviceImpl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
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
    public Page<HashMap<String, Object>> getComments(Page<HashMap<String, Object>> pager, Integer restaurantId) {
        Page<HashMap<String, Object>> page = new Page<>(pager.getCurrent(),pager.getSize());
        List<HashMap<String, Object>> res = commentMapper.getComments(page, restaurantId);
        for (HashMap<String, Object> tmp : res) {
            String pic = (String) tmp.get("picture");
            JSONArray jsonArray = (JSONArray) JSONArray.parse(pic);
            List<String> list = JSONArray.parseArray(jsonArray.toJSONString(), String.class);
            tmp.put("pictures", list);
        }
        return page.setRecords(res);
    }
}
