package com.minzi.plan.common;


import com.minzi.plan.model.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserContext {

    private Map<Long, UserEntity> threadMap = new ConcurrentHashMap<>();

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
        updateHashMap(threadMap,userEntity,id);
        threadMap.put(id,userEntity);
    }

    private void updateHashMap(Map<Long, UserEntity> hashMap, UserEntity newObject, Long threadId) {
        // 查找是否已存在该对象
        Long existingKey = null;
        for (Map.Entry<Long, UserEntity> entry : hashMap.entrySet()) {
            if (entry.getValue().equals(newObject)) {
                existingKey = entry.getKey();
                break;
            }
        }

        // 如果找到，删除旧的键值对
        if (existingKey != null) {
            hashMap.remove(existingKey);
        }

        // 插入新对象，主键为当前线程的 ID
        hashMap.put(threadId, newObject);
    }
}
