package com.minzi.plan.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minzi.plan.dao.SysConfigDao;
import com.minzi.plan.model.entity.SysConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SysConfigContext {

    @Resource
    private SysConfigDao sysConfigDao;

    private Map<String, String> configMap = null;

    @PostConstruct
    public void init() {
        log.info("配置加载完成，初始化配置信息");
        //得到所有的配置信息
        List<SysConfigEntity> sysConfigEntities = sysConfigDao.selectList(new LambdaQueryWrapper<SysConfigEntity>().eq(SysConfigEntity::getStatus, 1));
        Map<String, String> configMap = sysConfigEntities.stream().collect(Collectors.toMap(SysConfigEntity::getConfName, SysConfigEntity::getConfContent));
        this.configMap = configMap;
    }

    public String getConfigContext(String key){
        return configMap.get(key);
    }


}
