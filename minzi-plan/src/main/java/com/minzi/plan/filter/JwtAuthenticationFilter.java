package com.minzi.plan.filter;


import com.alibaba.fastjson.JSON;
import com.minzi.common.core.R;
import com.minzi.common.tools.SlidingWindow;
import com.minzi.common.utils.AppJwtUtil;
import com.minzi.plan.common.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;

import javax.annotation.Priority;
import javax.annotation.Resource;
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
@Priority(50)
@WebFilter(filterName = "jwtAuthenticationFilter", urlPatterns = {"/app/*"})
public class JwtAuthenticationFilter implements Filter {

    @Value("${filter.passage}")
    private String passageUriArray;

    @Resource
    private SlidingWindow slidingWindow;

    @Resource
    private UserContext userContext;

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
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json");
        //流量限制
        if (!slidingWindow.allowRequest(httpServletRequest.getRequestURI())) {
            PrintWriter writer = httpServletResponse.getWriter();
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            writer.write(JSON.toJSONString(R.error(-407, "服务器繁忙，请稍后再试")));
            return;
        }

        if (!HttpMethod.OPTIONS.toString().equals(((HttpServletRequest) servletRequest).getMethod()) && !passageUri.contains(requestURI)) {
            //鉴权
            String token = httpServletRequest.getHeader("Token");

            if (token == null) {
                PrintWriter writer = httpServletResponse.getWriter();
                httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
                writer.write(JSON.toJSONString(R.error(-402, "token为空，请检查")));
                return;
            }
            //鉴权
            if (!validateToken(token)) {
                PrintWriter writer = servletResponse.getWriter();
                httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
                writer.write(JSON.toJSONString(R.error(-406, "token无效")));
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
                userContext.setUserInfo(claims.get("name", String.class), claims.get("userName", String.class), claims.get("id", Long.class));
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}