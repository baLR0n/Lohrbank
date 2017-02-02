/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.pvergleich.service.DtoItem;
import de.othr.sw.pvergleich.service.IItemsLookUpService;
import de.othr.sw.pvergleich.service.IItemsLookUpServiceService;
import de.othr.sw.pvergleich.service.ItemsEntry;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Michael
 */
@RequestScoped
@Alternative
public class ItemLookUpService implements IItemLookUpService{
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/im-lamport_8080/PVergleich/IItemsLookUpService.wsdl")
    private IItemsLookUpServiceService itemsLookUpService;

    /**
     * Gets the cheapest item for a specific search term.
     * @param searchTerm
     * @return 
     */
    @Override
    public ItemsEntry GetCheapestItemFor(String searchTerm) {
        try
        {
            IItemsLookUpService lookUpPort = itemsLookUpService.getIItemsLookUpServicePort();

            // process result here
            List<DtoItem> result = lookUpPort.searchItemsWithShopList(searchTerm);
            return this.GetCheapestItem(result);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
            
    /**
     * Returns the cheapest item from a list.
     * @param items
     * @return 
     */
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
}
