/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 *
 * @author berni3
 */
@RequestScoped
public class ConnectionProducer {

    private static final Logger logger = Logger.getLogger(ConnectionProducer.class.getName());

    @Inject
    private PoolCreator poolCreator;

    @Produces
    @RequestScoped
    Connection create() throws SQLException {
        final DataSource ds = poolCreator.retrieve();
        final Connection connection = ds.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    void close(@Disposes Connection connection) throws SQLException {
        if (!connection.isClosed()) {
            try {
                final SQLWarning firstSqlWarning = connection.getWarnings();
                if (firstSqlWarning == null) {
                    connection.commit();
                } else {
                    for (SQLWarning sqlWarning = firstSqlWarning;
                            sqlWarning != null;
                            sqlWarning = sqlWarning.getNextWarning()) {
                        logger.log(Level.WARNING, "sqlWarning", sqlWarning);
                    }
                    connection.rollback();
                }
            } finally {
                connection.close();
            }
        }
    }

    static class SQLWarningIterator implements Iterator<SQLWarning> {

        Supplier<SQLWarning> supp;

        SQLWarningIterator(Supplier<SQLWarning> supp) {
            this.supp = supp;
        }

        @Override
        public boolean hasNext() {
            return supp.get() != null;
        }

        @Override
        public SQLWarning next() {
            final SQLWarning result = supp.get();
            if (result != null) {
                supp = () -> result.getNextWarning();
            } else {
                supp = () -> null;
            }
            return result;
        }

    }
}
