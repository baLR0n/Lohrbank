/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import service.Product;

/**
 *
 * @author Michael
 */
@RequestScoped
@Alternative
public class ItemOrderServiceMock implements IItemOrderService{
    
    @Override
    public List<Product> OrderItems(String authToken, List<String> orderIds) {
        List<Product> list = new ArrayList<>();
        
        for(String id : orderIds){
            Product product = new Product();
            product.setGlobalTradeIdentificationNumber(id);
            product.setProductPrice(10);
            product.setProductname("Mock-Produkt");
            
            list.add(product);
        }
        
        return list;
    }
}
