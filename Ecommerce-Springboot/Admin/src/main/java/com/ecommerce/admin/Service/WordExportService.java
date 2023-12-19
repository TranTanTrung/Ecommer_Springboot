package com.ecommerce.admin.Service;

import com.ecommerce.library.model.OrderDetail;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class WordExportService {
    private static final Logger logger = LoggerFactory.getLogger(WordExportService.class);

    public byte[] exportToWord(Long orderDetailId, List<OrderDetail> orderDetails) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            if (orderDetails == null || orderDetails.isEmpty()) {
                // Handle the case when orderDetails is null or empty (e.g., return an error document)
                return new byte[0];
            }

            // Add "Chi tiết đơn hàng" heading
            XWPFParagraph heading = document.createParagraph();
            heading.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun headingRun = heading.createRun();
            headingRun.setText("Chi tiết đơn hàng");
            headingRun.setBold(true);
            headingRun.setFontSize(16);

            // Create table with N+1 rows and 2 columns (N is the size of orderDetails)
            XWPFTable table = document.createTable(6, 2);

            // Set column widths...
            // Add cell in the first row and first column with the text "Thông tin"
            table.getRow(0).getCell(0).setText("Thông tin");

            // Set label in the first column of the second row
            table.getRow(1).getCell(0).setText("Mã đơn hàng");
            table.getRow(2).getCell(0).setText("Mã sản phẩm");
            table.getRow(3).getCell(0).setText("Tên sản phẩmm");
            table.getRow(4).getCell(0).setText("Số lượng");
            table.getRow(5).getCell(0).setText("Giá tiền");

            for (int i = 1; i <= 6 ; i++) {
                // Set label in the first column
                XWPFTableRow row = table.getRow(i);
                if (row == null) {
                    row = table.createRow();
                }

                // Set data in the second column based on OrderDetail information
                String value = "";
                switch (i) {
                    case 1:
                        value = orderDetails.get(0).getOrder() != null ? String.valueOf(orderDetails.get(0).getOrder().getId()) : "";
                        break;
                    case 2:
                        value = orderDetails.get(0).getProduct() != null ? String.valueOf(orderDetails.get(0).getProduct().getId()) : "";
                        break;
                    case 3:
                        value = orderDetails.get(0).getName();
                        break;
                    case 4:
                        value = String.valueOf(orderDetails.get(0).getQuantity());
                        break;
                    case 5:
                        value = String.valueOf(orderDetails.get(0).getCostPrice());
                        break;
                    default:
                        // Handle additional cases if needed
                        break;
                }

                // Ensure value is not null before setting it in the cell
                if (value != null) {
                    XWPFTableCell cell = row.getCell(1);
                    if (cell == null) {
                        cell = row.createCell();
                    }
                    cell.setText(value);
                }
            }

            document.write(out);

            return out.toByteArray();

        } catch (IOException e) {
            // Log the exception
            logger.error("Error while exporting to Word for orderDetailId: {}", orderDetailId, e);
            return new byte[0];
        }
    }
}
