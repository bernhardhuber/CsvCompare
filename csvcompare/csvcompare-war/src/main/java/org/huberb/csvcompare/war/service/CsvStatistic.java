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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.h2.tools.Csv;

/**
 *
 * @author berni3
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class CsvStatistic {

    @Inject
    private Connection con;

    public List<String> queryData(String tn) throws SQLException, IOException {
        final List<String> result = new ArrayList<>();
        final List<String> sqlQueryList = new ArrayList<>();
        sqlQueryList.add("SELECT COUNT(*) AS COUNT FROM " + tn);
        sqlQueryList.addAll(buildCountByColumns(tn));
        for (String sql : sqlQueryList) {
            result.addAll(executeQuery(sql));
        }
        return result;
    }

    /**
     * build group-by for each column
     *
     * @param tn
     * @return
     */
    List<String> buildCountByColumns(String tn) {
        String[] columns = Constants.splittedColumns();

        List<String> result = new ArrayList<>();

        for (int i = 0; i < columns.length; i += 1) {
            StringBuilder sb = new StringBuilder();
            String column = columns[i];
            // "SELECT COUNT(*) AS COUNT, VON FROM " + tn + " GROUP BY VON " + " ORDER BY 1 DESC",
            sb.append("SELECT COUNT(*) AS COUNT, ").append(column).
                    append(" FROM ").append(tn).append(" ").
                    append(" GROUP BY ").append(column).
                    append(" ORDER BY 1 DESC ");
            result.add(sb.toString());
        }
        return result;
    }

    List<String> executeQuery(String sql_1) throws SQLException, IOException {
        final List<String> result = new ArrayList<>();
        try (PreparedStatement s = con.prepareStatement(sql_1)) {
            try (ResultSet rs = s.executeQuery()) {

                try (StringWriter sw = new StringWriter()) {
                    new Csv().write(sw, rs);
                    sw.flush();

                    result.add(sw.toString());
                }
            }
        }
        return result;
    }
}
