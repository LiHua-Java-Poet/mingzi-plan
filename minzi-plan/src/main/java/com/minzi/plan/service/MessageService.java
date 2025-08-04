package com.minzi.plan.service;

import com.minzi.common.service.BaseService;
import com.minzi.plan.model.entity.MessageEntity;
import com.minzi.plan.model.to.message.MessageInfoTo;
import com.minzi.plan.model.to.message.MessageListTo;
import com.minzi.plan.model.vo.message.MessageSaveVo;
import com.minzi.plan.model.vo.message.MessageUpdateVo;


public interface MessageService extends BaseService<MessageEntity,MessageListTo,MessageInfoTo, MessageSaveVo, MessageUpdateVo> {
}