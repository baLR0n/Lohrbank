/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import java.util.List;
import service.Product;

/**
 *
 * @author Michael
 */
public interface IItemOrderService {
    List<Product> OrderItems(String authToken, List<String> orderIds);
}
