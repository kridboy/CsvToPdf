/*
 * Copyright (c) 2019. $name
 * All Rights Reserved.
 */

package com.keisse.practice.itext;


import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LargeCsvToPdfConverterTest {
    private static String PDF_EXTENSION = ".pdf";
    LargeCsvToPdfConverter converter;
    Table testTable;

    @Before
    public void beforeAll() {
        converter = new LargeCsvToPdfConverter("csvtest");
        testTable = new Table(3);
        testTable.addCell(new Cell().add(new Paragraph(new Text("header1").setFontSize((5f)))));
        testTable.addCell(new Cell().add(new Paragraph(new Text("header 2").setFontSize((5f)))));
        testTable.addCell(new Cell().add(new Paragraph(new Text("header3").setFontSize((5f)))));
        testTable.addCell(new Cell().add(new Paragraph(new Text("value1").setFontSize((5f)))));
        testTable.addCell(new Cell().add(new Paragraph(new Text("value,2").setFontSize((5f)))));
        testTable.addCell(new Cell().add(new Paragraph(new Text("value 3").setFontSize((5f)))));
    }

    @Test
    public void processCsvFileToPdfTable() throws IOException {
        Table fileTable = converter.processCsvFileToTable(new File("csvtest/test.csv"));
        for (int i = 0; i < fileTable.getChildren().size(); i++) {
            Assert.assertEquals(fileTable.getChildren().get(i).toString(), testTable.getChildren().get(i).toString());
        }
    }

    @Test
    public void createMappedTablesFromFiles() throws IOException {
        Map<String, Table> testMap = new HashMap<>();
        testMap.put("test", testTable);
        Assert.assertEquals(testMap.keySet().toString(), converter.createMappedTablesFromFiles().keySet().toString());
    }

    @Test
    public void convertFilesToPdf() throws IOException {
        converter.convertFilesToPdf();
        File resultFolder = new File("csvtest/pdf");
        Assert.assertTrue(resultFolder.isDirectory());
        boolean hasFiles = false;
        for (File file : resultFolder.listFiles())
            if (file.getName().substring(file.getName().lastIndexOf('.')).equals(PDF_EXTENSION))
                hasFiles = true;
        Assert.assertTrue(hasFiles);
    }
}
