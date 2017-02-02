/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.webservice.IDCardService;
import de.othr.sw.lohrbank.webservice.IdentityService;
import de.othr.sw.pvergleich.service.ItemsEntry;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
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

    @Inject
    private IItemLookUpService itemLookUpService;
    
    @Inject
    private IItemOrderService itemOrderService;
    
    @PersistenceContext(unitName="Lohrbank")
    private EntityManager entityManager;
        
    /**
     * Creates a new customer
     * @param customer
     * @return 
     */
    @Transactional
    @WebMethod(exclude=true)
    @Override
    public Customer CreateCustomer(@WebParam(name="customer") Customer customer) {
        // Persist new customer.
        entityManager.persist(customer);

        // Order TAN device
        // Get cheapest TAN Generator item
        ItemsEntry toOrder = this.itemLookUpService.GetCheapestItemFor("TAN");

        // Order that
        String authToken = "Bank1234567";
        List<String> orderIds = new ArrayList<>();
        orderIds.add(toOrder.getItemId());

        this.itemOrderService.OrderItems(authToken, orderIds);

        return customer;
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
    public Customer CheckCustomerAuth(@WebParam(name="customerId") String customerId, @WebParam(name="password") String password){
        Customer customer = entityManager.find(Customer.class, customerId);
        
        if(customer != null && customer.getPassword().equals(password)){
            return customer;
        }
        
        return null;
    }
    
    /**
     * Checks the identity of a potential new customer
     * @param id
     * @param name
     * @param firstName
     * @return 
     */
    @Override
    @WebMethod(exclude=true)
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
