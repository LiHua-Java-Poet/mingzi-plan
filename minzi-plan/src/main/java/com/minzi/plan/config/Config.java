package com.minzi.plan.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.minzi.common.tools.EntityAct;
import com.minzi.common.tools.impl.EntityActImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Map;


@Configuration
@EnableScheduling
public class Config {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许携带凭证
        config.addAllowedOrigin("*"); // 允许所有域名访问
        config.addAllowedHeader("*"); // 允许所有请求头
        config.addAllowedMethod("*"); // 允许所有 HTTP 方法
        config.addExposedHeader("*"); // 允许客户端访问所有响应头

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 应用到所有路径

        return new CorsFilter(source);
    }

    @Bean
    public RedisTemplate<String, Map<Integer, Long>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Map<Integer, Long>> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置key和value的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 根据数据库类型调整
        return interceptor;
    }

    @Bean
    public EntityAct getEntityAct(){
        return new EntityActImpl();
    }
}
