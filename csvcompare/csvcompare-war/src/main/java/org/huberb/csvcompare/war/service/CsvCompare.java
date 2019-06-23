/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.service;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author berni3
 */
@Stateless
public class CsvCompare {

    @Inject
    private Connection con;

    public List<String> csvCompare(String tn1, String tn2) throws SQLException, IOException {
        List<String> result = new ArrayList<>();
        String sqlL
                = "SELECT 'L' AS JOINMODE, CSV1.*, CSV2.* "
                + "FROM " + tn1 + " CSV1 "
                + "LEFT JOIN " + tn2 + " CSV2  "
                + buildOnClause()
                //+ "ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF "
                + "WHERE CSV2.VON IS NULL "
                + "ORDER BY 2";
        result.addAll(executeQuery(sqlL));

        String sqlR = "SELECT 'R' AS JOINMODE, CSV1.*, CSV2.* "
                + "FROM " + tn1 + " CSV1 "
                + "RIGHT JOIN " + tn2 + " CSV2 "
                + buildOnClause()
                //+ "ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF "
                + "WHERE CSV1.VON IS NULL "
                + "ORDER BY 2";
        result.addAll(executeQuery(sqlR));
        return result;
    }

    public List<String> csvCompareIdentical(String tn1, String tn2) throws SQLException, IOException {
        List<String> result = new ArrayList<>();

        String sql = "SELECT 'I' AS JOINMODE, CSV1.*, CSV2.* "
                + "FROM " + tn1 + " CSV1 "
                + "INNER JOIN " + tn2 + " CSV2  "
                + buildOnClause()
                //+ "ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF "
                + "ORDER BY 2";
        result.addAll(executeQuery(sql));
        return result;
    }

    //---
    protected List<String> executeQuery(String sql) throws SQLException, IOException {
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            try (StringWriter sw = new StringWriter()) {
                try (ResultSet rs = ps.executeQuery()) {
                    new org.h2.tools.Csv().write(sw, rs);
                    sw.flush();

                    result.add(sw.toString());
                }
            }
        }
        return result;
    }

    // "ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF "
    String buildOnClause() {
        String[] columns = Constants.splittedColumns();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < columns.length; i += 1) {
            String column = columns[i];
            if (i == 0) {
                sb.append(" ON ");
            } else {
                sb.append(" AND ");
            }
            sb.append("CSV1.").append(column).append(" = ").append("CSV2.").append(column).append(" ");
        }
        return sb.toString();
    }
}
