package com.minzi.plan.consumer;

import com.alibaba.fastjson.JSONObject;
import com.minzi.plan.dao.RequestLogDao;
import com.minzi.plan.model.entity.RequestLogEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RequestLogConsumer {

    @Resource
    private RequestLogDao requestLogDao;

    @RabbitListener(queues = "requestLogMqQueue")
    public void receiveMessage(String message) {
        RequestLogEntity requestLogEntity = JSONObject.parseObject(message, RequestLogEntity.class);
        requestLogDao.insert(requestLogEntity);
    }
}
