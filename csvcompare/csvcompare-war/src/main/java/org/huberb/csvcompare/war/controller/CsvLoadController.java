/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.controller;

import org.huberb.csvcompare.war.service.Constants;
import java.io.IOException;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.huberb.csvcompare.war.service.CsvLoad;
import org.huberb.csvcompare.war.service.FileManager;

/**
 *
 * @author berni3
 */
@RequestScoped
@Named("csvLoadController")
public class CsvLoadController {

    private String csv1Data;
    private String csv2Data;
    //---
    @Inject
    private FileManager fm;
    @Inject
    private CsvLoad csvLoad;

    public String getCsv1Data() {
        return csv1Data;
    }

    public void setCsv1Data(String csv1Data) {
        this.csv1Data = csv1Data;
    }

    public String getCsv2Data() {
        return csv2Data;
    }

    public void setCsv2Data(String csv2Data) {
        this.csv2Data = csv2Data;
    }

    //
    public void loadAction() throws IOException, SQLException {
        {
            FileManager.FileElement fe1 = fm.creaeFileElement("csv1");
            try {
                fm.writeToFileElement(fe1, csv1Data);
                String tn1 = Constants.TN_CSV_1;
                csvLoad.loadCsv(fe1.getF(), tn1);
            } finally {
                fm.deleteFileElement(fe1);
            }
        }
        {
            FileManager.FileElement fe2 = fm.creaeFileElement("csv2");
            try {
                fm.writeToFileElement(fe2, csv2Data);
                String tn2 = Constants.TN_CSV_2;
                csvLoad.loadCsv(fe2.getF(), tn2);
            } finally {
                fm.deleteFileElement(fe2);
            }
        }
    }
}
