package com.riskaudit.util;

import com.riskaudit.entity.order.OrderProduct;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author alisenturk
 */
public class TestMain {

    public static void main(String args[]) {
        HashMap<String, String> keys = new HashMap<String, String>();
        try {

            keys.put("#today#", Helper.date2String(new Date()));
            keys.put("#customer#", "Ali Şentürk");
            keys.put("#paymenttotal#", "100");
            keys.put("#creditcardno#", "4444xxxxxxxx4444");
            keys.put("#agencyname#", "www.site.com");
            keys.put("#reservationdate#", "20/01/2016");

            int productQuantity = 1;
            int totalSize = 6;
            int requiredSize = totalSize - productQuantity;
            int i = 0;
            int size = 0;
            for (int k=0;k<productQuantity;k++) {

                keys.put("#productcode" + (i + 1) + "#", "Code");
                keys.put("#productname" + (i + 1) + "#", "urun");
                keys.put("#count" + (i + 1) + "#", "1");
                keys.put("#price" + (i + 1) + "#","23");
                if (i >= totalSize) {
                    break;
                }
                i++;
                size = i;
            }
            for (int a = size; a <= requiredSize; a++) {
                keys.put("#productcode" + (a + 1) + "#", "");
                keys.put("#productname" + (a + 1) + "#", "");
                keys.put("#count" + (a + 1) + "#", "");
                keys.put("#price" + (a + 1) + "#", "");
            }//end for
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (keys != null && keys.size() > 0) {
            
            WordDocumentReplace.readWordDoc("/Users/alisenturk/Temp/test.docx", "/Users/alisenturk/Temp/opt/merchant/2/chargeback_kapak.docx", keys);

        }
    }

}
