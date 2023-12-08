package com.ecommerce.admin.Service;

import com.ecommerce.library.model.Order;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuccessExcelExporter {

    private final List<Order> listOrders;

    public SuccessExcelExporter(List<Order> listOrders) {
        this.listOrders = listOrders;
    }

    public void export(HttpServletResponse response) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ServletOutputStream outputStream = response.getOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("Orders");
            writeHeaderLine(sheet);
            writeDataLines(sheet);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=successful_orders.xlsx");

            workbook.write(outputStream);
        }
    }

    private void writeHeaderLine(XSSFSheet sheet) {
        Row row = sheet.createRow(0);

        CellStyle style = sheet.getWorkbook().createCellStyle();
        XSSFFont font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Date", style);
        createCell(row, 2, "Payment-Method", style);
        createCell(row, 3, "Quantity", style);
        createCell(row, 4, "Price", style);
        // Add more headers as needed
    }

    private void writeDataLines(XSSFSheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        XSSFFont font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);

        int rowCount = 1;
        int columnCount = 0;

        // Filter orders for the current month
        List<Order> currentMonthOrders = listOrders.stream()
                .filter(order -> isSameMonth(convertToLocalDate(order.getOrderDate()), LocalDate.now()))
                .collect(Collectors.toList());

        for (Order order : currentMonthOrders) {
            Row row = sheet.createRow(rowCount++);
            columnCount = 0;

            createCell(row, columnCount++, order.getId(), style);
            createCell(row, columnCount++, order.getOrderDate().toString(), style);
            createCell(row, columnCount++, order.getPaymentMethod(), style);
            createCell(row, columnCount++, order.getQuantity(), style);  // Assuming getQuantity() returns an Integer or Long
            createCell(row, columnCount++, order.getTotalPrice(), style); // Assuming getTotalPrice() returns a Double or BigDecimal
            // Add more data fields as needed
        }

        // Set a fixed width for the columns (in this example, 20 characters)
        for (int i = 0; i < columnCount; i++) {
            sheet.setColumnWidth(i, 20 * 256); // Width is in units of 1/256th of a character width
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        row.getLastCellNum();
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else {
            cell.setCellValue(""); // Handle other data types or set to an appropriate default
        }
        cell.setCellStyle(style);
    }
    private boolean isSameMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth();
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
