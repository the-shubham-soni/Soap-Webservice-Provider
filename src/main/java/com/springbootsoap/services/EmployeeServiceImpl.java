package com.springbootsoap.services;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.springbootsoap.model.Employee;
import com.springbootsoap.repository.EmployeeRepository;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private S3Service s3Service;

    @Value("${cloud.aws.sqs.queue-url}")
    private String SQS_QUEUE_URL;

    @Override
    public void addEmployee(Employee employee) {
        try {
            employeeRepository.save(employee);

            // Send message to SQS
            String messageId = sendMessageToSQS("Employee INSERTED with details: " +
                    getEmployeeDetails(employee));

            // Generate and upload PDF
            String pdfUrl = generatePdfAndUpload(employee, messageId);
            logger.info("PDF uploaded successfully. PDF URL: {}", pdfUrl);

        } catch (Exception e) {
            logger.error("Error in addEmployee: {}", e.getMessage(), e);
            throw new RuntimeException("Error in addEmployee: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        try {
            employeeRepository.save(employee);

            // Send update message to SQS
            String messageId = sendMessageToSQS("Employee UPDATED with details: " +
                    getEmployeeDetails(employee));

            // Generate and upload PDF
            String pdfUrl = generatePdfAndUpload(employee, messageId);
            logger.info("PDF uploaded successfully for updated employee. PDF URL: {}", pdfUrl);

        } catch (Exception e) {
            logger.error("Error in updateEmployee: {}", e.getMessage(), e);
            throw new RuntimeException("Error in updateEmployee: " + e.getMessage(), e);
        }
    }

    private String sendMessageToSQS(String messageBody) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(SQS_QUEUE_URL)
                .messageBody(messageBody)
                .build();
        SendMessageResponse response = sqsClient.sendMessage(sendMessageRequest);
        logger.info("Message sent to SQS. Message ID: {}", response.messageId());
        return response.messageId();
    }

    private String generatePdfAndUpload(Employee employee, String messageId) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            generatePdf(employee, messageId, outputStream);
            return s3Service.uploadFile(outputStream.toByteArray(), employee.getEmployeeId() + ".pdf");
        } catch (Exception e) {
            logger.error("Error while generating and uploading PDF", e);
            throw new RuntimeException("Error while generating and uploading PDF", e);
        }
    }

    private void generatePdf(Employee employee, String messageId, OutputStream outputStream) throws DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(new Paragraph("Employee ID: " + employee.getEmployeeId()));
        document.add(new Paragraph("Name: " + employee.getName()));
        document.add(new Paragraph("Phone: " + employee.getPhone()));
        document.add(new Paragraph("Address: " + employee.getAddress()));
        document.add(new Paragraph("Department: " + employee.getDepartment()));
        document.add(new Paragraph("SQS Message ID: " + messageId));
        document.close();
    }

    private String getEmployeeDetails(Employee employee) {
        return "Employee ID: " + employee.getEmployeeId() + ", " +
               "Name: " + employee.getName() + ", " +
               "Phone: " + employee.getPhone() + ", " +
               "Department: " + employee.getDepartment() + ", " +
               "Address: " + employee.getAddress();
    }
}
