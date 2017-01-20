/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.model;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.Debit;
import de.othr.sw.lohrbank.entity.Receipt;
import de.othr.sw.lohrbank.entity.Transaction;
import de.othr.sw.lohrbank.entity.converter.AccountConverter;
import de.othr.sw.lohrbank.service.AccountService;
import de.othr.sw.lohrbank.service.TransactionService;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Michael
 */
@Named
@SessionScoped
public class TransactionModel implements Serializable {
       
    @Inject
    private AccountService accountService;
    
    @Inject
    private TransactionService transactionService;
    
    @Inject
    private CustomerModel customerModel;
    
    @Inject
    private AccountModel accountModel;

    @Inject
    private AccountConverter accountConverter;
    
    private String message;
    private double value;
    private Long targetAccountId;    
    private Long customerAccountId;
    
    private Transaction lastTransaction;
    private Debit lastDebit;
    private Receipt lastReceipt;

    /// Creates an new transaction from the values entered in the register form.
    public String CreateTransaction(){
        this.lastTransaction = new Transaction(this.customerModel.getCurrentSession().getCustomerId(), this.message, this.customerAccountId, this.targetAccountId, this.value);
        
        if(this.lastTransaction != null){
            this.lastReceipt = this.transactionService.CreateTransaction(this.lastTransaction);
            
            // ToDo: handle results from receipt.
            if(this.lastReceipt.isSuccess()){
                return "transactionCreated";
            }
        }
        
        return "transactionCreationFailed";
    }

    /// Creates an new debit from the values entered in the register form.
    public String CreateDebit(){
        
        if(this.lastTransaction != null){
            return "debitCreated";
        }
        
        return "debitCreationFailed";
    }
            
    public List<Transaction> GetLastTransactions(int number){
        
        return null;
    }
    
    public String ShowTransactionCreation(){
        return "showTransactionCreation";
    }
    
    public String ShowDebitCreation(){
        return "showDebitCreation";
    }
    
    public List<Account> GetAllAccounts(){
        List<Account> check = this.accountService.GetAllAccounts(this.customerModel.getCurrentSession());
        
        return check;
    }
    
    /// Checks if the current user has any accounts registered.
    public boolean HasAccounts(){
        List<Account> check = this.accountService.GetAllAccounts(this.customerModel.getCurrentSession());
        
        return check != null && !check.isEmpty();
    }
    
    /// Getter / Setter

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public Long getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(Long customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public Transaction getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(Transaction lastTransaction) {
        this.lastTransaction = lastTransaction;
    }

    public Debit getLastDebit() {
        return lastDebit;
    }

    public void setLastDebit(Debit lastDebit) {
        this.lastDebit = lastDebit;
    }

    public AccountConverter getAccountConverter() {
        return accountConverter;
    }

    public void setAccountConverter(AccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }
   
}
