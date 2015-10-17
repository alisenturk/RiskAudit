package com.riskaudit.action.location;

import com.riskaudit.action.base.BaseAction;
import com.riskaudit.entity.location.Country;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class CountryAction extends BaseAction<Country> {

    @Override
    public List<Country> getList() {
        if (super.getList() == null || super.getList().isEmpty()) {
            super.setList(new ArrayList<Country>());
            super.getList().addAll(getCrud().getNamedList("Country.findAll"));
        }
        return super.getList();
    }

    public void handleFileUpload(FileUploadEvent event) {
        String errorMessage = "";
        try {
            List<Country> countriesList = new ArrayList<Country>();

            //Create the input stream from the xlsx/xls file
            String fileName = event.getFile().getFileName();
            String name = "";
            String shortCode = "";


            InputStream fis = event.getFile().getInputstream();

            //Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }

            Sheet sheet = workbook.getSheetAt(0);
            
            Iterator<Row> rowIterator = sheet.iterator();
            Row             row         = null;
            Iterator<Cell>  cellIterator= null;
            Cell            cell        = null;
            Country         country     = null;
            while (rowIterator.hasNext()) {
                name        = "";
                shortCode   = "";
    
                row = rowIterator.next();

                cellIterator = row.cellIterator();

                if (row.getRowNum() == 0) {
                    continue;
                }

                while (cellIterator.hasNext()) {
                    
                    cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            if (shortCode.equalsIgnoreCase("")) {
                                shortCode = cell.getStringCellValue().trim();
                            } else if (name.equalsIgnoreCase("")) {
                                name = cell.getStringCellValue().trim();
                            } 
                            break;

                    }

                } //end of cell iterator
                country = new Country();
                country.setCountryCode(shortCode);
                country.setCountryName(name);
                countriesList.add(country);
            } //end of rows iterator

            fis.close();
            for (Country c : countriesList) {
                super.setInstance(c);
                super.save();
            }
        } catch (IOException e) {
           errorMessage = e.getMessage();
        }
        System.out.println("eerrromessage..:" + errorMessage);
        FacesMessage message = null;
        if(errorMessage!=null && errorMessage.length()>3){
            message = new FacesMessage("ERROR..:","Country dosn't uploaded![" + errorMessage + "]");
        }else{
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);

    }
}
