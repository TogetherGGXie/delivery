package com.delivery.main.common.persistence.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delivery.main.common.persistence.service.UserService;
import com.delivery.main.common.persistence.template.modal.User;
import com.delivery.main.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.xfire.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author null123
 * @since 2020-02-24
 */
@Api("微信用户管理")
@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${appId}")
    private String appId;

    @Value("${appSecret}")
    private String appSecret;

    @Autowired
    private UserService userService;

    @ApiOperation("微信用户登陆")
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Map decodeUserInfo(@RequestParam(value = "code") String code,
                              @RequestParam(value = "encryptedData") String encryptedData,
                              @RequestParam(value = "iv") String iv,
                              HttpServletRequest request){
        Integer statusCode = 0;
        Map map = new HashMap();
        //登录凭证不能为空
        if (code == null || code.length() == 0) {
            map.put("status", -1);
            map.put("message", "code 不能为空");
            return map;
        }

        //授权（必填）
        String grantType = "authorization_code";
        String path = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code +
                "&grant_type=" + grantType;
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;

            /**设置URLConnection的参数和普通的请求属性****start***/

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            /**设置URLConnection的参数和普通的请求属性****end***/

            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //GET和POST必须全大写
            conn.setRequestMethod("GET");
            /**GET方法请求*****start*/
            /**
             * 如果只是发送GET方式请求，使用connet方法建立和远程资源之间的实际连接即可；
             * 如果发送POST方式的请求，需要获取URLConnection实例对应的输出流来发送请求参数。
             * */
            conn.connect();

            /**GET方法请求*****end*/

            /***POST方法请求****start*/

            /*out = new PrintWriter(conn.getOutputStream());//获取URLConnection对象对应的输出流

            out.print(data);//发送请求参数即数据

            out.flush();//缓冲数据
            */
            /***POST方法请求****end*/

            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                //解决中文乱码问题
                str=new String(str.getBytes(),"UTF-8");
//                System.out.println("str = " + str);
                sb.append(str);
            }
            String result = sb.toString();
//            System.out.println("string="+result);
//            System.out.println("sessionId="+request.getSession().getId());
            //关闭流
            is.close();
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            JSONObject jsonObject = JSONObject.parseObject(result);
            Map<String,Object> mapper = (Map<String,Object>)jsonObject;
//            System.out.println("map="+jsonObject.toString());
            if(mapper.get("errcode") != null) {
                map.put("message", "登录失败，请重试");
                map.put("status", -1);
                return map;
            }
            User user = userService.selectOne(new EntityWrapper<User>().eq("open_id",mapper.get("openid")));
            if (user == null){
                statusCode = 201;
                user = new User();
                user.setOpenId(mapper.get("openid").toString());
                user.setCreateTime(DateUtil.date());
                userService.insert(user);
            }else {
                 statusCode = 200;
            }
            user = this.updateUser(mapper.get("session_key").toString(), encryptedData, iv, user);
            userService.updateById(user);
            request.getSession().setAttribute("user",user);
            map.put("sessionId",request.getSession().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", statusCode);
        return map;
    }

    @ApiOperation("设置用户信息")
    @RequestMapping(value = "admin/user_info" ,method = RequestMethod.POST)
    @ResponseBody
    public Result setUserInfo(@RequestParam HashMap<String,String> userInfo, HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return new Result(-1,"用户登录已失效");
        }else{
            user.setNickName(userInfo.get("nickName"));
            user.setGender(Integer.valueOf(userInfo.get("gender")));
            user.setLanguage(userInfo.get("language"));
            user.setProvince(userInfo.get("province"));
            user.setCity(userInfo.get("city"));
            user.setCountry(userInfo.get("country"));
            user.setAvatarUrl(userInfo.get("avatarUrl"));
            boolean update = userService.updateById(user);
            if(update){
                return new Result(200,"设置用户信息成功");
            }else{
                return new Result(-1,"设置用户信息失败");
            }
        }
    }



    private User updateUser(String sessionKey, String encryptedData, String iv, User user) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(result);
                user.setGender((Integer) jsonObject.get("gender"));
                user.setAvatarUrl(jsonObject.get("avatarUrl").toString());
                user.setNickName(jsonObject.get("nickName").toString());
                user.setLanguage(jsonObject.get("language").toString());
                return user;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }


}

