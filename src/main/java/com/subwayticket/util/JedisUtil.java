package com.subwayticket.util;

import java.util.LinkedHashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis相关工具类
 * @author zhou-shengyun <GGGZ-1101-28@Live.cn>
 */
public class JedisUtil {
    private static final Map<String, JedisPool> jedisPoolMap = new LinkedHashMap<>();
    public static final String REDIS_DEFAULT_IP = "127.0.0.1";
    public static final int REDIS_DEFAULT_PORT = 6379;
    
    private JedisUtil(){}

    /**
     * 初始化默认主机地址的Jedis连接池
     */
    public static void initJedisPool(){
        initJedisPool(REDIS_DEFAULT_IP, REDIS_DEFAULT_PORT);
    }

    /**
     * 初始化指定主机地址的Jedis连接池
     * @param IP 指定主机地址的IP
     * @param port 指定主机地址的端口
     */
    public static void initJedisPool(String IP, int port){
        String key = IP + ":" + port;
        if(jedisPoolMap.containsKey(key))
            return;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnCreate(true);
        config.setMaxTotal(200);
        config.setMinIdle(10);
        JedisPool pool = new JedisPool(config, IP, port, 5000);
        jedisPoolMap.put(key, pool);
        
        //Test connection
        Jedis j = getJedis(IP, port);
        System.out.println("Jedis ping [" + key + "]: " + pool.getResource().ping());
        j.setex("SubwayTicket", 30, "Hello, Subway Ticket Server!");
        closeJedis(IP, port, j);
    }

    /**
     * 获取指定主机地址的Jedis连接池对象
     * @param IP 指定主机地址的IP
     * @param port 指定主机地址的端口
     * @return 指定主机地址的Jedis连接池对象
     */
    public static JedisPool getJedisPool(String IP, int port){
        String key = IP + ":" + port;
        return jedisPoolMap.get(key);
    }

    /**
     * 从默认主机地址的Jedis连接池中获取一个Jedis对象
     * @return Jedis对象实例
     */
    public static Jedis getJedis(){
        return getJedis(REDIS_DEFAULT_IP, REDIS_DEFAULT_PORT);
    }

    /**
     * 从指定主机的Jedis连接池中获取一个Jedis对象
     * @param IP 指定主机地址的IP
     * @param port 指定主机地址的端口
     * @return Jedis对象实例
     */
    public static Jedis getJedis(String IP, int port){
        JedisPool pool = getJedisPool(IP, port);
        return pool.getResource();
    }

    /**
     * 将一个Jedis对象返回到默认主机的连接池中
     * @param jedis Jedis对象实例
     * @deprecated
     */
    @Deprecated
    public static void closeJedis(Jedis jedis){
        closeJedis(REDIS_DEFAULT_IP, REDIS_DEFAULT_PORT, jedis);
    }

    /**
     * 将一个Jedis对象返回到指定主机的连接池中
     * @param IP 指定主机地址的IP
     * @param port 指定主机地址的端口
     * @param jedis Jedis对象
     * @deprecated
     */
    @Deprecated
    public static void closeJedis(String IP, int port, Jedis jedis){
        JedisPool pool = getJedisPool(IP, port);
        pool.returnResource(jedis);
    }
}
