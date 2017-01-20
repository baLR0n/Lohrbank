/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Account;
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
    public Receipt CreateTransaction(Long customerAccountId, Long targetAccountId, String message, double value){
        Account accountFrom = entityManager.find(Account.class, customerAccountId);
        Account accountTo = entityManager.find(Account.class, targetAccountId);
        
        Transaction transaction = new Transaction(accountFrom, accountTo, message, value);
        
        // Can the customer afford to send that much money?
        // -Double.MAX_VALUE is returned when the money transfer cannot be made.
        if(this.accountService.ChangeAccountBalance(accountFrom.getId(), -transaction.getTransactionValue()) != -Double.MAX_VALUE){
            // Alter target account balance.
            this.accountService.ChangeAccountBalance(accountTo.getId(), transaction.getTransactionValue());
            
            // Persist new transaction.
            entityManager.persist(transaction);    
            
            // Create and return receipt.
            return new Receipt(transaction);
        }

        // Parameterless constructor -> success = false.
        return new Receipt();
    }
    
//    @Transactional
//    @Override
//    public Receipt CreateTransaction(Transaction transaction) {
//        // ToDo:Check if possible and then alter values of accounts.
////        Account accountFrom = entityManager.find(Account.class, transaction.getAccountFromId());
////        Account accountTo = entityManager.find(Account.class, transaction.getAccountToId());
//        
//        // Can the customer afford to send that much money?
//        // -Double.MAX_VALUE is returned when the money transfer cannot be made.
//        if(this.accountService.ChangeAccountBalance(accountFrom.getId(), -transaction.getTransactionValue()) != -Double.MAX_VALUE){
//            // Alter target account balance.
//            this.accountService.ChangeAccountBalance(accountTo.getId(), transaction.getTransactionValue());
//            
//            // Persist new transaction.
//            entityManager.persist(transaction);    
//            
//            // Create and return receipt.
//            return new Receipt(transaction);
//        }
//
//        // Parameterless constructor -> success = false.
//        return new Receipt();
//    }
    
    @Transactional
    @Override
    public Receipt CreateDebit(Debit debit){
        // Persist new customer.
        entityManager.persist(debit);
        
        // Create and return receipt.
        return new Receipt(debit);
    }
}
