/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.AccountInfo;
import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.entity.Debit;
import de.othr.sw.lohrbank.entity.Transaction;
import java.io.Serializable;
import java.util.ArrayList;
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
        customer = this.entityManager.merge(customer);
        
        Account account = new Account();
        account.setOwner(customer);
        account.setName(name);
        account.setBalance(balance);
        account.setDisposition(disposition);
        this.entityManager.persist(account);
        
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
        this.entityManager.persist(account);
        
        return account;
    }

    @Transactional
    @Override
    public double ChangeAccountBalance(Long accountId, double value) {
        Account account = this.entityManager.find(Account.class, accountId);
        
        if(account != null)
        {
            double currentBalance = account.getBalance();

            // Check if the customer is allowed to proceed with this balance change.
            if(currentBalance + value > -account.getDisposition()){
                account.setBalance(currentBalance + value);
                this.entityManager.persist(account);
                return account.getBalance();
            }
            else{
                return -Double.MAX_VALUE;
            }
        }
        
        return -Double.MAX_VALUE;
    }

    @Transactional
    @Override
    public double ChangeAccountDisposition(Long accountId, double disposition) {
        Account account = this.entityManager.find(Account.class, accountId);
        
        if(account != null)
        {
            account.setDisposition(disposition);
            this.entityManager.persist(account);
            
            return account.getDisposition();
        }
        
        return 0;
    }

    @Transactional
    @Override
    public AccountInfo GetAccountInfo(Long accountId, Date from, Date to) {
        Account account = this.entityManager.find(Account.class, accountId);
        AccountInfo info = new AccountInfo();
        
        if(account != null)
        {
            info.setAccountId(accountId);
            info.setDateFrom(from);
            info.setDateTo(to);
            
            // Get transactions from this account.
            Query q = this.entityManager.createQuery("SELECT t FROM Transaction AS t WHERE target_id = :accountId OR client_id = :accountId", Transaction.class);
            q.setParameter("accountId", accountId);
            
            List<Transaction> transactionList = q.getResultList();
            
            info.setTransactions(transactionList);
        }
        
        return info;
    }

    @Transactional
    @Override
    public List<Account> GetAllAccounts(Customer customer) {
        customer = this.entityManager.merge(customer);
        
        TypedQuery<Account> query = this.entityManager.createQuery("SELECT a FROM Account AS a WHERE owner_id = :customerid", Account.class);
        query.setParameter("customerid", customer.getId());

        return query.getResultList();
    }
    
    @Transactional
    @Override
    public void CheckForDebits(Customer customer){
        if(customer == null)
        {
            return;
        }
        
        customer = this.entityManager.merge(customer);
        
        List<Account> accounts = this.GetAllAccounts(customer);
        List<Debit> debits = new ArrayList<>();
        
        // Get all accounts from this customer and check for debits.
        accounts.stream().map((account) -> {
            TypedQuery<Debit> query = this.entityManager.createQuery("SELECT d FROM Debit AS d WHERE client_id = :accountid OR target_id = :accountid", Debit.class);
            query.setParameter("accountid", account.getId());
            return query;
        }).forEach((query) -> {
            debits.addAll(query.getResultList());
        });
        
        // Check all debits if they were processed yet.
        // If not, do it if the time has come.
        debits.stream().map((debit) -> this.entityManager.merge(debit)).filter((debit) -> (!debit.isIsCompleted() && debit.getTransactionDate().compareTo(new Date()) <= 0)).map((debit) -> {
            this.ChangeAccountBalance(debit.getAccountFrom().getId(), -debit.getTransactionValue());
            return debit;
        }).map((debit) -> {
            this.ChangeAccountBalance(debit.getAccountTo().getId(), debit.getTransactionValue());
            return debit;
        }).map((debit) -> {
            debit.setIsCompleted(true);
            return debit;
        }).forEach((debit) -> {
            this.entityManager.persist(debit);
        });
    }
}
