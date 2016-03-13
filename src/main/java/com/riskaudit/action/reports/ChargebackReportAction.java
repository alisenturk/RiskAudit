package com.riskaudit.action.reports;

import com.riskaudit.action.base.CrudService;
import com.riskaudit.entity.base.Merchant;
import com.riskaudit.util.Helper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author alisenturk
 */

@ViewScoped
@Named(value = "chargebackReportAction")
public class ChargebackReportAction implements Serializable{
    @Inject
    CrudService crud;

    private Merchant        merchant            = Helper.getCurrentUserMerchant();
    private Date            requestDateEnd      = new Date();
    private Date            requestDateBegin    = Helper.dateAdd(requestDateEnd, -90);
    
    private ChargebackReportQueries   queries             = new ChargebackReportQueries();
    
    private List<Object[]>      chargebackProcessTypeBasedList            = new ArrayList<>();    
    private BarChartModel       chargebackProcessTypeBasedBarModel        = new BarChartModel();
    
    private List<Object[]>      chargebackCodeBasedList            = new ArrayList<>();    
    private BarChartModel       chargebackCodeBasedBarModel        = new BarChartModel();
    
    @PostConstruct
    public void init() {
        loadReportDatas();
    }
    
    public void loadReportDatas() {
        try {
            queries.clearData();
            
            queries.setMerchantId(merchant.getId());
            queries.setRequestDateBegin(requestDateBegin);
            queries.setRequestDateEnd(requestDateEnd);
            
            chargebackProcessTypeBasedList.clear();
            chargebackProcessTypeBasedList.addAll(crud.getNativeList(queries.getChargebackProcessTypeBased(), (HashMap<String, Object>) queries.getParams()));
            
            chargebackCodeBasedList.clear();
            chargebackCodeBasedList.addAll(crud.getNativeList(queries.getChargebackCodeBased(), (HashMap<String, Object>) queries.getParams()));
            
            loadChargebackProcessTypeBarChart();
            loadChargebackCodeBarChart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadChargebackProcessTypeBarChart(){
        chargebackProcessTypeBasedBarModel = new BarChartModel();
        chargebackProcessTypeBasedBarModel.setTitle("Process Type Chart");
        chargebackProcessTypeBasedBarModel.setAnimate(true);
        chargebackProcessTypeBasedBarModel.setLegendPosition("ne");

        Axis xAxis = chargebackProcessTypeBasedBarModel.getAxis(AxisType.X);
        xAxis.setLabel("");
        xAxis.setTickAngle(-30);

        Axis yAxis = chargebackProcessTypeBasedBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Count");
        yAxis.setMin(0);
        yAxis.setMax(5);

        String label = "";
        double val = 0d;


        ChartSeries respSerie = null;
        double maxVal = 0;
        if(chargebackProcessTypeBasedList!=null && chargebackProcessTypeBasedList.size()>0){
            respSerie = new ChartSeries();
            respSerie.setLabel("Process Type");
            for(Object[] item:chargebackProcessTypeBasedList){
                
                label = (String)item[0];
                if(val>maxVal)
                    maxVal = val;
                
                if(item[1] instanceof BigDecimal ){
                    val = ((BigDecimal)item[1]).doubleValue();
                }if(item[1] instanceof Double ){
                    val = ((Double)item[1]).doubleValue();
                }else if(item[1] instanceof BigInteger){
                    val = ((BigInteger)item[1]).doubleValue();
                }
                
                respSerie.getData().put(label, val);
            }
             chargebackProcessTypeBasedBarModel.addSeries(respSerie);
            
        }
        
         yAxis.setMax(maxVal+1);
    }
    
    private void loadChargebackCodeBarChart(){
        chargebackCodeBasedBarModel = new BarChartModel();
        chargebackCodeBasedBarModel.setTitle("Process Type Chart");
        chargebackCodeBasedBarModel.setAnimate(true);
        chargebackCodeBasedBarModel.setLegendPosition("ne");

        Axis xAxis = chargebackCodeBasedBarModel.getAxis(AxisType.X);
        xAxis.setLabel("");
        xAxis.setTickAngle(-30);

        Axis yAxis = chargebackCodeBasedBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Count");
        yAxis.setMin(0);
        yAxis.setMax(5);

        String label = "";
        double val = 0d;


        ChartSeries respSerie = null;
        double maxVal = 0;
        if(chargebackCodeBasedList!=null && chargebackCodeBasedList.size()>0){
            respSerie = new ChartSeries();
                respSerie.setLabel("Chargeback Code");
            for(Object[] item:chargebackCodeBasedList){
                
                label = (String)item[0];
                
                if(val>maxVal)
                    maxVal = val;
                
                if(item[1] instanceof BigDecimal ){
                    val = ((BigDecimal)item[1]).doubleValue();
                }if(item[1] instanceof Double ){
                    val = ((Double)item[1]).doubleValue();
                }else if(item[1] instanceof BigInteger){
                    val = ((BigInteger)item[1]).doubleValue();
                }
                respSerie.getData().put(label, val);
            }
             chargebackCodeBasedBarModel.addSeries(respSerie);
            
        }
        
         yAxis.setMax(maxVal+1);
    }

    public List<Object[]> getChargebackCodeBasedList() {
        return chargebackCodeBasedList;
    }

    public void setChargebackCodeBasedList(List<Object[]> chargebackCodeBasedList) {
        this.chargebackCodeBasedList = chargebackCodeBasedList;
    }

    public BarChartModel getChargebackCodeBasedBarModel() {
        return chargebackCodeBasedBarModel;
    }

    public void setChargebackCodeBasedBarModel(BarChartModel chargebackCodeBasedBarModel) {
        this.chargebackCodeBasedBarModel = chargebackCodeBasedBarModel;
    }

        
    public Date getRequestDateEnd() {
        return requestDateEnd;
    }

    public void setRequestDateEnd(Date requestDateEnd) {
        this.requestDateEnd = requestDateEnd;
    }

    public Date getRequestDateBegin() {
        return requestDateBegin;
    }

    public void setRequestDateBegin(Date requestDateBegin) {
        this.requestDateBegin = requestDateBegin;
    }

    public List<Object[]> getChargebackProcessTypeBasedList() {
        return chargebackProcessTypeBasedList;
    }

    public void setChargebackProcessTypeBasedList(List<Object[]> chargebackProcessTypeBasedList) {
        this.chargebackProcessTypeBasedList = chargebackProcessTypeBasedList;
    }

    public BarChartModel getChargebackProcessTypeBasedBarModel() {
        return chargebackProcessTypeBasedBarModel;
    }

    public void setChargebackProcessTypeBasedBarModel(BarChartModel chargebackProcessTypeBasedBarModel) {
        this.chargebackProcessTypeBasedBarModel = chargebackProcessTypeBasedBarModel;
    }
    
    
}
