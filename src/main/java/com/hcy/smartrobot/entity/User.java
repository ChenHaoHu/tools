package com.hcy.smartrobot.entity;

import lombok.Data;

/**
 * @ClassName: User
 * @Author: hcy
 * @Description:
 * @Date: 2019-05-14 10:44
 * @Version: 1.0
 **/
@Data
public class User {
    int id;
    String name;
    String passwd;

    public User(int id, String name, String passwd) {
        this.id = id;
        this.name = name;
        this.passwd = passwd;
    }
}
