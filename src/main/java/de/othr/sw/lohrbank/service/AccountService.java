/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.AccountInfo;
import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.entity.Transaction;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

/**
 *
 * @author Michael
 */
@SessionScoped
public class AccountService implements IAccountService, Serializable{
    
    @PersistenceContext(unitName="Lohrbank")
    private EntityManager entityManager;
    
    @Transactional
    @Override
    public Account CreateAccount(Customer customer, String name) {
        return this.CreateAccount(customer, name, 0, 500); // 500€ default disposition;
    }
    
    @Transactional
    @Override
    public Account CreateAccount(Customer customer, String name, double balance, double disposition) {
        customer = entityManager.merge(customer);
        
        Account account = new Account();
        account.setOwner(customer);
        account.setName(name);
        account.setBalance(balance);
        account.setDisposition(disposition);
        entityManager.persist(account);
        
        return account;
    }

    @Transactional
    @Override
    public Account CreateAccount(Customer customer, Long id, String name, double balance, double disposition) {
        customer = entityManager.merge(customer);
        
        Account account = new Account(id);
        account.setOwner(customer);
        account.setName(name);
        account.setBalance(balance);
        account.setDisposition(disposition);
        entityManager.persist(account);
        
        return account;
    }

    @Transactional
    @Override
    public double ChangeAccountBalance(int accountId, double value) {
        Account account = entityManager.find(Account.class, accountId);
        
        if(account != null)
        {
            double currentBalance = account.getBalance();

            // Check if the customer is allowed to proceed with this balance change.
            if(currentBalance + value > account.getDisposition()){
                account.setBalance(currentBalance + value);
                entityManager.persist(account);
                return account.getBalance();
            }
            else{
                // ToDo: change return value from float to Response object.
            }
        }
        
        return 0;
    }

    @Transactional
    @Override
    public double ChangeAccountDisposition(int accountId, double disposition) {
        Account account = entityManager.find(Account.class, accountId);
        
        if(account != null)
        {
            account.setDisposition(disposition);
            entityManager.persist(account);
            
            return account.getDisposition();
        }
        
        return 0;
    }

    @Transactional
    @Override
    public AccountInfo GetAccountInfo(int accountId, Date from, Date to) {
        Account account = entityManager.find(Account.class, accountId);
        AccountInfo info = new AccountInfo();
        
        if(account != null)
        {
            info.setAccountId(accountId);
            info.setDateFrom(from);
            info.setDateTo(to);
            
            // Get desired transactions from this timespan.
            Query q = entityManager.createQuery("SELECT t FROM Transactions as t WHERE t.accountidto = :accountId");
            q.setParameter("accountId", accountId);
            
            List<Transaction> transactionList = q.getResultList();
            
            info.setTransactions(transactionList);
        }
        
        return info;
    }

    @Transactional
    @Override
    public List<Account> GetAllAccounts(Customer customer) {
        customer = entityManager.merge(customer);
        
        TypedQuery<Account> query = this.entityManager.createQuery("SELECT a FROM Account AS a WHERE owner_id = :customerid", Account.class);
        query.setParameter("customerid", customer.getId());

        return query.getResultList();
    }
}
