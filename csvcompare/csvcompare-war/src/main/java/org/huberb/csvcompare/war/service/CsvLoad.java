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

    private final String sql_1_format = "DROP TABLE IF EXISTS %s";
    private final String sql2_format = "CREATE TABLE %s AS SELECT * FROM CSVREAD('%s', %s, %s)";

    @Inject
    private Connection con;

    public void loadCsv(File f, String tn) throws SQLException {
        final String sql_1_formatted = String.format(sql_1_format, tn);
        //final String sql_1 = "DROP TABLE IF EXISTS " + tn;
        try (PreparedStatement s = con.prepareStatement(sql_1_formatted)) {
            s.execute();
        }

        final String normalizedFileName = f.getAbsoluteFile().toURI().toString();
        final String sql_2_formatted = String.format(sql2_format, tn, normalizedFileName, Constants.CSVREAD_COLUMNSSTRING, Constants.CSVREAD_CSVOPTIONS);
        //final String sql_2 = "CREATE TABLE " + tn + " AS "
        //        + "SELECT * FROM CSVREAD(" + "'" + normalizedFileName + "'" + ","
        //        + Constants.CSVREAD_COLUMNSSTRING + ", "
        //        + Constants.CSVREAD_CSVOPTIONS
        //        + ")";

        try (PreparedStatement s = con.prepareStatement(sql_2_formatted)) {
            s.execute();
        }
    }
}
