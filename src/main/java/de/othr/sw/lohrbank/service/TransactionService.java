/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.entity.Debit;
import de.othr.sw.lohrbank.entity.Receipt;
import de.othr.sw.lohrbank.entity.Transaction;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author Michael
 */
@WebService
@RequestScoped
public class TransactionService implements ITransactionService {
    @PersistenceContext(unitName="Lohrbank")
    private EntityManager entityManager;
        
    @Inject
    private AccountService accountService;
    
    @Transactional
    @WebMethod(exclude=true)
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
    
    @WebMethod
    @Transactional
    @Override
    public Receipt CreateTransaction(
            @WebParam(name="customer")Customer customer, 
            @WebParam(name="customerAccountId") Long customerAccountId,
            @WebParam(name="targetAccountId") Long targetAccountId,
            @WebParam(name="message") String message, 
            @WebParam(name="value") double value)
    {
        customer = this.entityManager.merge(customer);
        
        Account accountFrom = entityManager.find(Account.class, customerAccountId);
        if(customer.getId().equals(accountFrom.getOwner().getId())){
            return this.CreateTransaction(customerAccountId, targetAccountId, message, value);
        }
        
        return new Receipt();
    }
    
    @WebMethod
    @Transactional
    @Override
    public Receipt CreateDebit(
            @WebParam(name="customer")Customer customer, 
            @WebParam(name="customerAccountId") Long customerAccountId,
            @WebParam(name="targetAccountId") Long targetAccountId,
            @WebParam(name="message") String message, 
            @WebParam(name="value") double value,
            @WebParam(name="date") Date date)
    {
        customer = this.entityManager.merge(customer);
        
        Account accountFrom = entityManager.find(Account.class, customerAccountId);
        if(customer.getId().equals(accountFrom.getOwner().getId())){
            return this.CreateDebit(customerAccountId, targetAccountId, message, value, date);
        }
        
        return new Receipt();
    }
    
    @Transactional
    @WebMethod(exclude=true)
    public Receipt CreateDebit(Long customerAccountId, Long targetAccountId, String message, double value, Date date){
        Account accountFrom = entityManager.find(Account.class, customerAccountId);
        Account accountTo = entityManager.find(Account.class, targetAccountId);
        
        Debit debit = new Debit(accountFrom, accountTo, message, value, date);

        // Persist new debit.
        entityManager.persist(debit);
            
        return new Receipt(debit);
    }
}
