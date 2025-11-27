package org.example.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Utility class để render PDF từ JasperReports template
 */
@Slf4j
public class JasperCvUtil {

    private JasperCvUtil() {
        // Utility class
    }

    /**
     * Render PDF từ template .jrxml và dữ liệu Map
     * 
     * @param data Map chứa dữ liệu CV
     * @param templateName Tên template (không có extension)
     * @return byte[] PDF content
     */
    public static byte[] generatePdf(Map<String, Object> data, String templateName) throws JRException {
        String templatePath = "/jasper/" + templateName + ".jrxml";
        InputStream jrxmlInput = JasperCvUtil.class.getResourceAsStream(templatePath);
        
        if (jrxmlInput == null) {
            log.error("Template not found: {}", templatePath);
            throw new JRException("Template not found: " + templatePath);
        }
        
        // Compile template
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);
        
        // Tạo data source
        JRDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(data));
        
        // Fill report với data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, dataSource);
        
        // Export to PDF bytes
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    /**
     * Render PDF từ template và dữ liệu, trả về JasperPrint để có thể lưu file
     */
    public static JasperPrint generateJasperPrint(Map<String, Object> data, String templateName) throws JRException {
        String templatePath = "/jasper/" + templateName + ".jrxml";
        InputStream jrxmlInput = JasperCvUtil.class.getResourceAsStream(templatePath);
        
        if (jrxmlInput == null) {
            log.error("Template not found: {}", templatePath);
            throw new JRException("Template not found: " + templatePath);
        }
        
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);
        JRDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(data));
        
        return JasperFillManager.fillReport(jasperReport, data, dataSource);
    }

    /**
     * Render PDF thật và lưu vào file
     * 
     * @param jasperPrint JasperPrint đã render
     * @param cvId ID của CV để đặt tên file
     * @return đường dẫn relative của file PDF
     */
    public static String savePdfToFile(JasperPrint jasperPrint, Long cvId) throws JRException {
        String folder = "uploads/pdf/";
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = "cv_" + cvId + "_" + System.currentTimeMillis() + ".pdf";
        String filePath = folder + fileName;
        
        JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
        log.info("PDF saved to: {}", filePath);

        return "/pdf/" + fileName;
    }

    /**
     * Xóa file PDF
     */
    public static boolean deletePdfFile(String pdfUrl) {
        if (pdfUrl == null || pdfUrl.isBlank()) {
            return false;
        }
        
        // Convert URL path to file path
        String filePath = "uploads" + pdfUrl;
        File file = new File(filePath);
        
        if (file.exists()) {
            boolean deleted = file.delete();
            log.info("Delete PDF file {}: {}", filePath, deleted ? "success" : "failed");
            return deleted;
        }
        
        return false;
    }
}
