package com.keisse.practice.itext;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ItextApp {
    public static void main(String[] args) throws FileNotFoundException {

        FileOutputStream out = new FileOutputStream("pdf/test.pdf");
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfdoc = new PdfDocument(writer);
        Document doc = new Document(pdfdoc);

        doc.add(new Paragraph("Belle Perez"));

        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfdoc,true);
        PdfDictionary dictionary = new PdfDictionary();
        PdfFormField formField = PdfTextFormField.createText(pdfdoc,new Rectangle(20,20,200,50),"txt1");
        acroForm.addField(formField);
        pdfdoc.close();

    }
}
