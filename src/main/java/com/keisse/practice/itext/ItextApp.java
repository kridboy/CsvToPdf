package com.keisse.practice.itext;

import com.keisse.practice.itext.controller.CsvToPdfConverter;

import java.io.IOException;

public class ItextApp {
    public static void main(String[] args) throws IOException {
        CsvToPdfConverter csvToPdfConverter = new CsvToPdfConverter();
        csvToPdfConverter.toPdf();
    }
}
