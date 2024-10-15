package com.arwall.nosrecettes.importInDb;

import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class OpenCsvUtil {

    public static List<String[]> readLineByLine(Path filePath) throws Exception {
        List<String[]> list = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(filePath)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    list.add(line);
                }
            }
        }
        return list;
    }

    public static void writeAllLines(List<String[]> lines, String fileName) throws Exception {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeAll(lines);
        }
    }
}
