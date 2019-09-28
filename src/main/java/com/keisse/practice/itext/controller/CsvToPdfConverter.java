package com.keisse.practice.itext.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.keisse.practice.itext.model.ParsedCSV;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvToPdfConverter {
    private static final File INPUT_FOLDER = new File("input");
    private static final String CSV_EXTENSION = ".csv";

    public void toPdf() throws IOException {
        for (ParsedCSV parsedCSV : parseInputFolder()) {

            PdfWriter writer = new PdfWriter("pdf/"+parsedCSV.getTitle() + ".pdf");
            PdfReader reader = new PdfReader(new ByteArrayInputStream(parsedCSV.getPdfByteArray()));
            new PdfDocument(reader, writer).close();
            writer.close();
        }
    }

    private List<ParsedCSV> parseInputFolder() {
        List<ParsedCSV> output = new ArrayList<>();

        if (INPUT_FOLDER.isDirectory() && INPUT_FOLDER.listFiles() != null) {
            for (File inputFile : INPUT_FOLDER.listFiles()) {
                String extension = inputFile.getName();
                extension = extension.substring(extension.lastIndexOf('.'));

                if (extension.equals(CSV_EXTENSION)) {
                   output.add(new ParsedCSV(inputFile));
                }
            }
        }
        return output;
    }
}