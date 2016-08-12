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
        j.close();
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
     * 销毁所有的Jedis连接池
     */
    public static void destroyAllJedisPool(){
        System.out.println("Destroy all Jedis pools.");
        for(Map.Entry<String, JedisPool> e : jedisPoolMap.entrySet()){
            e.getValue().destroy();
        }
        jedisPoolMap.clear();
    }
}
