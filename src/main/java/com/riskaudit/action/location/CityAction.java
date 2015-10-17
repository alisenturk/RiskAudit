/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.riskaudit.action.location;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.location.City;
import com.riskaudit.entity.location.Country;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author asenturk
 */
@Named
@ViewScoped
public class CityAction extends BaseAction<City> {

    private Country country = new Country();
    private List<Country> countries = new ArrayList<Country>();

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Country> getCountries() {
        if (countries == null || countries.isEmpty()) {
            countries.addAll(getCrud().getNamedList("Country.findAll"));
        }
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public void loadCities(ValueChangeEvent event) {
        super.setList(new ArrayList<City>());
        if (event != null && event.getNewValue() != null && event.getNewValue() != country) {
            super.setList(new ArrayList<City>());
            country = (Country) event.getNewValue();
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("country", country.getId());
            super.getList().addAll(getCrud().getNamedList("City.findCountryCities", params));
        }
    }

    @Override
    public List<City> getList() {
        if (super.getList() == null || super.getList().isEmpty()) {
            super.setList(new ArrayList<City>());
            super.getList().addAll(getCrud().getNamedList("City.findAll"));
        }
        return super.getList();
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            List<City> citiesList = new ArrayList<City>();

            //Create the input stream from the xlsx/xls file
            String fileName = event.getFile().getFileName();
            String cityCode    = "";
            String cityName    = "";
            String countryCode = "";
            String countryName = "";
            
            InputStream fis = event.getFile().getInputstream();

            //Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }

            Sheet           sheet           = workbook.getSheetAt(0);
            Iterator<Row>   rowIterator     = sheet.iterator();
            Row             row             = null;
            Iterator<Cell>  cellIterator    = null;
            Cell            cell            = null;
            City            city            = null;
            while (rowIterator.hasNext()) {
                cityCode    = "";
                cityName    = "";
                countryCode = "";
                countryName = "";

                row             = rowIterator.next();
                cellIterator    = row.cellIterator();
                
                if(row.getRowNum()==0)continue;
                
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            if (cityCode.equalsIgnoreCase("")) {
                                cityCode = cell.getStringCellValue().trim();
                            } else if (cityName.equalsIgnoreCase("")) {
                                cityName = cell.getStringCellValue().trim();
                            } else if (countryCode.equalsIgnoreCase("")) {
                                countryCode = cell.getStringCellValue().trim();
                            }
                            break;
                    }
                } //end of cell iterator
                if(countryCode.equals("#N/A"))continue;
                
                country = findCountry(countryCode);
                
                if(country!=null){
                    city = new City();
                    city.setCityCode(cityCode);
                    city.setCityName(cityName);
                    city.setCountry(country);

                    citiesList.add(city);
                }
                
            } //end of rows iterator

            //close file input stream
            fis.close();
            for (City c : citiesList) {
                super.setInstance(c);
                super.save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        country = null;
    }
    
    private Country findCountry(String countryCode){
        Country country = null;
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("countryCode",countryCode);
        List<Country> countries = getCrud().getNamedList("Country.findCountry",params);
        if(countries!=null && countries.size()>0){
            country = countries.get(0);
        }
        
        return country;
    }

    @Override
    public void newRecord() throws InstantiationException, IllegalAccessException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    CONCAT('CITY',(CAST(REPLACE(cityCode,'CITY','') as UNSIGNED)+1)) result ");
        sql.append("FROM city ");
        sql.append("WHERE id = (select max(id) from city) ");
        String cityCode = getCrud().getNativeSql(sql.toString());
        super.newRecord();
        if(cityCode!=null){
            getInstance().setCityCode(cityCode);
        }
    }
    
    

}
