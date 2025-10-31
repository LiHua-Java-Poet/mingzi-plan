package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.core.tools.resubmit.Resubmit;
import com.minzi.common.utils.EntityUtils;
import com.minzi.plan.dao.SessionDao;
import com.minzi.plan.model.entity.SessionEntity;
import com.minzi.plan.model.to.session.SessionInfoTo;
import com.minzi.plan.model.to.session.SessionListTo;
import com.minzi.plan.model.vo.session.SessionSaveVo;
import com.minzi.plan.model.vo.session.SessionUpdateVo;
import com.minzi.plan.service.SessionService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl extends ServiceImpl<SessionDao, SessionEntity> implements SessionService {

    @Resource
    private SessionService sessionService;

    @Resource
    private UserContext userContext;

    @Resource
    private EntityAct entityAct;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Wrapper<SessionEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<SessionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SessionEntity::getCreateTime);
        wrapper.orderByDesc(SessionEntity::getId);

        UserEntity userInfo = userContext.getUserInfo();
        wrapper.eq(userInfo != null, SessionEntity::getUserId, userInfo.getId());

        return wrapper;
    }

    @Override
    public List<SessionListTo> formatList(List<SessionEntity> list) {
        return list.stream().map(item -> {
            SessionListTo to = new SessionListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Resubmit(voClass = SessionSaveVo.class)
    @Override
    public void add(SessionSaveVo sessionSaveVo) {
        SessionEntity entity = new SessionEntity();
        EntityUtils.copySameFields(sessionSaveVo, entity);
        entity.setUserId(userContext.getUserId());
        sessionService.save(entity);
    }

    @Override
    public Wrapper<SessionEntity> getOneCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SessionEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.last("limit 1");
        return wrapper;
    }

    @Override
    public SessionInfoTo formatOne(SessionEntity entity) {
        SessionInfoTo to = new SessionInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(SessionUpdateVo UpdateVo) {
        SessionEntity entity = sessionService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        sessionService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {
        sessionService.remove(new LambdaQueryWrapper<SessionEntity>().in(SessionEntity::getId, ids));
    }
}