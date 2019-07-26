package com.persistance.Configuration;

import org.hibernate.dialect.MyISAMStorageEngine;
import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.MySQLStorageEngine;

public class MSLightweightHibernateDialect extends MySQL57Dialect {

	@Override
	protected MySQLStorageEngine getDefaultMySQLStorageEngine() {
		return MyISAMStorageEngine.INSTANCE;
	}
}
