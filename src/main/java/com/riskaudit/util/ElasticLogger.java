package com.riskaudit.util;

import com.alisenturk.elasticlogger.service.ElasticService;
import com.alisenturk.elasticlogger.service.ElasticSetting;
import java.util.List;

/**
 *
 * @author alisenturk
 */
public class ElasticLogger {
    
    private ElasticSetting  setting = new ElasticSetting();
    
    private ElasticService<ErrorClass>  service = null;

    public ElasticLogger() {
        init();
    }
    
    private void init(){
        String strDebug = Helper.getAppParameterValue("elastic.debug");
        boolean debug = Boolean.parseBoolean(strDebug);
        
        setting.setHostAddress(Helper.getAppParameterValue("elastic.host"));
        setting.setPortNumber(Helper.getAppParameterValue("elastic.port"));
        setting.setIndexName("apperrordb");
        setting.setMappingName("errorlog");
        setting.setDebugMode(debug);
        
        service = ElasticService.createElasticService(setting);

    }
    
    public void write(List<ErrorClass> errorList){
        
        long id = service.getDocumentCount();
        service.addDocument(errorList, id);
    }
}
