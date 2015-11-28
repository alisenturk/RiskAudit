package com.riskaudit.action.order;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.restws.data.order.OrderInfo;
import com.riskaudit.util.Helper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class MerchantOrderSearch implements Serializable{
    
    private String  orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public List<OrderInfo> orderInfoList(){ 
        List<OrderInfo> list =new ArrayList<OrderInfo>();
        try{
            Merchant merchant = Helper.getCurrentUserMerchant();
            if(merchant!=null && merchant.getActiveOrderWS()){
                Client client = Client.create();
                WebResource webResource = client.resource(merchant.getMerchantOrderWSPath());
                String input = "";
                Form form = new Form();
                form.add("orderNo", getOrderNo());
                ClientResponse response = webResource.post(ClientResponse.class,form);
                if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                }
                String output = response.getEntity(String.class);
                if(output!=null && output.length()>10){
                    Gson gson = new Gson();
                    list = gson.fromJson(output,new TypeToken<List<OrderInfo>>(){}.getType());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return list;
    }
    
}
