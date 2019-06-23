/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.controller;

import org.huberb.csvcompare.war.service.Constants;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.huberb.csvcompare.war.service.CsvStatistic;

/**
 *
 * @author berni3
 */
@RequestScoped
@Named("csvStatisticController")
public class CsvStatisticController {

    private List<String> data;

    public List<String> getData() {
        return data;
    }
    @Inject
    private CsvStatistic csvStatistic;

    //---
    @PostConstruct
    public void onCreate() {
        this.data = new ArrayList<>();
    }

    //---
    public void queryAction() throws SQLException, IOException {
        String tn1 = Constants.TN_CSV_1;
        String tn2 = Constants.TN_CSV_2;

        data.clear();
        data.addAll(csvStatistic.queryData(tn1));
        data.addAll(csvStatistic.queryData(tn2));
    }
}
