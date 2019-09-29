/*
 * Copyright (c) 2019. Matthias Keisse
 * All Rights Reserved.
 * Programmed for Java SE 1.7
 */
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
    public static final String REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|(\r\n|\n)";
    public static final String CSV_EXTENSION = ".csv";
    public static final String DEFAULT_INPUT_FOLDER = "input";
    public static final String OUTPUT_FOLDER = "/pdf";
    public static final float FONT_SIZE = 5f;
    private final File inputFolder;

    public CsvToPdfConverter() {
        inputFolder = new File("");
        new File(DEFAULT_INPUT_FOLDER + OUTPUT_FOLDER).mkdir();
    }

    public CsvToPdfConverter(String folderName) {
        inputFolder = new File(folderName);
        new File(folderName + OUTPUT_FOLDER).mkdir();
    }

    public void convertFilesToPdf() throws IOException {
        for (Map.Entry<String, Table> tableSet : createMappedTablesFromFiles().entrySet()) {
            try (PdfWriter writer = new PdfWriter(inputFolder.getName() + OUTPUT_FOLDER + "/" + tableSet.getKey() + ".pdf");
                 PdfDocument pdfDocument = new PdfDocument(writer);
                 Document document = new Document(pdfDocument)) {
                document.add(tableSet.getValue());
            }
        }
    }

    public Map<String, Table> createMappedTablesFromFiles() throws IOException {
        Map<String, Table> tableMap = new HashMap<>();
        if (inputFolder.isDirectory() && inputFolder.listFiles() != null) {
            for (File inputFile : inputFolder.listFiles()) {
                if (!inputFile.isDirectory()) {
                    String ext = inputFile.getName();
                    ext = ext.substring(ext.lastIndexOf('.'));

                    if (ext.equals(CSV_EXTENSION)) {
                        String fileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
                        tableMap.put(fileName, processCsvFileToPdfTable(inputFile));
                    }
                }
            }
        }
        return tableMap;
    }

    public Table processCsvFileToPdfTable(File inputFile) throws IOException {
        Queue<String> dataQueue = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileReader(inputFile))) {
            while (scanner.hasNext())
                dataQueue.offer(scanner.nextLine());
        }
        String[] headerNames = dataQueue.poll().split(",");
        Table pdfTable = new Table(headerNames.length).setHorizontalAlignment(HorizontalAlignment.CENTER);

        for (String str : headerNames)
            pdfTable.addCell(parseInputToCell(str));

        return fillDataToTable(pdfTable, dataQueue);
    }

    private Table fillDataToTable(Table pdfTable, Queue<String> dataQueue) {
        while (!dataQueue.isEmpty()) {
            String row = dataQueue.poll();
            String[] tableRow = row.split(REGEX);

            for (String str : tableRow)
                pdfTable.addCell(parseInputToCell(str));
        }
        return pdfTable;
    }

    private Cell parseInputToCell(String str) {
        str = str.trim();
        str = str.startsWith("\"") ? str.substring(1, str.length() - 1) : str;
        return new Cell().add(new Paragraph(new Text(str).setFontSize(FONT_SIZE)));
    }
}