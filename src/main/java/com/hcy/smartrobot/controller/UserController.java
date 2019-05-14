package com.hcy.smartrobot.controller;

import com.hcy.smartrobot.common.response.RespCode;
import com.hcy.smartrobot.common.response.ResponseEntity;
import com.hcy.smartrobot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/**
 * @ClassName: UserController
 * @Author: hcy
 * @Description:
 * @Date: 2019-05-14 10:57
 * @Version: 1.0
 **/
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/user/add")
    public ResponseEntity addUser(@RequestParam("name") String name,@RequestParam("passwd") String passwd){
        boolean b = userService.addUser(name, passwd);
        if (b == true){
            return new ResponseEntity(RespCode.SUCCESS,"注册成功");
        }else{
            return new ResponseEntity(RespCode.ERROR,"用户名已经存在");

        }
    }

    @RequestMapping("/user/login")
    public ResponseEntity loginUser(@NotEmpty(message = "账号不能为空")@RequestParam("name") String name, @RequestParam("passwd") String passwd){
        boolean b = userService.loginUser(name, passwd);
        if (b == true){
            return new ResponseEntity(RespCode.SUCCESS,"登陆成功");
        }else{
            return new ResponseEntity(RespCode.ERROR,"登陆失败");

        }
    }
}
