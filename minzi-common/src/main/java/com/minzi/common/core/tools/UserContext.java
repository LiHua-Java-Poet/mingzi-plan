package com.minzi.common.core.tools;


import com.minzi.common.core.model.entity.UserEntity;
import org.springframework.stereotype.Component;


@Component
public class UserContext {

    public static ThreadLocal<UserEntity> threadUserLocal = new ThreadLocal<>();

    public UserEntity getUserInfo() {
        return threadUserLocal.get();
    }

    public Long getUserId() {
        UserEntity userEntity = threadUserLocal.get();
        return userEntity == null ? 0L : userEntity.getId();
    }

    public void setUserInfo(String name, String userName, long userId, Integer type) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName(name);
        userEntity.setUserName(userName);
        userEntity.setType(type);
        threadUserLocal.set(userEntity);
    }

}
