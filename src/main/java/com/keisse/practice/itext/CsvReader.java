package com.keisse.practice.itext;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    private static final File INPUT_FOLDER = new File("input");
    private static final String FILE_EXTENSION = ".csv";

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();

        CsvReader csvReader = new CsvReader();
        List<ByteArrayInputStream> baisList = new ArrayList<>();
        for (ByteArrayOutputStream baos : csvReader.toPdf()) {
            baisList.add(new ByteArrayInputStream(baos.toByteArray()));
        }
        for (int i = 0; i < baisList.size(); i++) {
            PdfWriter writer = new PdfWriter("pdf/csv" + i + ".pdf");
            PdfReader reader = new PdfReader(baisList.get(i));
            new PdfDocument(reader, writer).close();
            writer.close();
        }
        long endTime = System.nanoTime();
        System.out.printf("Bleep, bloop. Code executed in: %.2f ms! %n", (double) (endTime - startTime) / 1000000);
    }

    public List<ByteArrayOutputStream> toPdf() {
        List<ByteArrayOutputStream> output = new ArrayList<>();

        if (INPUT_FOLDER.isDirectory() && INPUT_FOLDER.listFiles() != null) {
            for (File f : INPUT_FOLDER.listFiles()) {
                String extension = f.getName();
                extension = extension.substring(extension.lastIndexOf("."));

                if (extension.equals(FILE_EXTENSION)) {
                    try (
                            BufferedReader reader = new BufferedReader(new FileReader(f));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream()
                    ) {
                        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
                        Document document = new Document(pdfDoc);

                        List<String> rowList = new ArrayList();
                        reader.lines()
                                .forEach(rowList::add);
                        int columnCount = rowList.get(0).split(",").length;//TODO need to check here as well for possible illegal characters
                        Table table = new Table(columnCount);
                        for (String strRow : rowList) {
                            String[] row = strRow.split(",");

                            for (String data : row) {
                                table.addCell(new Cell().add(new Paragraph(data)));
                            }
                        }
                        document.add(table);
                        document.close();
                        output.add(baos);

                    } catch (IOException ioex) {
                        ioex.printStackTrace();
                    }
                }
            }
        }
        return output;
    }
}