package com.minzi.plan.filter;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Log
@WebFilter(filterName = "jwtAuthenticationFilter", urlPatterns = {"/*"})
public class JwtAuthenticationFilter implements Filter {

    @Value("${filter.passage}")
    private String passageUriArray;

    private Set<String> passageUri = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String[] split = passageUriArray.split(",");
        passageUri.addAll(Arrays.asList(split));
        log.info("过滤器初始化");
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        log.info("过滤器销毁");
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        //请求的接口地址
        String requestURI = httpServletRequest.getRequestURI();
        if (!passageUri.contains(requestURI)) {
            //鉴权
            String token = httpServletRequest.getHeader("token");

            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}