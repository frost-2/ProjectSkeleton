package com.frost2.skeleton.common.config;

import com.frost2.skeleton.common.util.StringUtils;
import com.frost2.skeleton.service.impl.RedisService;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

/**
 * @author 陈伟平
 * @date 2020-10-20 14:56:00
 * @see RedisService 具体使用情况见RedisService
 */
@EnableCaching
@Configuration
public class RedisConfig {

    @Value("${spring.redis.lettuce.pool.max-idle}")
    int maxIdle;
    @Value("${spring.redis.lettuce.pool.max-active}")
    int maxActive;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    long maxWaitMillis;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    int minIdle;
    @Value("${spring.redis.timeout}")
    int timeout;

    @Bean(name = "LoginRedisTemplate")
    public StringRedisTemplate redisTemplate(@Value("${spring.redis.database}") int database,
                                             @Value("${spring.redis.host}") String hostName,
                                             @Value("${spring.redis.port}") int port,
                                             @Value("${spring.redis.password}") String password) {
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(connectionFactory(database, hostName, port, password));
        return temple;
    }


    @Bean(name = "DeviceRedisTemplate")
    public StringRedisTemplate redisUatTemplate(@Value("${spring.redis2.database}") int database,
                                                @Value("${spring.redis2.host}") String hostName,
                                                @Value("${spring.redis2.port}") int port,
                                                @Value("${spring.redis2.password}") String password) {
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(connectionFactory(database, hostName, port, password));
        return temple;
    }

    /**
     * 使用lettuce配置Redis连接信息
     *
     * @param database Redis数据库编号
     * @param hostName 服务器地址
     * @param port     端口
     * @param password 密码
     * @return RedisConnectionFactory
     */
    public RedisConnectionFactory connectionFactory(int database, String hostName, int port, String password) {

        //redis基础配置信息
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(hostName);
        configuration.setPort(port);
        if (StringUtils.isNotBlank(password)) {
            configuration.setPassword(password);
        }
        if (database != 0) {
            configuration.setDatabase(database);
        }

        //Redis连接池配置信息: commons-pool就用这这里
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillis);

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(genericObjectPoolConfig)
                .build();

        LettuceConnectionFactory lettuce = new LettuceConnectionFactory(configuration, clientConfig);
        lettuce.afterPropertiesSet();
        return lettuce;
    }
}


