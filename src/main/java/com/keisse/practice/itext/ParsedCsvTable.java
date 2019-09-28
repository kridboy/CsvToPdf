package com.keisse.practice.itext;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ParsedCsvTable {
    private static final String REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|(\r\n|\n)";
    private static final float FONT_SIZE = 5f;
    private Queue<String> dataQueue = new LinkedList<>();
    private String title;
    private Table pdfTable;

    public ParsedCsvTable(File inputFile) {
        preProcess(inputFile);
        pdfTable = processDataToTable();
    }

    public Table getPdfTable() {
        return pdfTable;
    }

    public String getTitle() {
        return title;
    }

    private Queue<String> getDataQueue() {
        return dataQueue;
    }


    private void setTitle(String title) {
        this.title = title;
    }

    private void preProcess(File inputFile) {
        setTitle(inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.')));

        try (Scanner scanner = new Scanner(new FileReader(inputFile))) {
            while(scanner.hasNext()) {
                getDataQueue().offer(scanner.nextLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Table processDataToTable(){
        String[] headerNames = getDataQueue().poll().split(",");
        Table pdfTable = new Table(headerNames.length).setHorizontalAlignment(HorizontalAlignment.CENTER);
        Queue<String> tableData = new LinkedList<>();

        for(String str:headerNames)
            pdfTable.addCell(new Cell().add(new Paragraph(new Text(trimCellInput(str)).setFontSize(FONT_SIZE))));

        while(!getDataQueue().isEmpty()){
            String row = getDataQueue().poll();
            String[] tableRow = row.split(REGEX);

            for(String str:tableRow)
                tableData.offer(str);

            while(!tableData.isEmpty())
                pdfTable.addCell(new Cell().add(new Paragraph(new Text(trimCellInput(tableData.poll())).setFontSize(FONT_SIZE))));

        }
        return pdfTable;
    }

    private String trimCellInput(String str){
        str = str.trim();
        return str.startsWith("\"")?str.substring(1,str.length()-1):str;
    }
}
