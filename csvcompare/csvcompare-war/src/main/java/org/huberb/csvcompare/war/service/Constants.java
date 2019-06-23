/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.csvcompare.war.service;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author berni3
 */
public class Constants {

    public final static String TN_CSV_1 = "CSV_1";
    public final static String TN_CSV_2 = "CSV_2";

    public final static String COLUMNS = "VON,BETREFF";

    public final static String[] splittedColumns() {
        String[] columnsSplitted = StringUtils.split(COLUMNS, ',');
        return columnsSplitted;
    }
}
