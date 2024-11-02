package com.arwall.nosrecettes.demoutil.importcsv;

import com.opencsv.CSVReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class OpenCsvUtil {

    public static List<String[]> readLineByLine(String filePath) throws Exception {
        List<String[]> list = new ArrayList<>();
        Resource resource = new ClassPathResource(filePath);
        FileInputStream file = new FileInputStream(resource.getFile());
        try (Reader reader = new InputStreamReader(file)) {
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
