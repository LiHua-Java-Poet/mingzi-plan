package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.model.entity.UserEntity;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.utils.ObjectUtils;
import com.minzi.plan.dao.MessageDao;
import com.minzi.plan.model.entity.MessageEntity;
import com.minzi.plan.model.entity.SessionEntity;
import com.minzi.plan.model.to.message.MessageInfoTo;
import com.minzi.plan.model.to.message.MessageListTo;
import com.minzi.plan.model.vo.message.MessageSaveVo;
import com.minzi.plan.model.vo.message.MessageUpdateVo;
import com.minzi.plan.service.MessageService;
import com.minzi.plan.service.SessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {

    @Resource
    private MessageService messageService;

    @Resource
    private EntityAct entityAct;

    @Resource
    private UserContext userContext;

    @Resource
    private SessionService sessionService;

    @Override
    public Wrapper<MessageEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<MessageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MessageEntity::getId);

        UserEntity userInfo = userContext.getUserInfo();
        wrapper.eq(MessageEntity::getUserId, userInfo.getId());

        Object sessionId = lambdaHashMap.get(MessageEntity::getSessionId);
        wrapper.eq(!StringUtils.isEmpty(sessionId), MessageEntity::getSessionId, sessionId);

        return wrapper;
    }

    @Override
    public List<MessageListTo> formatList(List<MessageEntity> list) {
        entityAct.oneToOne(list, MessageEntity::getSessionEntity);
        return list.stream().map(item -> {
            MessageListTo to = new MessageListTo();
            EntityUtils.copySameFields(item, to);
            to.setSessionTitle(ObjectUtils.getFieldValue(item.getSessionEntity(), SessionEntity::getTitle));
            return to;
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void add(MessageSaveVo messageSaveVo) {
        UserEntity userInfo = userContext.getUserInfo();
        MessageEntity entity = new MessageEntity();
        EntityUtils.copySameFields(messageSaveVo, entity);
        entity.setUserId(userInfo.getId());
        messageService.save(entity);

        //额外更新一下会话的时间
        sessionService.update(new LambdaUpdateWrapper<SessionEntity>()
                .eq(SessionEntity::getId, entity.getSessionId())
                .set(SessionEntity::getUpdateTime, DateUtils.currentDateTime()));
    }

    @Override
    public Wrapper<MessageEntity> getOneCondition(Map<String, Object> params) {
        LambdaQueryWrapper<MessageEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.last("limit 1");
        return wrapper;
    }

    @Override
    public MessageInfoTo formatOne(MessageEntity entity) {
        MessageInfoTo to = new MessageInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(MessageUpdateVo UpdateVo) {
        MessageEntity entity = messageService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        messageService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {
        messageService.remove(new LambdaQueryWrapper<MessageEntity>().in(MessageEntity::getId, ids));
    }
}