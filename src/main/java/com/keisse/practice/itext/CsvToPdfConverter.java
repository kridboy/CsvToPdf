package com.keisse.practice.itext;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvToPdfConverter {
    private static final String REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|(\r\n|\n)";
    private static final String CSV_EXTENSION = ".csv";
    private static final float FONT_SIZE = 5f;
    private final File inputFolder;

    public CsvToPdfConverter(String folderName) {
        inputFolder = new File(folderName);
        new File("pdf").mkdir();
    }

    public void convertFiles() throws IOException {
        for (Map.Entry<String, Table> tableSet : createCsvTables().entrySet()) {
            try (PdfWriter writer = new PdfWriter("pdf/" + tableSet.getKey() + ".pdf");
                 PdfDocument pdfDocument = new PdfDocument(writer);
                 Document document = new Document(pdfDocument)) {
                document.add(tableSet.getValue());
            }
        }
    }

    private Map<String, Table> createCsvTables() throws IOException {
        Map<String, Table> csvMap = new HashMap<>();
        if (inputFolder.isDirectory() && inputFolder.listFiles() != null) {
            for (File inputFile : inputFolder.listFiles()) {
                String extension = inputFile.getName();
                extension = extension.substring(extension.lastIndexOf('.'));

                if (extension.equals(CSV_EXTENSION)) {
                    String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
                    csvMap.put(fileName, processDataToTable(inputFile));
                }
            }
        }
        return csvMap;
    }

    private Table processDataToTable(File inputFile) throws IOException {
        Queue<String> dataQueue = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileReader(inputFile))) {
            while (scanner.hasNext())
                dataQueue.offer(scanner.nextLine());
        }
        String[] headerNames = dataQueue.poll().split(",");
        Table pdfTable = new Table(headerNames.length).setHorizontalAlignment(HorizontalAlignment.CENTER);

        for (String str : headerNames)
            pdfTable.addCell(parseCell(str));

        return fillTable(pdfTable,dataQueue);
    }

    private Table fillTable(Table pdfTable,Queue<String> dataQueue){
        Queue<String> tableData = new LinkedList<>();
        while (!dataQueue.isEmpty()) {
            String row = dataQueue.poll();
            String[] tableRow = row.split(REGEX);

            for (String str : tableRow)
                tableData.offer(str);

            while (!tableData.isEmpty())
                pdfTable.addCell(parseCell(tableData.poll()));
        }
        return pdfTable;
    }

    private Cell parseCell(String str){
        return new Cell().add(new Paragraph(new Text(trimCellInput(str)).setFontSize(FONT_SIZE)));
    }

    private String trimCellInput(String str) {
        str = str.trim();
        return str.startsWith("\"") ? str.substring(1, str.length() - 1) : str;
    }

}