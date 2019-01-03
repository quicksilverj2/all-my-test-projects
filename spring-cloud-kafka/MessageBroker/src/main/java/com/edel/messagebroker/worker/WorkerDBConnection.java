package com.edel.messagebroker.worker;
import com.edel.messagebroker.util.DBConnectionParam;
import com.msf.log.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;

public class WorkerDBConnection {

    private static final Object lock = new Object();

    private static Logger log = Logger.getLogger(WorkerDBConnection.class);

    private static volatile WorkerDBConnection instance = null;

    private BasicDataSource emt ;

    public static WorkerDBConnection getInstance(){
        WorkerDBConnection r = instance;
        if(r==null){
            synchronized (lock) {
                r = instance;
                if(r==null){
                    instance = r = new WorkerDBConnection();
                }
            }
        }
        return r;
    }

    private WorkerDBConnection(){
        log.debug("Worker DB COnnection INitialization");
    }

    public void loadEmtSettings(DBConnectionParam dbConnectionParam){
        emt = new BasicDataSource();
        emt.setDriverClassName("org.postgresql.Driver");
        emt.setUrl(dbConnectionParam.getDbIP());
        emt.setUsername(dbConnectionParam.getDbUser());
        emt.setPassword(dbConnectionParam.getDbPassword());
        emt.setMaxIdle(dbConnectionParam.getDbMaxIdle());
        emt.setMinIdle(dbConnectionParam.getDbMinIdle());
        emt.setInitialSize(dbConnectionParam.getDbInitialSize());
        emt.setMaxTotal(dbConnectionParam.getDbMaxTotal());
        emt.setTimeBetweenEvictionRunsMillis(dbConnectionParam.getDbTimeBetweenEvictionRunsMillis());
        emt.setMinEvictableIdleTimeMillis(dbConnectionParam.getDbMinEvictableIdleTimeMillis());
        emt.setValidationQuery(dbConnectionParam.getDbValidationQuery());
        emt.setMaxWaitMillis(dbConnectionParam.getDbMaxWaitMills());
        emt.setTestWhileIdle(true);
        emt.setLogAbandoned(true);
        emt.setRemoveAbandonedOnMaintenance(true);
        emt.setRemoveAbandonedTimeout(dbConnectionParam.getRemoveAbandonedTimeout());
    }

    public Connection getEmtConnetion(){
        Connection connection = null;
        try{
            long startTime = System.currentTimeMillis();
            connection = emt.getConnection();
            log.debug("emt conn time taken " + (System.currentTimeMillis()-startTime));
        }catch(Exception e){
            log.error("Error " + e.getMessage());
        }
        return connection;
    }

    public void closeConnections(){
        if(emt!=null){
            try{
                emt.close();
            }catch (Exception e){
                log.error("Error in closing connections ",e);
            }
        }
    }
}