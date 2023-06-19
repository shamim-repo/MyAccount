package com.telentandtech.myaccount.DocumentUtils;


import android.os.Environment;
import android.util.Log;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CSVtoPDFConverter {
    public static boolean convertCSVtoPDF(String csvFilePath, String pdfFileName, int columnCount) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File dir=new File(directory.getAbsolutePath()+"/MyAccount");
            if (!dir.exists())
                dir.mkdir();
            File pdfFile = new File(dir, pdfFileName);

            PdfWriter writer = new PdfWriter(pdfFile.getAbsolutePath());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Table table = new Table(columnCount).useAllAvailableWidth();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                for (String value : data) {
                    table.addCell(new Paragraph(value.replace("\"",""))
                            .setTextAlignment(TextAlignment.CENTER));
                }
            }
            document.add(table);
            document.close();

            Log.d("PDF Converter", "PDF saved to: " + pdfFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
