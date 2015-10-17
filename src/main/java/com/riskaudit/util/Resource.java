package com.riskaudit.util;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asenturk
 */
public class Resource {

    @Produces
    @PersistenceContext
    EntityManager em;
    
    public static String    PROJECT_WEBSERVICE_PATH="http://localhost:8080/Patient/rest";
}
