package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.plan.model.to.message.MessageInfoTo;
import com.minzi.plan.model.to.message.MessageListTo;
import com.minzi.plan.model.vo.message.MessageSaveVo;
import com.minzi.plan.model.vo.message.MessageUpdateVo;
import com.minzi.plan.service.MessageService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/message")
public class MessageController {

    @Resource
    private MessageService messageService;


    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<MessageListTo> all = messageService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = messageService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        MessageInfoTo one = messageService.getOne(id);
        return R.ok().setData(one);
    }

    @PostMapping("/save")
    public R save(@RequestBody MessageSaveVo vo) {
        messageService.add(vo);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody MessageUpdateVo vo) {
        messageService.update(vo);
        return R.ok();
    }
}
