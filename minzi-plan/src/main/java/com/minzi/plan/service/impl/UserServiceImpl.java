package com.minzi.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.utils.AppJwtUtil;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.utils.StringUtils;
import com.minzi.plan.dao.UserDao;
import com.minzi.plan.model.entity.*;
import com.minzi.plan.model.enums.SysMenuEnums;
import com.minzi.plan.model.enums.UserEnums;
import com.minzi.plan.model.to.sysMenu.SysMenuListTo;
import com.minzi.plan.model.to.sysRole.SysRoleInfoTo;
import com.minzi.plan.model.to.sysRole.SysRoleListTo;
import com.minzi.plan.model.to.task.TaskItemTo;
import com.minzi.plan.model.to.task.TaskListTo;
import com.minzi.plan.model.to.user.UserInfoTo;
import com.minzi.plan.model.to.user.UserListTo;
import com.minzi.plan.model.to.user.UserLoginTo;
import com.minzi.plan.model.vo.user.UserRegVo;
import com.minzi.plan.model.vo.user.UserSaveVo;
import com.minzi.plan.model.vo.user.UserUpdateVo;
import com.minzi.plan.service.*;
import lombok.extern.java.Log;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.minzi.common.utils.MD5Utils.MD5Upper;

@Log
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    @Resource
    private UserService userService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserContext userContext;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public List<UserEntity> getList() {
        return userService.list();
    }


    @Override
    public R login(String userName, String password, String captchaCode, String timeToken) {
        //校验一次验证码是否正确
        //存一次redis
        ValueOperations<String, Object> redis = redisTemplate.opsForValue();
        R.dataParamsAssert(StringUtils.isEmpty(captchaCode), "请传入验证码");
        R.dataParamsAssert(StringUtils.isEmpty(redis.get("timeToken_" + timeToken)), "不存在验证码");
        R.dataParamsAssert(!captchaCode.equals(Objects.requireNonNull(redis.get("timeToken_" + timeToken)).toString()), "错误的验证码");

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
        map.put("loginTime", DateUtils.currentDateTime());
        map.put("type", userEntity.getType());
        //存一次数据库
        hashOps.putAll(token, map);
        redisTemplate.expire(token, 24, TimeUnit.HOURS);

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
    public List<SysMenuListTo> getUserMenu() {
        UserEntity userInfo = userContext.getUserInfo();

        //获取到角色的菜单信息
        List<SysUserRoleEntity> userRoleList = sysUserRoleService.list(new LambdaQueryWrapper<SysUserRoleEntity>().eq(SysUserRoleEntity::getUserId, userInfo.getId()));
        Set<Long> roleIdList = userRoleList.stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toSet());
        List<SysRoleEntity> roleList = sysRoleService.list(new LambdaQueryWrapper<SysRoleEntity>().in(SysRoleEntity::getId, roleIdList));
        List<Long> roleIdlIST = roleList.stream().map(SysRoleEntity::getId).collect(Collectors.toList());
        List<SysRoleMenuEntity> roleMenuEntityList = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenuEntity>().in(SysRoleMenuEntity::getRoleId, roleIdlIST));
        Set<Long> menuIdList = roleMenuEntityList.stream().map(SysRoleMenuEntity::getMenuId).collect(Collectors.toSet());
        List<SysMenuEntity> menuEntityList = sysMenuService.list(new LambdaQueryWrapper<SysMenuEntity>()
                .in(SysMenuEntity::getId, menuIdList)
                .eq(SysMenuEntity::getStatus, SysMenuEnums.SysMenuStatus.ZENG_CHANG.getCode()));

        //获取到用户类型
        LambdaQueryWrapper<SysMenuEntity> wrapper = new LambdaQueryWrapper<SysMenuEntity>().eq(SysMenuEntity::getStatus, SysMenuEnums.SysMenuStatus.ZENG_CHANG.getCode());
        if (UserEnums.UserType.YON_HU.getCode().equals(userInfo.getType())) wrapper.eq(SysMenuEntity::getMenuType, SysMenuEnums.MenuType.PU_TON.getCode());
        List<SysMenuEntity> allMenuList = sysMenuService.list(wrapper);
        menuEntityList.addAll(allMenuList);


        // 基于 menuId 去重（保留第一次出现的）
        menuEntityList = new ArrayList<>(
                menuEntityList.stream()
                        .collect(Collectors.toMap(
                                SysMenuEntity::getId,          // key = menuId
                                e -> e,                        // value = entity
                                (oldVal, newVal) -> oldVal     // 冲突时保留旧的
                        ))
                        .values()
        );

        //这里手动做一次排序
        menuEntityList.sort(
                Comparator.comparing(SysMenuEntity::getSort, Comparator.nullsLast(Integer::compareTo))
                        .reversed()
        );

        return sysMenuService.formatList(menuEntityList);
    }

    @Override
    public void delete(String[] ids) {

    }

    @Override
    public UserInfoTo getOne(Long id) {
        return UserService.super.getOne(id);
    }


    @Override
    public UserInfoTo formatOne(UserEntity entity) {
        return null;
    }

    @Override
    public Wrapper<UserEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(UserEntity::getId);

        //查询 - Id
        Object id = lambdaHashMap.get(UserEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), UserEntity::getId, id);

        //查询 - 状态
        Object status = lambdaHashMap.get(UserEntity::getStatus);
        wrapper.eq(!StringUtils.isEmpty(status), UserEntity::getStatus, status);

        Object userName = lambdaHashMap.get(UserEntity::getUserName);
        wrapper.like(!StringUtils.isEmpty(userName), UserEntity::getUserName, userName);

        Object name = lambdaHashMap.get(UserEntity::getName);
        wrapper.like(!StringUtils.isEmpty(name), UserEntity::getName, name);

        Object account = lambdaHashMap.get(UserEntity::getAccount);
        wrapper.like(!StringUtils.isEmpty(account), UserEntity::getAccount, account);

        Object phone = lambdaHashMap.get(UserEntity::getPhone);
        wrapper.like(!StringUtils.isEmpty(phone), UserEntity::getPhone, phone);

        Object type = lambdaHashMap.get(UserEntity::getType);
        wrapper.eq(!StringUtils.isEmpty(type), UserEntity::getType, type);

        return wrapper;
    }

    @Override
    public List<UserListTo> formatList(List<UserEntity> list) {
        //这里查一次用户的角色
        Set<Long> userIdList = list.stream().map(UserEntity::getId).collect(Collectors.toSet());

        List<SysUserRoleEntity> userRoleList = sysUserRoleService.list(new LambdaQueryWrapper<SysUserRoleEntity>().in(SysUserRoleEntity::getUserId, userIdList));
        Set<Long> roleIdList = userRoleList.stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toSet());
        Map<Long, List<SysUserRoleEntity>> userIdMap = EntityUtils.resortEntityByColumnLevel2(userRoleList, SysUserRoleEntity::getUserId);


        //获取到对应的角色id映射
        List<SysRoleEntity> roleList = sysRoleService.list(new LambdaQueryWrapper<SysRoleEntity>().in(SysRoleEntity::getId, roleIdList));
        Map<Long, SysRoleEntity> roleIdMap = EntityUtils.resortEntityByColumnLevel1(roleList, SysRoleEntity::getId);

        return list.stream().map(item -> {
            UserListTo to = new UserListTo();
            EntityUtils.copySameFields(item, to);
            to.setRoleList(new ArrayList<>());

            //拿到对应的用户信息
            List<SysUserRoleEntity> userRoleEntityList = userIdMap.get(item.getId());
            if (userRoleEntityList == null) return to;
            List<SysRoleInfoTo> roleListToList = userRoleEntityList.stream().filter(b -> roleIdMap.get(b.getRoleId()) != null).map(role -> {
                SysRoleInfoTo sysRoleEntity = new SysRoleInfoTo();
                SysRoleEntity sysRole = roleIdMap.get(role.getRoleId());
                EntityUtils.copySameFields(sysRole, sysRoleEntity);
                return sysRoleEntity;
            }).collect(Collectors.toList());
            to.setRoleList(roleListToList);

            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(UserSaveVo userSaveVo) {

    }

    @Override
    public void update(UserUpdateVo userUpdateVo) {

    }
}
