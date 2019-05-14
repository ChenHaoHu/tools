package com.hcy.smartrobot.service;

import com.hcy.smartrobot.databse.UserData;
import com.hcy.smartrobot.entity.User;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserServiceImpl
 * @Author: hcy
 * @Description:
 * @Date: 2019-05-14 10:52
 * @Version: 1.0
 **/
@Service
public class UserServiceImpl implements UserService {


    @Override
    public boolean addUser(String name, String passwd) {
        boolean b = UserData.insertUser(new User(0, name, passwd));
        return b;
    }

    @Override
    public boolean loginUser(String name, String passwd) {
        User userByName = UserData.getUserByName(name);
        if (userByName == null){
            return false;
        }
        String pass  = userByName.getPasswd();

        if (pass.equals(passwd)){
            return true;
        }else{
            return false;
        }
    }
}
