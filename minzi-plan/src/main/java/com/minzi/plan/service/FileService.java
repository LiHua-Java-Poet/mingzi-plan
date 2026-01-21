package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.FileEntity;
import com.minzi.plan.model.to.file.FileInfoTo;
import com.minzi.plan.model.to.file.FileListTo;
import com.minzi.plan.model.vo.file.FileSaveVo;
import com.minzi.plan.model.vo.file.FileUpdateVo;

import java.util.List;
import java.util.Map;


public interface FileService extends BaseService<FileEntity,FileListTo,FileInfoTo, FileSaveVo, FileUpdateVo> {

    /**
     * 保存文档
     * @param vo 上传的文档内容
     */
    void saveDocument(FileUpdateVo vo);

    /**
     * 获取到文件夹目录
     * @param params
     */
    List<FileListTo> folderList(Map<String, Object> params);
}