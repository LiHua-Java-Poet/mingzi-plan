package com.minzi.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzi.common.core.map.LambdaHashMap;
import com.minzi.common.core.tools.EntityAct;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.EntityUtils;
import com.minzi.common.core.tools.UserContext;
import com.minzi.common.utils.StringUtils;
import com.minzi.plan.dao.SysMenuDao;
import com.minzi.plan.model.entity.SysMenuEntity;
import com.minzi.plan.model.to.sysMenu.SysMenuInfoTo;
import com.minzi.plan.model.to.sysMenu.SysMenuListTo;
import com.minzi.plan.model.vo.sysMenu.SysMenuSaveVo;
import com.minzi.plan.model.vo.sysMenu.SysMenuUpdateVo;
import com.minzi.plan.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private EntityAct entityAct;

    @Override
    public Wrapper<SysMenuEntity> getListCondition(Map<String, Object> params) {
        LambdaHashMap<String, Object> lambdaHashMap = new LambdaHashMap<>(params);
        LambdaQueryWrapper<SysMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysMenuEntity::getSort);

        Object key = params.get("key");
        wrapper.or().like(!StringUtils.isEmpty(key), SysMenuEntity::getMenuName, key)
                .or().like(!StringUtils.isEmpty(key), SysMenuEntity::getNavigatorPath, key)
                .or().like(!StringUtils.isEmpty(key), SysMenuEntity::getComponetPath, key);

        Object id = lambdaHashMap.get(SysMenuEntity::getId);
        wrapper.eq(!StringUtils.isEmpty(id), SysMenuEntity::getId, id);

        Object menuName = lambdaHashMap.get(SysMenuEntity::getMenuName);
        wrapper.like(!StringUtils.isEmpty(menuName), SysMenuEntity::getMenuName, menuName);

        Object navigatorPath = lambdaHashMap.get(SysMenuEntity::getNavigatorPath);
        wrapper.like(!StringUtils.isEmpty(navigatorPath), SysMenuEntity::getNavigatorPath, navigatorPath);

        Object componetPath = lambdaHashMap.get(SysMenuEntity::getComponetPath);
        wrapper.like(!StringUtils.isEmpty(componetPath), SysMenuEntity::getComponetPath, componetPath);

        Object status = lambdaHashMap.get(SysMenuEntity::getStatus);
        wrapper.eq(!StringUtils.isEmpty(status), SysMenuEntity::getStatus, status);

        Object menuType = lambdaHashMap.get(SysMenuEntity::getMenuType);
        wrapper.eq(!StringUtils.isEmpty(menuType), SysMenuEntity::getMenuType, menuType);

        return wrapper;
    }

    @Override
    public List<SysMenuListTo> formatList(List<SysMenuEntity> list) {
        return list.stream().map(item -> {
            SysMenuListTo to = new SysMenuListTo();
            EntityUtils.copySameFields(item, to);
            return to;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(SysMenuSaveVo sysMenuSaveVo) {
        SysMenuEntity entity = new SysMenuEntity();
        EntityUtils.copySameFields(sysMenuSaveVo, entity);
        sysMenuService.save(entity);
    }


    @Override
    public SysMenuInfoTo formatOne(SysMenuEntity entity) {
        SysMenuInfoTo to = new SysMenuInfoTo();
        EntityUtils.copySameFields(entity, to);
        return to;
    }

    @Override
    public void update(SysMenuUpdateVo UpdateVo) {
        SysMenuEntity entity = sysMenuService.getById(UpdateVo.getId());
        EntityUtils.copySameFields(UpdateVo, entity);
        entity.setUpdateTime(DateUtils.currentDateTime());
        sysMenuService.updateById(entity);
    }

    @Override
    public void delete(String[] ids) {

        sysMenuService.update(
                new LambdaUpdateWrapper<SysMenuEntity>()
                        .set(SysMenuEntity::getDeleteTime, DateUtils.currentDateTime())
                        .in(SysMenuEntity::getId, ids)
        );
    }

}