package com.subwayticket.util;

import java.util.LinkedHashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author buptsse-zero <GGGZ-1101-28@Live.cn>
 */
public class JedisUtil {
    private static final Map<String, JedisPool> jedisPoolMap = new LinkedHashMap<>();
    public static final String REDIS_DEFAULT_IP = "127.0.0.1";
    public static final int REDIS_DEFAULT_PORT = 6379;
    
    private JedisUtil(){}
    
    public static void initJedisPool(){
        initJedisPool(REDIS_DEFAULT_IP, REDIS_DEFAULT_PORT);
    }
    
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
    
    public static JedisPool getJedisPool(String IP, int port){
        String key = IP + ":" + port;
        return jedisPoolMap.get(key);
    }
    
    public static Jedis getJedis(){
        return getJedis(REDIS_DEFAULT_IP, REDIS_DEFAULT_PORT);
    }
    
    public static Jedis getJedis(String IP, int port){
        JedisPool pool = getJedisPool(IP, port);
        return pool.getResource();
    }
    
    public static void closeJedis(Jedis jedis){
        closeJedis(REDIS_DEFAULT_IP, REDIS_DEFAULT_PORT, jedis);
    }
    
    public static void closeJedis(String IP, int port, Jedis jedis){
        JedisPool pool = getJedisPool(IP, port);
        pool.returnResource(jedis);
    }
}
