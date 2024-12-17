package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.R;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.MD5Utils;
import com.minzi.common.utils.ObjectUtils;
import com.minzi.plan.dao.UserDao;
import com.minzi.plan.model.entity.UserEntity;
import com.minzi.plan.model.vo.user.UserRegVo;
import com.minzi.plan.service.UserService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.minzi.common.utils.MD5Utils.MD5Upper;

@Log
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    @Resource
    private UserService userService;


    @Override
    public List<UserEntity> getList() {
        List<UserEntity> list = userService.list();
        return userService.list();
    }


    @Override
    public R login(String account, String password) {

        List<UserEntity> list = userService.list(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getAccount, account));
        if (list.isEmpty()){
            return R.fail();
        }
        UserEntity userEntity = list.get(0);
        String newPassword = MD5Upper(password, userEntity.getCreateTime().toString());

        String password1 = userEntity.getPassword();
        if (!password1.equals(newPassword)){
            return R.fail();
        }
        return R.ok();
    }

    @Override
    public void reg(UserRegVo vo) {
        Integer currentTime = DateUtils.currentDateTime();
        String newPassword = MD5Upper(vo.getPassword(), currentTime.toString());

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(vo.getUserName());
        userEntity.setName(vo.getName());
        userEntity.setAccount(vo.getAccount());
        userEntity.setPassword(newPassword);
        userEntity.setStatus(1);
        userEntity.setCreateTime(currentTime);
        userService.save(userEntity);
    }
}
