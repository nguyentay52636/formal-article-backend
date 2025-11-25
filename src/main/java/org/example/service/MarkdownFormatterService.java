package org.example.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarkdownFormatterService {

    public String markdownToHtml(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return "";
        }

        // Split into lines for processing
        String[] lines = markdown.split("\n");
        List<String> htmlLines = new ArrayList<>();
        boolean inList = false;
        boolean inOrderedList = false;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmed = line.trim();
            
            if (trimmed.isEmpty()) {
                if (inList) {
                    htmlLines.add("</ul>");
                    inList = false;
                }
                if (inOrderedList) {
                    htmlLines.add("</ol>");
                    inOrderedList = false;
                }
                htmlLines.add("");
                continue;
            }
            
            // Headers
            if (trimmed.startsWith("#")) {
                if (inList) {
                    htmlLines.add("</ul>");
                    inList = false;
                }
                if (inOrderedList) {
                    htmlLines.add("</ol>");
                    inOrderedList = false;
                }
                
                int level = 0;
                while (level < trimmed.length() && trimmed.charAt(level) == '#') {
                    level++;
                }
                String headerText = trimmed.substring(level).trim();
                headerText = processInlineFormatting(headerText);
                htmlLines.add(String.format("<h%d>%s</h%d>", Math.min(level, 6), headerText, Math.min(level, 6)));
                continue;
            }
            
            // Unordered list
            if (trimmed.matches("^[-*]\\s+.+")) {
                if (inOrderedList) {
                    htmlLines.add("</ol>");
                    inOrderedList = false;
                }
                if (!inList) {
                    htmlLines.add("<ul>");
                    inList = true;
                }
                String listItem = trimmed.substring(trimmed.indexOf(' ') + 1);
                listItem = processInlineFormatting(listItem);
                htmlLines.add("<li>" + listItem + "</li>");
                continue;
            }
            
            // Ordered list
            if (trimmed.matches("^\\d+\\.\\s+.+")) {
                if (inList) {
                    htmlLines.add("</ul>");
                    inList = false;
                }
                if (!inOrderedList) {
                    htmlLines.add("<ol>");
                    inOrderedList = true;
                }
                String listItem = trimmed.substring(trimmed.indexOf('.') + 1).trim();
                listItem = processInlineFormatting(listItem);
                htmlLines.add("<li>" + listItem + "</li>");
                continue;
            }
            
            // Regular paragraph
            if (inList) {
                htmlLines.add("</ul>");
                inList = false;
            }
            if (inOrderedList) {
                htmlLines.add("</ol>");
                inOrderedList = false;
            }
            
            String processed = processInlineFormatting(trimmed);
            htmlLines.add("<p>" + processed + "</p>");
        }
        
        // Close any open lists
        if (inList) {
            htmlLines.add("</ul>");
        }
        if (inOrderedList) {
            htmlLines.add("</ol>");
        }
        
        // Join lines
        String html = String.join("\n", htmlLines);
        
        // Clean up empty paragraphs
        html = html.replaceAll("<p></p>", "");
        html = html.replaceAll("<p>\\s*</p>", "");
        
        // Wrap with styles
        return wrapWithStyles(html);
    }
    
    private String processInlineFormatting(String text) {
        // Escape HTML
        text = text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
        
        // Bold **text** or __text__
        text = text.replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>");
        text = text.replaceAll("__(.+?)__", "<strong>$1</strong>");
        
        // Italic *text* or _text_ (but not if it's part of bold)
        text = text.replaceAll("(?<!\\*)\\*(?!\\*)(.+?)(?<!\\*)\\*(?!\\*)", "<em>$1</em>");
        text = text.replaceAll("(?<!_)_(?!_)(.+?)(?<!_)_(?!_)", "<em>$1</em>");
        
        return text;
    }
    
    private String wrapWithStyles(String html) {
        return String.format("""
            <div class="markdown-content" style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                        line-height: 1.8;
                        color: #2c3e50;
                        max-width: 900px;
                        margin: 0 auto;
                        padding: 30px;
                        background: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                %s
            </div>
            <style>
                .markdown-content h1 { 
                    font-size: 2.2em; 
                    font-weight: 700; 
                    margin-top: 1.2em; 
                    margin-bottom: 0.6em; 
                    color: #1a252f; 
                    border-bottom: 3px solid #3498db; 
                    padding-bottom: 0.4em;
                    line-height: 1.3;
                }
                .markdown-content h2 { 
                    font-size: 1.8em; 
                    font-weight: 600; 
                    margin-top: 1em; 
                    margin-bottom: 0.5em; 
                    color: #2c3e50;
                    border-left: 4px solid #3498db;
                    padding-left: 0.5em;
                }
                .markdown-content h3 { 
                    font-size: 1.4em; 
                    font-weight: 600; 
                    margin-top: 0.9em; 
                    margin-bottom: 0.4em; 
                    color: #34495e; 
                }
                .markdown-content h4 { 
                    font-size: 1.2em; 
                    font-weight: 600; 
                    margin-top: 0.8em; 
                    margin-bottom: 0.3em; 
                    color: #555; 
                }
                .markdown-content h5, .markdown-content h6 { 
                    font-size: 1.1em; 
                    font-weight: 600; 
                    margin-top: 0.7em; 
                    margin-bottom: 0.3em; 
                    color: #666; 
                }
                .markdown-content p { 
                    margin-bottom: 1.2em; 
                    text-align: justify;
                    line-height: 1.8;
                }
                .markdown-content ul, .markdown-content ol { 
                    margin-left: 2em; 
                    margin-bottom: 1.2em;
                    padding-left: 1em;
                }
                .markdown-content ul {
                    list-style-type: disc;
                }
                .markdown-content ol {
                    list-style-type: decimal;
                }
                .markdown-content li { 
                    margin-bottom: 0.6em;
                    line-height: 1.7;
                }
                .markdown-content li p {
                    margin-bottom: 0.5em;
                }
                .markdown-content strong { 
                    font-weight: 700; 
                    color: #1a252f; 
                }
                .markdown-content em { 
                    font-style: italic; 
                    color: #555;
                }
                .markdown-content br { 
                    line-height: 1.8; 
                }
            </style>
            """, html);
    }
}
