package com.minzi.plan.filter;


import com.alibaba.fastjson.JSON;
import com.minzi.common.core.R;
import com.minzi.common.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
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
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        //请求的接口地址
        String requestURI = httpServletRequest.getRequestURI();
        if (!HttpMethod.OPTIONS.toString().equals(((HttpServletRequest) servletRequest).getMethod()) && !passageUri.contains(requestURI)) {
            //鉴权
            String token = httpServletRequest.getHeader("Token");
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json");

            if (token == null) {
                PrintWriter writer = httpServletResponse.getWriter();
                httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
                writer.write(JSON.toJSONString(R.error(402, "token为空，请检查")));
                return;
            }
            //鉴权
            if (!validateToken(token)) {
                PrintWriter writer = servletResponse.getWriter();
                httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
                writer.write(JSON.toJSONString(R.error(402, "token无效")));
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean validateToken(String token) {
        try {
            Jws<Claims> jws = AppJwtUtil.getJws(token);
            Claims claims = jws.getBody();
            int i = AppJwtUtil.verifyToken(claims);
            if (i < 1) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}