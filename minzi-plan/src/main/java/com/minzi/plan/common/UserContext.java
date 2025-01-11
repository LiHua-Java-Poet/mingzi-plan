package com.minzi.plan.common;


import com.minzi.plan.model.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserContext {

    private Map<Long, UserEntity> threadMap = new HashMap<>();

    public UserEntity getUserInfo(){
        long id = Thread.currentThread().getId();
        return threadMap.get(id);
    }

    public void setUserInfo(String name,String userName,long userId){
        long id = Thread.currentThread().getId();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName(name);
        userEntity.setUserName(userName);
        threadMap.put(id,userEntity);
    }
}
