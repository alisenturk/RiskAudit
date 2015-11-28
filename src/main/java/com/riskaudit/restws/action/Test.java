package com.riskaudit.restws.action;

import com.google.gson.Gson;
import com.riskaudit.enums.Currency;
import com.riskaudit.enums.MarketPlace;
import com.riskaudit.restws.data.order.OrderInfo;
import com.riskaudit.restws.data.order.OrderProduct;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asenturk
 */
public class Test {
    
    public static void main(String[] args){
        OrderInfo o = new OrderInfo();
        o.setOrderNo("O1");
        o.setOrderDate("20151024");
        o.setMemberName("Ali");
        o.setMemberSurname("Şentürk");
        o.setMemberUsername("ali.senturk");
        o.setMarketPlace(MarketPlace.WEB.getValue());
        o.setAgentCode("1");
        o.setOrderAmount(100.34);
        o.setCurrency(Currency.EUR.getKey());
        o.setStatusCode("GNDR");
        OrderProduct op = new OrderProduct();
        op.setProductCode("ABC");
        op.setProudctName("ABC Deterjanları");
        op.setCategoryCode("BYZESYA");
        op.setSubCategoryCode("Mutfak");
        op.setQuantity(1);
        op.setPrice(50.17);
        o.getProducts().add(op);
        op = new OrderProduct();
        op.setProductCode("OMO");
        op.setProudctName("OMO Deterjanları");
        op.setCategoryCode("BYZESYA");
        op.setSubCategoryCode("Mutfak");
        op.setQuantity(2);
        op.setPrice(50.17);
        o.getProducts().add(op);
        
        List<OrderInfo> list = new ArrayList<OrderInfo>();
        list.add(o);
        
        Gson gson = new Gson();
        String result = gson.toJson(list);
        
        System.out.println("result..:" + result);
        
    }
}
