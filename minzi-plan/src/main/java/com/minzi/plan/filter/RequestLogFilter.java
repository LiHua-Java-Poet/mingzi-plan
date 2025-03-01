package com.minzi.plan.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.minzi.common.utils.DateUtils;
import com.minzi.plan.dao.RequestLogDao;
import com.minzi.plan.model.entity.RequestLogEntity;
import lombok.extern.java.Log;
import org.springframework.core.annotation.Order;

import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Log
@Priority(100)
@WebFilter(filterName = "requestLogFilter", urlPatterns = {"/*"})
public class RequestLogFilter implements Filter {


    @Resource
    private RequestLogDao requestLogDao;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 转换为 HttpServletRequest 和 HttpServletResponse
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 包装请求对象
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpServletRequest);
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);

//        // 记录请求信息
        String requestMethod = cachedRequest.getMethod();
        String requestURL = cachedRequest.getRequestURL().toString();
        Map<String, String> requestHeaders = getRequestHeaders(cachedRequest);
        String requestBody = getRequestBody(cachedRequest);
        Map<String, String[]> requestParams = httpServletRequest.getParameterMap();
        String remoteHost = httpServletRequest.getRemoteHost();
        int remotePort = httpServletRequest.getRemotePort();

        // 记录请求时间
        Integer startTime = DateUtils.currentDateTime();
        filterChain.doFilter(cachedRequest, responseWrapper);

        // 记录响应信息
        int responseStatus = responseWrapper.getStatus();
        String responseData = new String(responseWrapper.getResponseData(), responseWrapper.getCharacterEncoding());
        Integer duration = DateUtils.currentDateTime() - startTime;

        // 打印日志或存储到数据库
        RequestLogEntity requestLogEntity = logRequest(requestMethod, requestURL, requestHeaders, requestParams, requestBody, responseStatus, responseData, duration, startTime, remoteHost + ":" + remotePort);
        requestLogDao.insert(requestLogEntity);

        // 将响应数据写回客户端
        servletResponse.getOutputStream().write(responseWrapper.getResponseData());

    }

    private RequestLogEntity logRequest(String requestMethod,
                            String requestURL,
                            Map<String, String> requestHeaders,
                            Map<String, String[]> requestParams,
                            String requestBody,
                            int responseStatus,
                            String responseData,
                            Integer duration,
                            Integer startTime,
                            String remoteAddr) {
        RequestLogEntity log = new RequestLogEntity();
        log.setRequestUrl(requestURL);
        log.setRequestBody(requestBody);
        log.setRequestHeaders(JSON.toJSONString(requestHeaders));
        log.setRequestMethod(requestMethod);
        log.setRequestParams(JSON.toJSONString(requestParams));
        log.setResponseStatus(responseStatus);
        log.setResponseData(responseData);
        log.setRequestTime(startTime);
        log.setDurationTime(duration);
        log.setResponseTime(DateUtils.currentDateTime());
        log.setClientIp(remoteAddr);
        return log;
    }

    /**
     * 获取请求头
     */
    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    /**
     * 获取请求体
     */
    private String getRequestBody(HttpServletRequest request) {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            return "Failed to read request body";
        }
        return requestBody.toString();
    }

}
