package com.springbootsoap.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.springbootsoap.model.Employee;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;

@Service
public class PDFService {

    public File generateEmployeePDF(Employee employee) {
        File pdfFile = new File("employee_" + employee.getEmployeeId() + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();
            document.add(new Paragraph("Employee Details:\n" + employee.toString()));
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
        return pdfFile;
    }
}
