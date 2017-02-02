/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.xml.ws.WebServiceRef;
import service.OrderService;
import service.OrderServiceService;
import service.Product;

/**
 *
 * @author Michael
 */
@RequestScoped
@Alternative
public class ItemOrderService implements IItemOrderService{
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/im-lamport_8080/SimonWebshop/OrderService.wsdl")
    private OrderServiceService orderService;

    /**
     * Orders a list of items.
     * @param authToken
     * @param orderIds
     * @return 
     */
    @Override
    public List<Product> OrderItems(String authToken, List<String> orderIds) {
        
        try
        {
            OrderService orderPort = orderService.getOrderServicePort();
            return orderPort.buyProduct(authToken, orderIds);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}
