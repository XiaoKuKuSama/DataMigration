package com.ydx.datamigration.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存 配置
 */
@Configuration
public class CacheConfig {

    public static final int DEFAULT_MAXIMUMSIZE = 5000;  //最大缓存条数
    public static final int DEFAULT_EXPIREAFTERACCESS = 60; //最后一次写入或访问单位时间后过期



    /**
     * 必须要指定这个Bean，refreshAfterWrite=5s这个配置属性才生效 刷新缓存 用不到
     *
     * @return
     */
//    @Bean
//    public CacheLoader<Object, Object> cacheLoader() {
//
//        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
//
//            @Override
//            public Object load(Object key) throws Exception {
//                return null;
//            }
//
//            // 重写这个方法将oldValue值返回回去，进而刷新缓存
//            @Override
//            public Object reload(Object key, Object oldValue) throws Exception {
//                return oldValue;
//            }
//        };
//
//        return cacheLoader;
//    }


    /**
     * 创建基于Caffeine的Cache Manager
     * @return
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        ArrayList<CaffeineCache> caches = Lists.newArrayList();

        caches.add(new CaffeineCache("ydxAgentInfoCatch",
                Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterAccess(DEFAULT_EXPIREAFTERACCESS,TimeUnit.SECONDS)
                        .maximumSize(DEFAULT_MAXIMUMSIZE)
                        .build())
        );

        caches.add(new CaffeineCache("ydxSysUserCatch",
                Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterAccess(DEFAULT_EXPIREAFTERACCESS, TimeUnit.SECONDS)
                        .maximumSize(DEFAULT_MAXIMUMSIZE)
                        .build())
        );


        cacheManager.setCaches(caches);

        return cacheManager;
    }

}
