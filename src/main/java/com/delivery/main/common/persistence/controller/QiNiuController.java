package com.delivery.main.common.persistence.controller;

import cn.hutool.Hutool;
import cn.hutool.core.util.IdUtil;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/qiniu")
@Api("七牛控制器")
public class QiNiuController {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @RequestMapping("/getToken")
    @ApiOperation("获取上传token")
    public Result getToken(HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            return new Result(-1, "登录状态失效");
        }
        Auth auth = Auth.create(accessKey, secretKey);
        HashMap<String, String> object = new HashMap<>();
        object.put("token", auth.uploadToken(bucket));
        object.put("uuid", IdUtil.simpleUUID());

        return new Result(200, "获取token成功", object);
    }

}