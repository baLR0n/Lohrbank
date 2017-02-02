/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.pvergleich.service.ItemsEntry;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;

/**
 *
 * @author Michael
 */
@RequestScoped
@Alternative
public class ItemLookUpServiceMock implements IItemLookUpService{
    
    @Override
    public ItemsEntry GetCheapestItemFor(String searchTerm) {
        ItemsEntry mock = new ItemsEntry();
        mock.setEntryId("123456");
        mock.setItemId("123456");
        mock.setPrice(13.37);
        mock.setShopID("12345");

        return mock;
    }
}
