package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.SessionEntity;
import com.minzi.plan.model.to.session.SessionInfoTo;
import com.minzi.plan.model.to.session.SessionListTo;
import com.minzi.plan.model.vo.session.SessionSaveVo;
import com.minzi.plan.model.vo.session.SessionUpdateVo;


public interface SessionService extends BaseService<SessionEntity,SessionListTo,SessionInfoTo, SessionSaveVo, SessionUpdateVo> {
}