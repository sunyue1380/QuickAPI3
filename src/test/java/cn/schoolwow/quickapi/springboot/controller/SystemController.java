package cn.schoolwow.quickapi.springboot.controller;

import cn.schoolwow.quickapi.springboot.domain.User;
import cn.schoolwow.quickapi.springboot.domain.UserExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @apiNote 系统服务
 * */
@RestController
@RequestMapping("/system")
public class SystemController {
    private Logger logger = LoggerFactory.getLogger(SystemController.class);
    /**
     * 获取用户列表
     * */
    @PostMapping(value = "/getUserList")
    public List<User> getUserList(){
        User user = new User();
        user.setUsername("quickapi");
        user.setPassword("123456");
        user.setAge(30);
        return Arrays.asList(user);
    }

    /**
     * 忘记密码
     * @param user 用户
     * */
    @PostMapping(value = "/updatePassword")
    public boolean updatePassword(
            @RequestBody User user
    ){
        logger.info("[更新密码]用户名:{},密码:{}",user.getUsername(),user.getPassword());
        return true;
    }

    /**
     * 更新用户信息
     * @param user 用户
     * */
    @PutMapping(value = "/updateUser")
    public Object updateUser(
            @RequestBody User user
    ){
        return true;
    }

    /**
     * 更新用户扩展信息
     * @param userExtend 用户扩展信息
     * */
    @PutMapping(value = "/updateUserExtend")
    public Object updateUserExtend(
            @RequestBody UserExtend userExtend
    ){
        return true;
    }

    /**
     * 忽略参数
     * @param file 文件
     * @param user 用户
     * */
    @PostMapping(value = "/ignore")
    public void ignore(
            MultipartFile file,
            User user,
            HttpServletResponse response
    ){}

    /**
     * 表单提交
     * @param user 用户
     * */
    @PostMapping(value = "/updateUser")
    public void upload(
            User user
    ){
        logger.info("[用户值]用户名:{},密码:{}",user.getUsername(),user.getPassword());
    }


}
