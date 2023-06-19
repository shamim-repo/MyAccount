package com.telentandtech.myaccount.DocumentUtils;

import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtils {
    public static boolean writeListToCSV(List<String[]> data, String filePath) {
        File directory = new File(filePath);

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(data);
            Log.d("CSVUtils", "CSV saved to: " + filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
