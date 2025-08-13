package com.minzi.plan.service;

import com.minzi.common.core.service.BaseService;
import com.minzi.plan.model.entity.FileEntity;
import com.minzi.plan.model.to.file.FileInfoTo;
import com.minzi.plan.model.to.file.FileListTo;
import com.minzi.plan.model.vo.file.FileSaveVo;
import com.minzi.plan.model.vo.file.FileUpdateVo;


public interface FileService extends BaseService<FileEntity,FileListTo,FileInfoTo, FileSaveVo, FileUpdateVo> {

    /**
     * 保存文档
     * @param vo 上传的文档内容
     */
    void saveDocument(FileUpdateVo vo);
}