package com.edel.messagebroker.conductor;

import com.edel.messagebroker.util.DBConnectionParam;
import com.edel.messagebroker.util.MBHelper;
import com.msf.log.Logger;
import com.msf.network.RedisConfigParameters;
import com.msf.network.RedisConnection;
import com.msf.network.RedisPoolConfig;
import com.msf.network.StandaloneCacheService;
import lombok.Getter;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.ArrayList;
import java.util.Set;

public class ConductorConfig implements RedisConfigParameters {

    private static Logger log = Logger.getLogger(ConductorConfig.class);
    private static volatile ConductorConfig instance;
    private static final Object lock = new Object();

    private String ipBind;

    private int portBind;

    private int queueLength;

    public static ConductorConfig getInstance(){
        ConductorConfig r = instance;
        if(r==null){
            synchronized (lock){
                r = instance;
                if(r==null){
                    instance = r = new ConductorConfig();
                }
            }
        }
        return r;
    }

    public static String getIpBind(){
        return getInstance().ipBind;
    }

    public static int getPortBind(){
        return getInstance().portBind;
    }

    public static int getQueueLength(){
        return getInstance().queueLength;
    }

    private ConductorConfig(){
        log.debug("conductor Config Initialized");
    }

    private int nseMaxSize;

    private int cdsMaxSize;

    private DBConnectionParam emtConnParam = new DBConnectionParam();

    @Getter
    private int reconnectSubscriber;

    @Getter
    private String redisChannelBaseString;

    private RedisPoolConfig redisAdminConfig;

    private RedisConnection redisAdminConn;

    public static DBConnectionParam getEmtConnParam(){
       return getInstance().emtConnParam;
    }

    public static int getNseMaxSize(){
        return getInstance().nseMaxSize;
    }

    public static int getCdsMaxSize(){
        return getInstance().cdsMaxSize;
    }

    public void loadDBConnection(PropertiesConfiguration config){
        MBHelper.loadEmtConnections(config,emtConnParam);
    }

    public void loadFile(String fileName) throws Exception {
        PropertiesConfiguration config = new PropertiesConfiguration(fileName);
        loadClientsSize(config);
        loadDBConnection(config);
        loadAdminConfig(config);
        loadServerDetails(config);
    }

    private void loadServerDetails(PropertiesConfiguration config){
        ipBind = config.getString("socket.server.ip.bind");
        portBind = config.getInt("socket.server.port.bind");
        queueLength = config.getInt("socket.server.queue.length");
    }

    private void loadClientsSize(PropertiesConfiguration config){
        nseMaxSize = config.getInt("config.conductor.nse.maxpartition");
        log.debug("NseMax Size "+nseMaxSize);
        cdsMaxSize = config.getInt("config.conductor.cds.maxpartition");
        log.debug("cds max size "+cdsMaxSize);
    }

    private void loadAdminConfig(PropertiesConfiguration config){

        String hostPort = config.getString("jedis.admin.instance");

        String[] hostPortA = hostPort.split(":");

        redisAdminConfig = RedisPoolConfig.builder().maxIdle(config.getInt("jedis.admin.maxIdle"))
                .maxLongWait(config.getLong("jedis.admin.maxWait"))
                .minIdle(config.getInt("jedis.admin.minIdle"))
                .maxTotal(config.getInt("jedis.admin.maxTotal")).build();

        redisAdminConn = new RedisConnection(hostPortA[0],Integer.parseInt(hostPortA[1]));

        StandaloneCacheService.getInstance().loadFiles(this);

        redisChannelBaseString = config.getString("config.redis.admin.base");

        reconnectSubscriber = config.getInt("config.redis.admin.reconnect");
    }

    @Override
    public long getRedisClusterMaxWaitMillis() {
        return 0;
    }

    @Override
    public int getRedisClusterMaxIdle() {
        return 0;
    }

    @Override
    public int getRedisClusterMinIdle() {
        return 0;
    }

    @Override
    public int getRedisClusterMaxTotal() {
        return 0;
    }

    @Override
    public ArrayList<RedisConnection> getRedisClusterConnections() {
        return null;
    }

    @Override
    public RedisConnection getRedisStandaloneConnection() {
        return redisAdminConn;
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
