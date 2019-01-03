package com.edel.messagebroker.util;

public class DBConnectionParam {

	
	private String dbIP;
	
	private String dbPort;
	
	private String dbUser;
	
	private String dbPassword;
	
	private String dbValidationQuery  = "SELECT 1";
	
	private int dbMinIdle;
	
	private int dbMaxIdle;
	
	private int dbMaxTotal;
	
	private int dbInitialSize;
	
	private long dbTimeBetweenEvictionRunsMillis;
	
	private long dbMinEvictableIdleTimeMillis;
	
	private long dbMaxWaitMills;
	
	private int removeAbandonedTimeout;

	public int getRemoveAbandonedTimeout() {
		return removeAbandonedTimeout;
	}

	public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
	}

	public String getDbIP() {
		return dbIP;
	}

	public void setDbIP(String dbIP) {
		this.dbIP = dbIP;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public int getDbMinIdle() {
		return dbMinIdle;
	}

	public void setDbMinIdle(int dbMinIdle) {
		this.dbMinIdle = dbMinIdle;
	}

	public int getDbMaxIdle() {
		return dbMaxIdle;
	}

	public void setDbMaxIdle(int dbMaxIdle) {
		this.dbMaxIdle = dbMaxIdle;
	}

	public int getDbMaxTotal() {
		return dbMaxTotal;
	}

	public void setDbMaxTotal(int dbMaxTotal) {
		this.dbMaxTotal = dbMaxTotal;
	}

	public int getDbInitialSize() {
		return dbInitialSize;
	}

	public void setDbInitialSize(int dbInitialSize) {
		this.dbInitialSize = dbInitialSize;
	}

	public long getDbTimeBetweenEvictionRunsMillis() {
		return dbTimeBetweenEvictionRunsMillis;
	}

	public void setDbTimeBetweenEvictionRunsMillis(long dbTimeBetweenEvictionRunsMillis) {
		this.dbTimeBetweenEvictionRunsMillis = dbTimeBetweenEvictionRunsMillis;
	}

	public long getDbMinEvictableIdleTimeMillis() {
		return dbMinEvictableIdleTimeMillis;
	}

	public void setDbMinEvictableIdleTimeMillis(long dbMinEvictableIdleTimeMillis) {
		this.dbMinEvictableIdleTimeMillis = dbMinEvictableIdleTimeMillis;
	}

	public long getDbMaxWaitMills() {
		return dbMaxWaitMills;
	}

	public void setDbMaxWaitMills(long dbMaxWaitMills) {
		this.dbMaxWaitMills = dbMaxWaitMills;
	}

	public String getDbValidationQuery() {
		return dbValidationQuery;
	}

	public void setDbValidationQuery(String dbValidationQuery) {
		this.dbValidationQuery = dbValidationQuery;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
}
