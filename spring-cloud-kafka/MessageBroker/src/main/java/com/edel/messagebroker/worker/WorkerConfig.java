package com.edel.messagebroker.worker;

import com.edel.messagebroker.util.DBConnectionParam;
import com.edel.messagebroker.util.MBHelper;
import com.msf.log.Logger;
import com.msf.network.*;
import lombok.Getter;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorkerConfig implements RedisConfigParameters {

    private static Logger log = Logger.getLogger(WorkerConfig.class);

    private static volatile WorkerConfig instance;

    private static final Object lock = new Object();

    public static WorkerConfig getInstance(){
        WorkerConfig r = instance;
        if(r==null){
            synchronized (lock){
                r =instance;
                if(r==null){
                    instance = r = new WorkerConfig();
                }
            }
        }
        return r;
    }

    private WorkerConfig(){
        log.debug("Worker Config Initialized ");
    }

    @Getter
    private String conIp;

    @Getter
    private int conPort;

    @Getter
    private DBConnectionParam emtConnParam =new DBConnectionParam();

    //KAFKA
    @Getter
    private String moengageTopic;

    @Getter
    private String moengageBrokers;

    // REDIS CLUSTER
    @Getter
    private String redisSpecificpattern;

    private RedisPoolConfig redisClusterPoolConfig;

    private ArrayList<RedisConnection> redisClusterConnections = new ArrayList<>();

    // REDIS PUB SUB
    @Getter
    private RedisPoolConfig redisAdminConfig;

    @Getter
    private String redisChannelBaseString;

    @Getter
    private int reconnectSubscriber;

    private RedisConnection redisAdminConnection;

    @Getter
    private int threadPoolCount;

    @Getter
    private int esTimeOut;

    public void loadFile(String fileName) throws Exception {
        PropertiesConfiguration config = new PropertiesConfiguration(fileName);
        loadServerDetails(config);
        loadMoengageKafkaParmas(config);
        loadDBServerProperties(config);
        loadClusterConfig(config);
        loadAdminConfig(config);
        loadExecutorServiceSettings(config);
    }

    private void loadServerDetails(PropertiesConfiguration config){
        conIp = config.getString("socket.server.ip.bind");
        conPort = config.getInt("socket.server.port.bind");
    }

    private void loadDBServerProperties(PropertiesConfiguration config){
        MBHelper.loadEmtConnections(config,emtConnParam);
    }

    private void loadMoengageKafkaParmas(PropertiesConfiguration config) throws Exception{
        moengageTopic = config.getString("config.topic.moengage");
        moengageBrokers = config.getString("config.kafka.moengage.brokers");
    }

    private void loadExecutorServiceSettings(PropertiesConfiguration config) throws  Exception{
        threadPoolCount = config.getInt("config.es.threadpoolcount");
        esTimeOut = config.getInt("config.es.timeout");
    }

    private void loadClusterConfig(PropertiesConfiguration config){

        List<Object> jedisClusterInstances = config.getList("jedis.cluster.instance");

        if (jedisClusterInstances.size() == 0) {
            log.error("No redis cluster instances to connect to ");
            System.exit(-1);
        } else {
            for (int i = 0; i < jedisClusterInstances.size(); i++) {
                String[] jedisHostPort = ((String) jedisClusterInstances.get(i)).split(":");
                //redisClusterConnections.(new HostAndPort(jedisHostPort[0], Integer.parseInt(jedisHostPort[1])));
                redisClusterConnections.add(new RedisConnection(jedisHostPort[0], Integer.parseInt(jedisHostPort[1])));

            }
        }

        redisClusterPoolConfig = RedisPoolConfig.builder().maxIdle(config.getInt("jedis.cluster.maxIdle"))
                .maxLongWait(config.getLong("jedis.cluster.maxWait"))
                .minIdle(config.getInt("jedis.cluster.minIdle"))
                .maxTotal(config.getInt("jedis.cluster.maxTotal")).build();

        CacheService.getInstance().loadFiles(this);

        redisSpecificpattern = config.getString("mw.redis.specificpattern");
    }

    private void loadAdminConfig(PropertiesConfiguration config){

        String hostPort = config.getString("jedis.admin.instance");

        String[] hostPortA = hostPort.split(":");

        redisAdminConfig = RedisPoolConfig.builder().maxIdle(config.getInt("jedis.admin.maxIdle"))
                .maxLongWait(config.getLong("jedis.admin.maxWait"))
                .minIdle(config.getInt("jedis.admin.minIdle"))
                .maxTotal(config.getInt("jedis.admin.maxTotal")).build();

        redisAdminConnection = new RedisConnection(hostPortA[0],Integer.parseInt(hostPortA[1]));

        StandaloneCacheService.getInstance().loadFiles(this);

        redisChannelBaseString = config.getString("config.redis.admin.base");

        reconnectSubscriber = config.getInt("config.redis.admin.reconnect");
    }


    @Override
    public long getRedisClusterMaxWaitMillis() {
        return redisClusterPoolConfig.getMaxLongWait();
    }

    @Override
    public int getRedisClusterMaxIdle() {
        return redisClusterPoolConfig.getMaxIdle();
    }

    @Override
    public int getRedisClusterMinIdle() {
        return redisClusterPoolConfig.getMinIdle();
    }

    @Override
    public int getRedisClusterMaxTotal() {
        return redisClusterPoolConfig.getMaxTotal();
    }

    @Override
    public ArrayList<RedisConnection> getRedisClusterConnections() {
        return redisClusterConnections;
    }

    @Override
    public RedisConnection getRedisStandaloneConnection() {
        return redisAdminConnection;
    }

    @Override
    public int getRedisStandaloneMaxIdle() {
        return redisAdminConfig.getMaxIdle();
    }

    @Override
    public int getRedisStandaloneMinIdle() {
        return redisAdminConfig.getMinIdle();
    }

    @Override
    public long getRedisStandaloneMaxWaitMillis() {
        return redisAdminConfig.getMaxLongWait();
    }

    @Override
    public int getRedisStandaloneMaxTotal() {
        return redisAdminConfig.getMaxTotal();
    }

    @Override
    public RedisConnection getRedisQuoteConnection() {
        return null;
    }

    @Override
    public RedisPoolConfig getRedisQuoteCacheConfig() {
        return null;
    }

    @Override
    public Set<String> getRedisQuoteSentinals() {
        return null;
    }

    @Override
    public RedisPoolConfig getRedisQuoteSentinelConfig() {
        return null;
    }
}
