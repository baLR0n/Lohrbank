/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.pvergleich.service.ItemsEntry;

/**
 *
 * @author Michael
 */
public interface IItemLookUpService {
    ItemsEntry GetCheapestItemFor(String searchTerm);
}
