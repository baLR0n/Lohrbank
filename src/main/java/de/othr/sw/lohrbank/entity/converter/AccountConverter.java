/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.entity.converter;

import de.othr.sw.lohrbank.entity.Account;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Michael
 */
@SessionScoped
@FacesConverter("de.othr.sw.lohrbank.entity.converter.AccountConverter")
public class AccountConverter implements Converter, Serializable {

    @PersistenceContext(unitName="Lohrbank")
    private EntityManager entityManager;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null)
        {
            return "";
        }
        
        Account account = this.entityManager.find(Account.class, value);
        if(account != null)
        {
            return account;
        }
        
        return "";
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null)
        {
            return "";
        }
        
        Account account = this.entityManager.find(Account.class, value);
        if(account != null)
        {
            return account.getName();
        }
        
        return "";
    }
    
}
