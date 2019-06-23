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
import org.huberb.csvcompare.war.service.CsvCompare;

/**
 *
 * @author berni3
 */
@RequestScoped
@Named("csvCompareController")
public class CsvCompareController {

    private List<String> data;

    public List<String> getData() {
        return data;
    }
    @Inject
    private CsvCompare csvCompare;

    //---
    @PostConstruct
    public void onCreate() {
        this.data = new ArrayList<>();
    }

    //---
    public void compareAction() throws SQLException, IOException {
        String tn1 = Constants.TN_CSV_1;
        String tn2 = Constants.TN_CSV_2;

        data.clear();
        data.addAll(csvCompare.csvCompareIdentical(tn1, tn2));
        data.addAll(csvCompare.csvCompare(tn1, tn2));
    }
}
