/*
 * Copyright (c) 2019. $name
 * All Rights Reserved.
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LargeCsvToPdfConverter {
    private static final String CSV_EXTENSION = ".csv";
    private static final float FONT_SIZE = 5f;
    public static final String DEFAULT_INPUT_FOLDER = "input";
    public static final String OUTPUT_FOLDER = "/pdf";
    private final File inputFolder;

    public LargeCsvToPdfConverter() {
        inputFolder = new File("");
        new File(DEFAULT_INPUT_FOLDER + OUTPUT_FOLDER).mkdir();
    }

    public LargeCsvToPdfConverter(String folderName) {
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
                        tableMap.put(fileName, processCsvFileToTable(inputFile));
                    }
                }
            }
        }
        return tableMap;
    }

    public Table processCsvFileToTable(File inputFile) throws IOException {
        try (Reader reader = new FileReader(inputFile)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            Table pdfTable = new Table(records.iterator().next().size()).setHorizontalAlignment(HorizontalAlignment.CENTER);

            for (CSVRecord record : records) {
                Iterator<String> iterator = record.iterator();
                while (iterator.hasNext())
                    pdfTable.addCell(parseInputToCell(iterator.next()));
            }
            return pdfTable;
        }
    }

    private Cell parseInputToCell(String str) {
        str = str.trim();
        str = str.startsWith("\"") ? str.substring(1, str.length() - 1) : str;
        return new Cell().add(new Paragraph(new Text(str).setFontSize(FONT_SIZE)));
    }
}
