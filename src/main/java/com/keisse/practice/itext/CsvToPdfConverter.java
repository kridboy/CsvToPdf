package com.keisse.practice.itext;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvToPdfConverter {
    private static final File INPUT_FOLDER = new File("input");
    private static final String CSV_EXTENSION = ".csv";

    public static void main(String[] args) throws IOException{
        CsvToPdfConverter csvToPdfConverter = new CsvToPdfConverter();
        csvToPdfConverter.convertFiles();
    }

    public void convertFiles() throws IOException{
        for(ParsedCsvTable parsedCsvTable : createParsedTablesFromFiles()){
            PdfWriter writer = new PdfWriter("pdf/"+ parsedCsvTable.getTitle() + ".pdf");
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);
            document.add(parsedCsvTable.getPdfTable());
            document.close();
            pdfDocument.close();
            writer.close();
        }
    }

    private List<ParsedCsvTable> createParsedTablesFromFiles() {
        List<ParsedCsvTable> output = new ArrayList<>();
        if (INPUT_FOLDER.isDirectory() && INPUT_FOLDER.listFiles() != null) {
            for(File inputFile:INPUT_FOLDER.listFiles()){
                String extension = inputFile.getName();
                extension = extension.substring(extension.lastIndexOf('.'));

                if (extension.equals(CSV_EXTENSION)) {
                    output.add(new ParsedCsvTable(inputFile));
                }
            }
        }
        return output;
    }
}