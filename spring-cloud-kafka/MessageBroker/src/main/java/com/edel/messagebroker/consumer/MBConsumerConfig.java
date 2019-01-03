package com.edel.messagebroker.consumer;

import com.msf.log.Logger;
import com.msf.network.*;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.*;

public class MBConsumerConfig implements RedisConfigParameters {

    private static Logger log = Logger.getLogger(MBConsumerConfig.class);

    private static volatile MBConsumerConfig instance;

    private static final Object lock = new Object();

    private String topic;

    private String brokers;

    private String groupID;

    private String moengageTopic;

    private String moengageBrokers;

    private String redisSpecificpattern;

    private RedisPoolConfig redisClusterPoolConfig;

    private RedisPoolConfig redisAdminConfig;

    private RedisPoolConfig redisSentinalConfig;

    private Set<String> sentinals = new HashSet<>();

    private RedisConnection redisConnection;

    private int threadPoolCount;

    private int esTimeOut;

    private int reconnectSubscriber;

    private String redisChannelBaseString;

    private ArrayList<RedisConnection> redisClusterConnections = new ArrayList<>();

    private String tradeMessage ;

    private String trdConfTitle;

    private String trdPendMsg;

    private long nseLstTm;

    private long cdsLstTm;

    public static long getNseLstTm(){
        return getInstance().nseLstTm;
    }

    public static long getCdsLstTm(){
        return getInstance().cdsLstTm;
    }

    public static String getTrdPendMsg(){
        return getInstance().trdPendMsg;
    }

    public static int getReconnectSubscriber(){
        return getInstance().reconnectSubscriber;
    }

    public static String getTrdConfTitle(){
        return getInstance().trdConfTitle;
    }

    public static String getRedisBaseString(){
        return getInstance().redisChannelBaseString;
    }

    public static String getTopic(){
        return getInstance().topic;
    }

    public static String getBrokers(){
        return getInstance().brokers;
    }

    public static String getGroupID(){
        return getInstance().groupID;
    }

    public static String getMoengageTopic(){
        return getInstance().moengageTopic;
    }

    public static String getMonegageBrokers(){
        return getInstance().moengageBrokers;
    }

    public static int getThreadPoolCount(){
        return getInstance().threadPoolCount;
    }

    public static int getESTimeOut(){
        return getInstance().esTimeOut;
    }

    public static String getRedisSpecificPattern(){
        return getInstance().redisSpecificpattern;
    }

    public static String getTradeMessage(){
        return getInstance().tradeMessage;
    }

    public static MBConsumerConfig getInstance(){
        MBConsumerConfig r = instance;
        if(r==null){
            synchronized (lock){
                r = instance;
                if(r==null){
                    instance = r = new MBConsumerConfig();
                }
            }
        }
        return r;
    }

    private MBConsumerConfig(){
        log.debug("MB Consumer Config Initialized ");
    }

    public void loadFile(String fileName) throws Exception {
        PropertiesConfiguration config = new PropertiesConfiguration(fileName);
        loadKafkaParams(config);
        loadMoengageKafkaParmas(config);
        loadClusterConfig(config);
        loadExecutorServiceSettings(config);
        loadAdminConfig(config);
        loadTradeMessage(config);
        loadSentinelConfig(config);
        loadTimeRelatedStuff(config);
    }

    private void loadTimeRelatedStuff(PropertiesConfiguration config){
        Calendar current = Calendar.getInstance();
        current.set(Calendar.MILLISECOND,0);
        String nseTime = config.getString("config.trade.redis.nse.time");
        String[] nseTimeArray = nseTime.split(":");
        current.set(Calendar.HOUR_OF_DAY,Integer.parseInt(nseTimeArray[0]));
        current.set(Calendar.MINUTE,Integer.parseInt(nseTimeArray[1]));
        current.set(Calendar.SECOND,Integer.parseInt(nseTimeArray[2]));
        nseLstTm = current.getTimeInMillis();
        log.debug("Nse last Time "+nseLstTm);
        String cdsTime = config.getString("config.trade.redis.cds.time");
        String[] cdsTimeArray = cdsTime.split(":");
        current.set(Calendar.HOUR_OF_DAY,Integer.parseInt(cdsTimeArray[0]));
        current.set(Calendar.MINUTE,Integer.parseInt(cdsTimeArray[1]));
        current.set(Calendar.SECOND,Integer.parseInt(cdsTimeArray[2]));
        cdsLstTm = current.getTimeInMillis();
        log.debug("Cds Last Time "+cdsLstTm);
    }

    private void loadKafkaParams(PropertiesConfiguration config) throws  Exception{
        topic = config.getString("config.topic.name");
        brokers = config.getString("config.kafka.brokers");
        groupID = config.getString("config.kakfka.groupid");
    }

    private void loadMoengageKafkaParmas(PropertiesConfiguration config) throws Exception{
        moengageTopic = config.getString("config.topic.moengage");
        moengageBrokers = config.getString("config.kafka.moengage.brokers");
    }

    private void loadExecutorServiceSettings(PropertiesConfiguration config) throws  Exception{
        threadPoolCount = config.getInt("config.es.threadpoolcount");
        esTimeOut = config.getInt("config.es.timeout");
    }

    private void loadAdminConfig(PropertiesConfiguration config){

        String hostPort = config.getString("jedis.admin.instance");

        String[] hostPortA = hostPort.split(":");

        redisAdminConfig = RedisPoolConfig.builder().maxIdle(config.getInt("jedis.admin.maxIdle"))
                .maxLongWait(config.getLong("jedis.admin.maxWait"))
                .minIdle(config.getInt("jedis.admin.minIdle"))
                .maxTotal(config.getInt("jedis.admin.maxTotal")).build();

        redisConnection = new RedisConnection(hostPortA[0],Integer.parseInt(hostPortA[1]));

        StandaloneCacheService.getInstance().loadFiles(this);

        redisChannelBaseString = config.getString("config.redis.admin.base");

        reconnectSubscriber = config.getInt("config.redis.admin.reconnect");
    }


    private void loadTradeMessage(PropertiesConfiguration config){
        tradeMessage = config.getString("config.trade.messsage");
        log.debug("Trade Message "+tradeMessage);
        trdConfTitle = config.getString("config.trade.title");
        trdPendMsg = config.getString("config.trade.pending.message");//
    }


    private void loadSentinelConfig(PropertiesConfiguration config){
        redisSentinalConfig = RedisPoolConfig.builder().maxIdle(config.getInt("jedis.sentinel.maxIdle"))
                .minIdle(config.getInt("jedis.sentinel.minIdle"))
                .maxLongWait(config.getLong("jedis.sentinel.maxWait"))
                .maxTotal(config.getInt("jedis.sentinel.maxTotal")).build();

        List<Object> jedisSentinels = config.getList("jedis.sentinel.instance");

        if (jedisSentinels.size() == 0) {
            log.error("No redis cluster instances to connect to ");
            System.exit(-1);
        } else {
            for (int i = 0; i < jedisSentinels.size(); i++) {
                sentinals.add((String)jedisSentinels.get(i));
                log.debug("sentinel "+(String) jedisSentinels.get(i));
                //redisClusterConnections.(new HostAndPort(jedisHostPort[0], Integer.parseInt(jedisHostPort[1])));
            }
        }

        QuoteSentinelCache.getInstance().loadFiles(this);
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
        return redisConnection;
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
        return sentinals;
    }

    @Override
    public RedisPoolConfig getRedisQuoteSentinelConfig() {
        return redisSentinalConfig;
    }
}
