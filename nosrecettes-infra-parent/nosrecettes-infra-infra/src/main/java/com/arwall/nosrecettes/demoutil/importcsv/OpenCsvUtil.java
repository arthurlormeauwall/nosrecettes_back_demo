package com.arwall.nosrecettes.demoutil.importcsv;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class OpenCsvUtil {

    public static List<String[]> readLineByLine(String filePath) throws Exception {
        List<String[]> list = new ArrayList<>();
        var file = new InputStreamReader(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(filePath));
        try (Reader reader = file) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    list.add(line);
                }
            }
        }
        return list;
    }
}
