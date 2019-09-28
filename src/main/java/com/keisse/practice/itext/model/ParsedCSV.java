package com.keisse.practice.itext.model;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ParsedCSV {
    private static final String REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|(\r\n|\n)";
    private int columnCount;
    private byte[] pdfByteArray;
    private String title;


    public ParsedCSV(File inputFile) {
        pdfByteArray = parseInput(inputFile);
    }

    public int getColumnCount() {
        return columnCount;
    }

    public byte[] getPdfByteArray() {
        return pdfByteArray;
    }

    public String getTitle() {
        return title;
    }

    private void setColumnCount(int columnCount){
        this.columnCount = columnCount;
    }

    private void setTitle(String title){
     this.title = title;
    }

    private byte[] parseInput(File inputFile){
        try (Scanner scanner = new Scanner(new FileReader(inputFile));
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            setTitle(inputFile.getName().substring(0,inputFile.getName().lastIndexOf('.')));
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
            Document document = new Document(pdfDocument);
            if (scanner.hasNext()) {
                document.add(csvToTable(scanner));
                document.close();
            }
            return baos.toByteArray();

        }catch (IOException ex){
            ex.printStackTrace();
        }
        return new byte[0];
    }

    private Table csvToTable(Scanner scanner) {
        String[] headings = scanner.nextLine().split(",");
        setColumnCount(headings.length);
        Table csvTable = new Table(getColumnCount()).setHorizontalAlignment(HorizontalAlignment.CENTER);
        scanner.useDelimiter(REGEX);

        for (String head : headings)
            csvTable.addCell(new Cell().add(new Paragraph(trimCellInput(head))));

        while (scanner.hasNext()) {
            csvTable.addCell(new Cell().add(new Paragraph(trimCellInput(scanner.next()))));
        }
        return csvTable;
    }

    String trimCellInput(String str){
        str = str.trim();
        return str.startsWith("\"")?str.substring(1,str.length()-1):str;
    }
}
