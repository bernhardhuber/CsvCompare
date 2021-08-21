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
import java.util.function.Supplier;
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
public class CsvQuery {

    private final String sql_1_format = "SELECT * FROM %s";

    @Inject
    private Connection con;

    public List<String> queryData(String tn) throws SQLException, IOException {
        final List<String> result = new ArrayList<>();
        final String sql_1_formatted = String.format(sql_1_format, tn);
        //String sql_1 = "SELECT * FROM " + tn;
        try (PreparedStatement ps = con.prepareStatement(sql_1_formatted)) {
            try (ResultSet rs = ps.executeQuery()) {
                try (StringWriter sw = new StringWriter()) {
                    new Csv().write(sw, rs);
                    sw.flush();

                    result.add(sw.toString());
                }
            }
        }
        return result;
    }

static class H2CsvTools {
    static class QueryTemplate {

        private String sql;
        private Connection con;

        public QueryTemplate sql(String sql) {
            this.sql = sql;
            return this;
        }

        public QueryTemplate connection(Connection connection) {
            this.con = connection;
            return this;
        }

        public String execute() throws SQLException, IOException {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                setParameters(ps);
                try (ResultSet rs = ps.executeQuery()) {
                    try (StringWriter sw = new StringWriter()) {
                        new Csv().write(sw, rs);
                        sw.flush();

                        return sw.toString();
                    }
                }
            }

        }

        protected void setParameters(PreparedStatement ps) {
        }

    }

    public static class QueryTemplateException extends RuntimeException {

        public QueryTemplateException() {
        }

        public QueryTemplateException(String string) {
            super(string);
        }

        public QueryTemplateException(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }

    }

    public static class QueryTemplateSupplier implements Supplier<String> {

        private final QueryTemplate qt;

        public QueryTemplateSupplier(QueryTemplate qt) {
            this.qt = qt;
        }

        @Override
        public String get() {
            try {
                final String result = qt.execute();
                return result;
            } catch (SQLException ex) {
                throw new QueryTemplateException("execute", ex);
            } catch (IOException ex) {
                throw new QueryTemplateException("execute", ex);
            }
        }
    }
}
}
