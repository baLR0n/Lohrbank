/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Customer;

/**
 *
 * @author Michael
 */
public interface ICustomerService {
    Customer CreateCustomer(Customer customer);
    Customer CheckCustomerAuth(String customerId, String password);
    
    boolean IdentityRequest(String id, String name, String firstName);
}
