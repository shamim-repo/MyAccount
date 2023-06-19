package com.telentandtech.myaccount.DocumentUtils;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListToXlsxConverter {

    public static boolean convertToXlsx(List<String[]> dataList, String filePath) {
        try {
            // Create a new workbook with settings
            WorkbookSettings settings = new WorkbookSettings();
            settings.setSuppressWarnings(true);
            WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath), settings);

            // Create a new sheet
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            // Insert data into the sheet
            for (int i = 0; i < dataList.size(); i++) {
                String[] row = dataList.get(i);
                for (int j = 0; j < row.length; j++) {
                    addCell(sheet, j, i, row[j]);
                }
            }

            // Write and close the workbook
            workbook.write();
            workbook.close();
            return true;
        } catch (IOException | WriteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void addCell(WritableSheet sheet, int column, int row, String data)
            throws WriteException {
        // Create a cell format
        WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setWrap(true);

        // Create a cell and add it to the sheet
        Label label = new Label(column, row, data, cellFormat);
        sheet.addCell(label);
    }
}
