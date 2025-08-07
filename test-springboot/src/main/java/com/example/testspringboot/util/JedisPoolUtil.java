package com.example.testspringboot.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Properties;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/15 13:26
 */
public class JedisPoolUtil {
    // Redis服务器地址(域名或IP)
    private static String host;

    // Redis服务器连接端⼝(Redis默认端⼝号是6379)
    private static String port;

    // Redis服务器连接密码(默认为空)
    private static String password;

    // 最⼤连接数
    private static String maxTotal;

    // 最⼤空闲连接数
    private static String maxIdle;

    // 最⼤的阻塞时⻓
    private static String maxWait;

    // 向资源池借⽤连接时是否做连接有效性检测（ping）。检测到的⽆效连接将会被移除
    private static String testOnBorrow;

    // 存储数据库编号
    private static String database;

    private volatile static JedisPool jedisPool = null;
    private static final Jedis jedis = null;

    static {
        // 读取配置⽂件。加载redis.properties配置⽂件，通过反射的⽅式得到⽂件输⼊流
        InputStream inputStream = JedisPoolUtil.class.getClassLoader().getResourceAsStream("redis.properties");
        // 创建读取配置⽂件的properties对象，Properties继承了Hashtable类，Hashtable类实现了Map接⼝
        Properties properties = new Properties();
        try {
            /*
             * 1.⽅法作⽤：从字节输⼊流中读取键值对。该⽅法常⽤于读取配置⽂件。
             * 2.参数含义：参数中使⽤了字节输⼊流，通过流对象可以关联到某⽂件上，这样就能够加载⽂本中的数据了。⽂本中的数据，必须是键值对形式，可以使⽤空格、等号、冒号等符号分隔。
             */
            properties.load(inputStream);
            // 获取Redis数据库连接信息
            host = properties.getProperty("redis.host");
            port = properties.getProperty("redis.port");
            password = properties.getProperty("redis.password");
            maxTotal = properties.getProperty("redis.maxTotal");
            maxIdle = properties.getProperty("redis.maxIdle");
            maxWait = properties.getProperty("redis.maxWait");
            testOnBorrow = properties.getProperty("redis.testOnBorrow");
            database = properties.getProperty("redis.database", "0");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private JedisPoolUtil() {

    }

    private static JedisPool getInstance() {
        // 单例模式实现：双检锁/双重校验锁。这种⽅式采⽤双锁机制，安全且在多线程情况下能保持⾼性能
        if (jedisPool == null) {
            synchronized (JedisPoolUtil.class) {
                if (jedisPool == null) {
                    // 创建⼀个配置对象
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxTotal(Integer.parseInt(maxTotal)); // 资源池中的最⼤连接数
                    config.setMaxIdle(Integer.parseInt(maxIdle)); // 资源池允许的最⼤空闲连接数
                    // 当资源池连接⽤尽后，调⽤者的最⼤等待时间(单位为毫秒)
                    config.setMaxWait(Duration.ofMillis(Long.parseLong(maxWait)));
                    // 向资源池借⽤连接时是否做连接有效性检测(业务量很⼤时候建议设置为false，减少⼀次ping的开销)
                    config.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));

                    // 构建 URI
                    String uriStr;
                    if (StringUtil.isNotBlank(password)) {
                        uriStr = String.format("redis://%s@%s:%s/%s", password, host, port, database);
                    } else {
                        uriStr = String.format("redis://%s:%s/%s", host, port, database);
                    }

                    try {
                        URI redisUri = new URI(uriStr);
                        jedisPool = new JedisPool(config, redisUri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jedisPool;
    }

    /**
     * 获取连接⽅法
     */
    public static Jedis getJedis() {
        return getInstance().getResource();
    }

    public static String get(String key) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    public static void set(String key, String value) {
        Jedis jedis = getJedis();
        jedis.setnx(key, value);
        jedis.close();
    }

    public static boolean exists(String key) {
        Jedis jedis = getJedis();
        boolean exists = jedis.exists(key);
        jedis.close();
        return exists;
    }

    public static void put(String key, String value) {
        Jedis jedis = getJedis();
        jedis.set(key, value);
        jedis.close();
    }

    public static void put(String key, Long expire, String value) {
        Jedis jedis = getJedis();
        jedis.setex(key, expire, value);
        jedis.close();
    }

    public static Long incr(String key) {
        Jedis jedis = getJedis();
        Long value = jedis.incr(key);
        jedis.close();
        return value;
    }

    public static Long incr(String key, Long expire) {
        Jedis jedis = getJedis();
        Long value = jedis.incr(key);
        jedis.expire(key, expire);
        jedis.close();
        return value;
    }
}
