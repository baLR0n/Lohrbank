/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.webservice.IDCardService;
import de.othr.sw.lohrbank.webservice.IdentityService;
import javax.enterprise.context.RequestScoped;
import javax.jws.WebMethod;
import javax.jws.WebService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author Michael
 */
@WebService
@RequestScoped
public class CustomerService implements ICustomerService {
    
    @PersistenceContext(unitName="Lohrbank")
    private EntityManager entityManager;
        
    @Transactional
    @Override
    public Customer CreateCustomer(Customer customer) {
        try
        {
            // Persist new customer.
            entityManager.persist(customer);
            return customer;
        }
        catch (Throwable t) 
        {
            return null;
        }
    }
    
    /**
     * Give your customer ID (e-mail address) and your password and check if the authorization is granted.
     *
     * @param customerId
     * @param password
     * @return Customer object to continue web banking.
     */
    @WebMethod
    @Override
    public Customer CheckCustomerAuth(String customerId, String password){
        Customer customer = entityManager.find(Customer.class, customerId);
        
        if(customer != null && customer.getPassword().equals(password)){
            return customer;
        }
        
        return null;
    }
    
    @Override
    public boolean IdentityRequest(String id, String name, String firstName){   
        
        try { // Call Web Service Operation
            IdentityService service = new IdentityService();
            IDCardService port = service.getIDCardServicePort();
            
            // TODO initialize WS operation arguments here
            String idCardNumber = id;
            String firstname = firstName;
            String lastname = name;
            
            // TODO process result here
            boolean result = port.identityRequest(idCardNumber, firstname, lastname);
            return result;
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }

        return false;
    }
}
