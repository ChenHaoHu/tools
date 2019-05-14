package com.hcy.smartrobot.databse;

import com.alibaba.fastjson.JSONArray;
import com.hcy.smartrobot.entity.User;

/**
 * @ClassName: Data
 * @Author: hcy
 * @Description:  模拟数据库
 * @Date: 2019-05-14 10:41
 * @Version: 1.0
 **/
public class UserData {

   private static JSONArray data = new JSONArray();
   private static int count = 0;


   public static synchronized boolean insertUser(User user){

      for (int i = 0; i < data.size(); i++) {
         User o = (User)data.get(i);
         if (o.getName().equals(user.getName())){
            return false;
         }
      }
      count++;
      user.setId(count);
      data.add(user);
      return true;

   }

   public static  synchronized void deletetUserById(int id){
      //
   }

   public  static  synchronized User getUserByName(String name){
      for (int i = 0; i < data.size(); i++) {
         User o = (User)data.get(i);
         if (o.getName().equals(name)){
            return o;
         }
      }
      return null;
   }
}
