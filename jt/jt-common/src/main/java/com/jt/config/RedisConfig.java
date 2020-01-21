package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
//    @Value("${redis.host}")
//    private String host;
//    @Value("${redis.port}")
//    private Integer port;
//    @Value("${redis.nodes}")
//    private String nodes;
//    @Value("${redis.sentinel}")
//    private String sentinel;
    @Value("${redis.clusterNodes}")
    private String clusterNodes;

    /**
     * SpringBoot整合redis
     */
//    @Bean
//    @Scope("prototype")
//    public Jedis jedis(){
//        return new Jedis(host,port);
//    }

    /**
     * 配置redis分片
     */
//    @Bean
//    @Scope("prototype")
//    public ShardedJedis shardedJedis(){
//        ArrayList<JedisShardInfo> shards = new ArrayList<>();
//        String[] arrayNode = nodes.split(",");
//
//        for (String s : arrayNode) {
//            String host = s.split(":")[0];
//            int port = Integer.parseInt(s.split(":")[1]);
//            JedisShardInfo info = new JedisShardInfo(host, port);
//            shards.add(info);
//        }
//        return new ShardedJedis(shards);
//    }

    /**
     * redis哨兵
     */
//    @Bean
//    public JedisSentinelPool jedissentinelPool(){
//        HashSet<String> set = new HashSet<>();
//        set.add(sentinel);
//        return new JedisSentinelPool("mymaster", set);
//    }


    /**
     * redis 集群
     */
    @Bean
    @Scope("prototype")
    public JedisCluster jedisCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        String[] split = clusterNodes.split(",");
        for (String s : split) {
            String host = s.split(":")[0];
            int port = Integer.parseInt(s.split(":")[1]);
            nodes.add(new HostAndPort(host,port));
        }
        return new JedisCluster(nodes);
    }


}
