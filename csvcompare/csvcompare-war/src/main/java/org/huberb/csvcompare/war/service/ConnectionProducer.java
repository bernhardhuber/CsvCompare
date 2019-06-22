/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.service;

import java.sql.Connection;
import java.sql.SQLException;
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

    @Inject
    private PoolCreator poolCreator;

    @Produces
    @RequestScoped
    Connection create() throws SQLException {
        final DataSource ds = poolCreator.retrieve();
        final Connection connection = ds.getConnection();
        return connection;
    }

    void close(@Disposes Connection connection) throws SQLException {
        connection.close();
    }
}
