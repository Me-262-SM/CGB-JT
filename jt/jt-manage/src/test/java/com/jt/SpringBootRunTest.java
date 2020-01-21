package com.jt;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.HashSet;

@SpringBootTest
class SpringBootRunTest {
    private Jedis jedis;

    @BeforeEach
    void init(){
        jedis = new Jedis("176.201.103.23", 6379);
    }


    @Test
    void JedisTest(){
        //数据不被修改
//        jedis.setnx("a","hello");
        //同时设置超时时间和赋值（保证原子性）
//        jedis.setex("b",10,"bbb");
        /*1.数据不被修改  2.同时设置超时时间和赋值（保证原子性）*/
        SetParams params = new SetParams();
        params.nx().ex(10);
        jedis.set("1909","hello",params);
    }


    @Test
    void JedisHashTest(){
        jedis.hset("user","id","125");
        jedis.hset("person","name","tomcat");
        HashMap<String, String> map = new HashMap<>();
        map.put("id","444");
        map.put("name","NB");
        map.put("age","20");
        jedis.hset("student",map);
        System.out.println(jedis.hvals("person"));
        System.out.println(jedis.hgetAll("student"));
    }


    @Test
    void JedisTransactionTest(){
        Transaction transaction = jedis.multi();
        try {
            transaction.set("a","aa");
            transaction.set("b","bbb");
            transaction.set("c","ccc");
            transaction.exec();
        } catch (Exception e) {
            transaction.discard();
        }
    }


    @Test
    void JedisSentinel(){
        HashSet<String> set = new HashSet<>();
        set.add("176.201.103.23:26379");
        JedisSentinelPool pool = new JedisSentinelPool("mymaster", set);
        Jedis jedis_s = pool.getResource();
        jedis_s.set("1909","哨兵配置成功");
        System.out.println(jedis.get("1909"));
    }


    @Test
    void JedisCluster(){
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("176.201.103.23",7000));
        nodes.add(new HostAndPort("176.201.103.23",7001));
        nodes.add(new HostAndPort("176.201.103.23",7002));
        nodes.add(new HostAndPort("176.201.103.23",7003));
        nodes.add(new HostAndPort("176.201.103.23",7004));
        nodes.add(new HostAndPort("176.201.103.23",7005));
        JedisCluster cluster = new JedisCluster(nodes);
        cluster.set("key","redis cluster success");
        System.out.println(cluster.get("key"));
    }



}