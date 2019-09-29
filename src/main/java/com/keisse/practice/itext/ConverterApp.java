package com.keisse.practice.itext;

import java.io.IOException;

public class ConverterApp {
    public static void main(String[] args) throws IOException {
        CsvToPdfConverter converter = new CsvToPdfConverter("input");
        converter.convertFiles();
    }
}
