/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

/**
 *
 * @author berni3
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class CsvLoad {

    @Inject
    private Connection con;

    public void loadCsv(File f, String tn) throws SQLException {
        String sql_1 = "DROP TABLE IF EXISTS " + tn;
        try (PreparedStatement s = con.prepareStatement(sql_1)) {
            s.execute();
        }

        final String normalizedFileName = f.getAbsoluteFile().toURI().toString();
        final String sql_2 = "CREATE TABLE " + tn + " AS "
                + "SELECT * FROM CSVREAD(" + "'" + normalizedFileName + "'" + ","
                + "null,"
                + "'charset=UTF-8 fieldSeparator=|')";

        try (PreparedStatement s = con.prepareStatement(sql_2)) {
            s.execute();
        }
    }
}
