package com.keisse.practice.itext;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvReader {
    private static final File INPUT_FOLDER = new File("input");
    private static final String FILE_EXTENSION = ".csv";
    private static final String REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)|(\r\n|\n)";

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
                    try (Scanner scanner = new Scanner(new FileReader(f));
                         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(baos));
                        Document document = new Document(pdfDocument);

                        if (scanner.hasNext()) {

                            document.add(csvToTable(scanner));
                            document.close();
                            output.add(baos);
                        }
                    } catch (IOException ioex) {
                        ioex.printStackTrace();
                    }
                }
            }
        }
        return output;
    }
    private Table csvToTable(Scanner scanner){
        String[] headings = scanner.nextLine().split(",");
        Table csvTable = new Table(headings.length).setHorizontalAlignment(HorizontalAlignment.CENTER);
        scanner.useDelimiter(REGEX);

        for (String head : headings)
            csvTable.addCell(new Cell().add(new Paragraph(head) ));

        while (scanner.hasNext()) {
            String testStr = scanner.next().trim();
            csvTable.addCell(new Cell().add(new Paragraph(testStr)));
        }
        return csvTable;
    }
}