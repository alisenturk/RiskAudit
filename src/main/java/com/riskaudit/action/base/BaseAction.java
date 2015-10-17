package com.riskaudit.action.base;

import com.riskaudit.entity.base.BaseEntity;
import com.riskaudit.enums.Status;
import com.riskaudit.util.Helper;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.param.ParamValue;

/**
 *
 * @author asenturk
 */
@Dependent
public class BaseAction<T extends BaseEntity> implements Serializable{
    @Inject
    private CrudService crud;
    
    @Inject
    @Param(required = false)
    private ParamValue<Long> id;
    
    private T           instance;
    private List<T>     list = new ArrayList<T>();
    
    public CrudService getCrud() {
        return crud;
    }

    public void setCrud(CrudService crud) {
        this.crud = crud;
    }

    public ParamValue<Long> getId() {
        return id;
    }

    public void setId(ParamValue<Long> id) {
        this.id = id;
    }

    
    public T getInstance(){
        try{
            if(instance==null){
                if(id!=null && id.getValue()!=null && id.getValue()>0){
                    instance = crud.find(getClassType(),id.getValue());                
                }else{
                    instance = createInstance();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }
    private Class<T> getClassType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }
    
    public T createInstance() {
        try {
            T a = getClassType().newInstance();
            return a;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
    
    
    public void newRecord() throws InstantiationException, IllegalAccessException{
        instance = createInstance();
    }
    public void save(){
        try{
            if(instance.isManaged()){
                crud.updateObject(instance);
                Helper.addMessage(Helper.getMessage("Global.Record.Updated"));
            }else{
                crud.createObject(instance);
                newRecord();
                Helper.addMessage(Helper.getMessage("Global.Record.Added"));
            }
            list = new ArrayList<T>();
        }catch(Exception e){
            Helper.addMessage("HATA..:" + e.getMessage());
        }
    }
    
    public void delete(T t){
        t.setStatus(Status.DELETED);
        crud.deleteObject(t);
        list = new ArrayList<T>();
        Helper.addMessage(Helper.getMessage("Global.Record.Deleted"));
    }

    
}
