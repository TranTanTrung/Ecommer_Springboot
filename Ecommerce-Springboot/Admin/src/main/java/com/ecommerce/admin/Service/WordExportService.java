package com.ecommerce.admin.Service;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class WordExportService {

    public byte[] exportToWord(String data) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Add centered heading "CHI TIẾT HÓA ĐƠN"
            XWPFParagraph heading = document.createParagraph();
            heading.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun headingRun = heading.createRun();
            headingRun.setText("CHI TIẾT HÓA ĐƠN");
            headingRun.setBold(true);
            headingRun.setFontSize(16);
            headingRun.setFontFamily("Times New Roman");

            // Add a blank line
            document.createParagraph();

            // Add content
            XWPFParagraph contentParagraph = document.createParagraph();
            XWPFRun contentRun = contentParagraph.createRun();
            contentRun.setText(data);

            document.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null; // Or handle the exception according to your needs
        }
    }
}
