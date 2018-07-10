package com.dynamic.appliction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @program: demo
 * @description: redis服务器的连接
 * @author: Mr.MO
 * @create: 2018-07-04 21:56
 **/
@Configuration
//maxInactiveIntervalInSeconds 默认是1800秒过期
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {

}
