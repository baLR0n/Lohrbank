/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.webservice.IDCardService;
import de.othr.sw.lohrbank.webservice.IdentityService;
import de.othr.sw.pvergleich.service.DtoItem;
import de.othr.sw.pvergleich.service.IItemsLookUpService;
import de.othr.sw.pvergleich.service.IItemsLookUpServiceService;
import de.othr.sw.pvergleich.service.ItemsEntry;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.xml.ws.WebServiceRef;
import service.OrderService;
import service.OrderServiceService;

/**
 *
 * @author Michael
 */
@WebService
@RequestScoped
public class CustomerService implements ICustomerService {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/im-lamport_8080/SimonWebshop/OrderService.wsdl")
    private OrderServiceService orderService;

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/im-lamport_8080/PVergleich/IItemsLookUpService.wsdl")
    private IItemsLookUpServiceService itemsLookUpService;
    
    @PersistenceContext(unitName="Lohrbank")
    private EntityManager entityManager;
        
    @Transactional
    @WebMethod(exclude=true)
    @Override
    public Customer CreateCustomer(@WebParam(name="customer") Customer customer) {
        try
        {
            // Persist new customer.
            entityManager.persist(customer);
            
            // Order TAN device
            // First, get cheapest offer.
            try {
                IItemsLookUpService lookUpPort = itemsLookUpService.getIItemsLookUpServicePort();
                // TODO initialize WS operation arguments here
                String searchString = "TAN";
                // TODO process result here
                List<DtoItem> result = lookUpPort.searchItemsWithShopList(searchString);
                
                // Select cheapest item
                ItemsEntry toOrder = this.GetCheapestItem(result);                                
                System.out.println("Result = "+result);  
                
                // Order that
                OrderService orderPort = orderService.getOrderServicePort();
                // TODO initialize WS operation arguments here
                String authToken = "Bank1234567";
                List<String> orderIds = new ArrayList<>();
                orderIds.add(toOrder.getItemId());

                // TODO process result here
                List<service.Product> orderResult = orderPort.buyProduct(authToken, orderIds);
                System.out.println("Result = "+orderResult);
            } catch (Exception ex) {
                // TODO handle custom exceptions here
            }

            return customer;
        }
        catch (Throwable t) 
        {
            return null;
        }
    }
    
    private ItemsEntry GetCheapestItem(List<DtoItem> items){
        ItemsEntry lowest = null;
        for(DtoItem item : items){ // Iterate through item collections by shop
            for(ItemsEntry entry : item.getEntryList()){ // Iterate through each item from a shop
                if(lowest == null || lowest.getPrice() > entry.getPrice()){ // Check if current item is the cheapest.
                    lowest = entry;
                }
            }
        }
        
        return lowest;
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
