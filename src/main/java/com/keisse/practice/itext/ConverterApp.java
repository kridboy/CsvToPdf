package com.keisse.practice.itext;

import java.io.IOException;

public class ConverterApp {
    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        CsvToPdfConverter converter = new CsvToPdfConverter("input");
        converter.convertFilesToPdf();
        //LargeCsvToPdfConverter largeConverter = new LargeCsvToPdfConverter(new File("input"));
        //largeConverter.convertFiles();
        long endTime = System.nanoTime();
        System.out.printf("Bleep, bloop! Execution time is= %.2fMS!",(double)((endTime-startTime)/1000000));

    }
}
