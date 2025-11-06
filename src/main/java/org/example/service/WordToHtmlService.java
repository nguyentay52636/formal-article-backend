package org.example.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class WordToHtmlService {

    public String convertWordToHtml(InputStream inputStream) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            
            for (XWPFParagraph paragraph : paragraphs) {
                html.append("<p>");
                List<XWPFRun> runs = paragraph.getRuns();
                
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null && !text.isEmpty()) {
                        boolean isBold = run.isBold();
                        boolean isItalic = run.isItalic();
                        
                        if (isBold) html.append("<strong>");
                        if (isItalic) html.append("<em>");
                        
                        html.append(escapeHtml(text));
                        
                        if (isItalic) html.append("</em>");
                        if (isBold) html.append("</strong>");
                    }
                }
                html.append("</p>");
            }
        }
        
        html.append("</body></html>");
        return html.toString();
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
