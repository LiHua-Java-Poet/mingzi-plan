package com.minzi.common.core;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@ApiImplicitParams({
        @ApiImplicitParam(name = "token",value = "认证token",required = true, dataType = "string", paramType = "header")
})
public interface BaseController {
}
