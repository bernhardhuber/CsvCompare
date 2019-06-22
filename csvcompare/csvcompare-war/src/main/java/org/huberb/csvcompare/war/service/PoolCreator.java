/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class PoolCreator {

    private JdbcConnectionPool cp;

    @PostConstruct
    public void setUp() {
        this.cp = JdbcConnectionPool.create("jdbc:h2:~/CsvCompare1", "sa", "sa");
    }

    @PreDestroy
    public void tearDown() {
        this.cp.dispose();
    }

    JdbcConnectionPool retrieve() {
        return cp;
    }
}
