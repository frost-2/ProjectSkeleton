package com.frost2.skeleton.service.impl;

import com.frost2.skeleton.common.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 陈伟平
 * @date 2020-10-29 16:26:20
 * @see RedisConfig Redis多数据源配置见RedisConfig
 */
@Service
public class RedisService {

    @Autowired
    @Resource(name = "LoginRedisTemplate")
    private StringRedisTemplate loginRedis;

    @Autowired
    @Resource(name = "DeviceRedisTemplate")
    StringRedisTemplate deviceRedis;

    /**
     * 测试是否可以连接Redis
     *
     * @return 是否存在key1和key2
     */
    public String test() {
        Boolean aBoolean = loginRedis.hasKey("key1");
        Boolean bBoolean = deviceRedis.hasKey("key2");
        return aBoolean + "-" + bBoolean;
    }

}
