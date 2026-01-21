package com.minzi.plan.controller;


import com.minzi.common.core.query.PageUtils;
import com.minzi.common.core.query.R;
import com.minzi.plan.model.to.file.FileInfoTo;
import com.minzi.plan.model.to.file.FileListTo;
import com.minzi.plan.model.vo.file.FileSaveVo;
import com.minzi.plan.model.vo.file.FileUpdateVo;
import com.minzi.plan.service.FileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/file")
public class FileController {

    @Resource
    private FileService fileService;


    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page"))) {
            List<FileListTo> all = fileService.all(params);
            return R.ok().setData(all);
        }
        PageUtils pageUtils = fileService.queryPage(params);
        return R.ok().setData(pageUtils);
    }

    @GetMapping("/folderList")
    public R folderList(@RequestParam Map<String, Object> params) {
        List<FileListTo> fileListTos = fileService.folderList(params);
        return R.ok().setData(fileListTos);
    }

    @GetMapping("/info")
    public R info(@RequestParam Long id) {
        FileInfoTo one = fileService.getOne(id);
        return R.ok().setData(one);
    }

    @PostMapping("/save")
    public R save(@RequestBody FileSaveVo vo) {
        fileService.add(vo);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody FileUpdateVo vo) {
        fileService.update(vo);
        return R.ok();
    }

    @PostMapping("/saveDocument")
    public R saveDocument(@RequestBody FileUpdateVo vo) {
        fileService.saveDocument(vo);
        return R.ok();
    }

    @ApiOperation(value = "删除文件")
    @PostMapping("/delete")
    public R delete(@RequestBody String[] ids) {
        fileService.delete(ids);
        return R.ok();
    }

}
