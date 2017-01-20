/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Debit;
import de.othr.sw.lohrbank.entity.Receipt;
import de.othr.sw.lohrbank.entity.Transaction;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author Michael
 */
@RequestScoped
public class TransactionService implements ITransactionService {
    @PersistenceContext(unitName="Lohrbank")
    private EntityManager entityManager;
        
    @Inject
    private AccountService accountService;
    
    @Transactional
    @Override
    public Receipt CreateTransaction(Transaction transaction) {
        // ToDo:Check if possible and then alter values of accounts.
        if(true){
            
        // Persist new transaction.
        entityManager.persist(transaction);
        
        // Create and return receipt.
        return new Receipt(transaction);
        }

        // Parameterless constructor -> success = false.
        return new Receipt();
    }
    
    @Transactional
    @Override
    public Receipt CreateDebit(Debit debit){
        // Persist new customer.
        entityManager.persist(debit);
        
        // Create and return receipt.
        return new Receipt(debit);
    }
}
