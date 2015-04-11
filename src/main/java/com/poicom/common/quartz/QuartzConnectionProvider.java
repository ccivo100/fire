package com.poicom.common.quartz;

import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.utils.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;

public class QuartzConnectionProvider implements ConnectionProvider {

	private Logger logger = LoggerFactory
			.getLogger(QuartzConnectionProvider.class);
	private Config config;

	@Override
	public Connection getConnection() throws SQLException {
		return config.getConnection();
	}

	@Override
	public void shutdown() throws SQLException {
		if (QuartzPlugin.dsAlone)
			config.getConnection().close();
		else
			logger.info("quartz datasource is not alone,so you cant close it");
	}

	@Override
	public void initialize() throws SQLException {
		config = DbKit.getConfig(QuartzPlugin.dsName);
		if (config == null) {
			throw new RuntimeException("quartz datasource  not found");
		}
	}

}
