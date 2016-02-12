package com.riskaudit.action.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author asenturk
 */
@Stateless
public class CrudService implements Serializable {

    @Inject
    private EntityManager em;

    public Object updateObject(Object object) {
        return em.merge(object);
    }

    public void createObject(Object object) {
        em.persist(object);
    }

    public void refresh(Object object) {
        em.refresh(object);
    }

    public <T> T find(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    public void deleteObject(Object object) {
        em.merge(object);
        //em.remove(em.merge(object));
    }

    public List getList(String query, int maxResult) {
        return em.createQuery(query).setMaxResults(maxResult).getResultList();
    }

    public List getList(String query) {
        return em.createQuery(query).getResultList();
    }

    public List getList(String query, HashMap<String, Object> params) {
        List rl = null;
        Query cquery = null;
        try {
            cquery = em.createQuery(query);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() == null) {
                        continue;
                    }
                    cquery.setParameter(entry.getKey(), entry.getValue());
                }
            }
            rl = cquery.getResultList();
        } catch (Exception e) {
            System.out.println("Query..:" + query +" params..:" + params.toString());
        }
        return rl;
    }

    public List getNamedList(String query) {
        return em.createNamedQuery(query).getResultList();
    }

    public List getNativeList(String query) {
        return em.createNativeQuery(query).getResultList();
    }

    public String getNativeSql(String query) {
        return (String) em.createNativeQuery(query).getSingleResult();
    }

    public List getNamedList(String query, HashMap<String, Object> params) {
        List rl = null;
        Query nq = em.createNamedQuery(query);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nq.setParameter(entry.getKey(), entry.getValue());
            }
        }
        rl = nq.getResultList();

        return rl;
    }

    public List getNativeList(String query, HashMap<String, Object> params) {
       List rl = null;
       Query nq = em.createNativeQuery(query);
       if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nq.setParameter(entry.getKey(), entry.getValue());
            }
       }
       rl = nq.getResultList();
       return rl; 
    }
    
    public void updateQuery(String query) {
        try {
            em.createQuery(query).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void excutedUpdateNamedQuery(String query, HashMap<String, Object> params) {
        Query nq = em.createNamedQuery(query);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nq.setParameter(entry.getKey(), entry.getValue());
            }
        }
        nq.executeUpdate();
    }
    
    public <T> T findEntity(Class<T> clazz,String query, HashMap<String, Object> params) {
        Query nq = em.createNamedQuery(query);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nq.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return clazz.cast(nq.getSingleResult());
    }

}
