package com.minzi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;


public interface BaseService<T, LIST_TO, INFO_TO, SAVE_VO, UPDATE_VO> extends SaveBaseService<SAVE_VO>, UpdateBaseService<UPDATE_VO>,ListBaseService<T,LIST_TO> ,InfoBaseService<T,INFO_TO>,IService<T> {
}
