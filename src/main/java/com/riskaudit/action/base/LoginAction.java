package com.riskaudit.action.base;

import com.riskaudit.entity.base.User;
import com.riskaudit.enums.UserType;
import com.riskaudit.util.Helper;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author asenturk
 */
@ManagedBean
@RequestScoped
public class LoginAction implements Serializable {

    @Inject
    private EntityManager em;

    @Inject
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String doLogin() {
        try {
                        
            /***** login için spring security alternatif picketlink*/
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");
            dispatcher.forward((ServletRequest) context.getRequest(),(ServletResponse) context.getResponse());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (ServletException ex) {
            Logger.getLogger(LoginAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public String doLogOut() throws IOException, ServletException {
        
        ExternalContext context
                = FacesContext.getCurrentInstance().getExternalContext();

        RequestDispatcher dispatcher
                = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_logout");

        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());

        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }

    public void userSet(){
        try{
            User userTmp = em.createNamedQuery("User.findUser",User.class).setParameter("email",user.getEmail()).getSingleResult();
            if (null != user) {
                if (userTmp.getPassword().equals(user.getPassword())) {
                    if (null != userTmp) {
                        int rndm = (int)(Math.random()*1000);
                        user = userTmp;
                        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                        session.setAttribute("user",user);
                        session.setAttribute(Helper.SES_SECRET_KEY,user.getEmail()+rndm);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            Helper.addMessage("Kullanici bulunamadı!");
        }
    }

    public void injectUser() {
        
        if (user==null || user.getEmail()==null) {
            System.out.println("SecurityContextHolder.getContext().getAuthentication().getName()..:" + SecurityContextHolder.getContext().getAuthentication().getName());
            user =  em.createNamedQuery("User.findUser",User.class).setParameter("email",SecurityContextHolder.getContext().getAuthentication().getName()).getSingleResult();
        }
    }

    public void checkUser() {

        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            NavigationHandler nh = FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            nh.handleNavigation(FacesContext.getCurrentInstance(), null, "/app/index.xhtml?faces-redirect=true");
        }
    }
    
    public String getLoginUsername(){
        String currentUser = "";
        
        User userLog =  Helper.getCurrentUserFromSession();
        if(userLog!=null){
            currentUser = userLog.getFirstname()+ " " + userLog.getLastname();
        }
        
        userLog = null;
       
        return currentUser;
    }
    
    public void redirectUser() throws IOException {

        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            NavigationHandler nh = FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            if(user==null || user.getEmail()==null)injectUser();
            
            if(user!=null && user.getMerchant()!=null){
                boolean isValid = Helper.checkUserLicense(user.getMerchant().getMerchantName(),user.getMerchant().getLicenseExpireDate(),user.getMerchant().getLicenseHash());
                if(!isValid){
                    SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                    nh.handleNavigation(FacesContext.getCurrentInstance(), null, "/expireLicense.xhtml?faces-redirect=true");
                    return;
                }
            }
            if(user!=null && user.getUserType()!=null && user.getUserType().equals(UserType.ADMIN)){
                nh.handleNavigation(FacesContext.getCurrentInstance(), null, "/app/index.xhtml?faces-redirect=true");
                return;
            }else{
                nh.handleNavigation(FacesContext.getCurrentInstance(), null, "/app/index.xhtml?faces-redirect=true");
                return;
            }
        }
    }
}
