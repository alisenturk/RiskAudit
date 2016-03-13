package com.riskaudit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

/**
 *
 * @author alisenturk
 */
public class WordDocumentReplace {


    public static File readWordDoc(String fileName,String orginalFilePath,HashMap<String, String> keyMap) {
        File file = null;
        FileInputStream     fis             = null;
        FileOutputStream    outputStream    = null;
        try {
            file = new File(fileName);
            outputStream = new FileOutputStream(file);
            fis         = new FileInputStream(orginalFilePath);
            XWPFDocument doc = new XWPFDocument(fis);        
            doc = replaceText(doc, keyMap);
        
            doc.write(outputStream);
            
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    //if (outputStream != null) {
                    //	outputStream.close();
                    //}
                    fis.close();
                   // outputStream = null;
                    fis = null;
                } catch (IOException ioEx) {
                    ioEx.printStackTrace();
                }
            }
        }
        return file;
    }//end readWordDoc
	
    public static XWPFDocument replaceText(XWPFDocument doc, HashMap<String, String> keys) throws Exception {
        String txt = "";
        int txtPosition = 0;
        String key = "";
        String val = "";
        for (XWPFParagraph p : doc.getParagraphs()) { //Reading each paragraph on document
            for (XWPFRun run : p.getRuns()) { //Reading each line on paragraph
                txtPosition = run.getTextPosition();
                txt = run.getText(txtPosition);
                for (Map.Entry<String, String> entry : keys.entrySet()) { //Fields are being changed which is sent in keymap with fields in keymap
                    key = entry.getKey();
                    val = entry.getValue();
                    //System.out.println("txt...:" + txt + " key ..:" + key);
                    if (txt != null && txt.indexOf(key) > -1) {
                        txt = txt.replace(key, val);
                        run.setText(txt, 0);
                    }//end if
                }//end for
            }//end for
        }//end for
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
               for (XWPFTableCell cell : row.getTableCells()) {
                  for (XWPFParagraph p : cell.getParagraphs()) {
                     for (XWPFRun run : p.getRuns()) {
                         txtPosition = run.getTextPosition();
                         txt = run.getText(txtPosition);
                         for (Map.Entry<String, String> entry : keys.entrySet()) { //Fields are being changed which is sent in keymap with fields in keymap
                             key = entry.getKey();
                             val = entry.getValue();
                             if (txt != null && txt.indexOf(key) > -1) {
                                 txt = txt.replace(key, val);
                                 run.setText(txt, 0);
                             }//end if
                         }//end for
                     }
                  }
               }
            }
         }
        return doc; 
    }//end replaceText
    
}
