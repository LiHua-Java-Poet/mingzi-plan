package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.common.utils.AppJwtUtil;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.utils.StringUtils;
import com.minzi.plan.dao.UserDao;
import com.minzi.plan.model.to.user.UserInfoTo;
import com.minzi.plan.model.to.user.UserListTo;
import com.minzi.plan.model.to.user.UserLoginTo;
import com.minzi.plan.model.vo.user.UserRegVo;
import com.minzi.plan.model.vo.user.UserSaveVo;
import com.minzi.plan.model.vo.user.UserUpdateVo;
import com.minzi.plan.service.UserService;
import lombok.extern.java.Log;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.minzi.common.utils.MD5Utils.MD5Upper;

@Log
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    @Resource
    private UserService userService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<UserEntity> getList() {
        return userService.list();
    }


    @Override
    public R login(String userName, String password,String captchaCode,String timeToken) {
        //校验一次验证码是否正确
        //存一次redis
        ValueOperations<String, Object> redis = redisTemplate.opsForValue();
        R.dataParamsAssert(StringUtils.isEmpty(captchaCode),"请传入验证码");
        R.dataParamsAssert(StringUtils.isEmpty(redis.get("timeToken_" + timeToken)),"不存在验证码");
        R.dataParamsAssert(!captchaCode.equals(Objects.requireNonNull(redis.get("timeToken_" + timeToken)).toString()),"错误的验证码");

        List<UserEntity> list = userService.list(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUserName, userName));
        if (list.isEmpty()) {
            return R.error(402, "用户名不存在");
        }
        UserEntity userEntity = list.get(0);
        String newPassword = MD5Upper(password, userEntity.getCreateTime().toString());

        if (userEntity.getStatus() == 2) {
            return R.error(-402, "该账号已停用");
        }

        String password1 = userEntity.getPassword();
        if (!password1.equals(newPassword)) {
            return R.error(406, "密码错误");
        }

        //颁发token
        String token = AppJwtUtil.getToken(userEntity.getId(), userEntity.getName(), userEntity.getUserName());
        UserLoginTo to = new UserLoginTo();
        EntityUtils.copySameFields(userEntity, to);
        to.setToken(token);

        //这里额外将token存到redis中
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        map.put("id", userEntity.getId());
        map.put("name", userEntity.getName());
        map.put("userName", userEntity.getUserName());
        map.put("loginTime",DateUtils.currentDateTime());
        //存一次数据库
        hashOps.putAll(token, map);
        redisTemplate.expire(token,24, TimeUnit.HOURS);

        return R.ok().setData(to);
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

    @Override
    public void delete(String[] ids) {

    }

    @Override
    public UserInfoTo getOne(Long id) {
        return UserService.super.getOne(id);
    }

    @Override
    public Wrapper<UserEntity> getOneCondition(Map<String, Object> params) {
        return null;
    }

    @Override
    public UserInfoTo formatOne(UserEntity entity) {
        return null;
    }

    @Override
    public Wrapper<UserEntity> getListCondition(Map<String, Object> params) {
        return null;
    }

    @Override
    public List<UserListTo> formatList(List<UserEntity> list) {
        return null;
    }

    @Override
    public void add(UserSaveVo userSaveVo) {

    }

    @Override
    public void update(UserUpdateVo userUpdateVo) {

    }
}
