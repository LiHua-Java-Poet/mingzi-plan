package com.minzi.common.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class PageUtils {

    Integer pageCount;

    Integer count;

    Integer page;

    Integer limit;

    List<?> list;

    public PageUtils(IPage<?> page){
        this.list=page.getRecords();
        this.count=(int)page.getTotal();
        this.limit=(int)page.getSize();
        this.page=(int)page.getCurrent();
        this.pageCount=(int)page.getPages();
    }

    public <T> List<T> list(){
        return (List<T>) this.list;
    }
}
