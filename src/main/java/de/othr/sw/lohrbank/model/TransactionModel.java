/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.model;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.AccountInfo;
import de.othr.sw.lohrbank.entity.Debit;
import de.othr.sw.lohrbank.entity.Receipt;
import de.othr.sw.lohrbank.entity.Transaction;
import de.othr.sw.lohrbank.entity.converter.AccountConverter;
import de.othr.sw.lohrbank.service.AccountService;
import de.othr.sw.lohrbank.service.TransactionService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private AccountConverter accountConverter;
    
    private String message;
    private double value;
    private Long targetAccountId;    
    private Long customerAccountId;
    private Date transactionDate;
    
    private Transaction lastTransaction;
    private Debit lastDebit;
    private Receipt lastReceipt;

    /**
     * Creates an new transaction from the values entered in the register form.
     * @return 
     */
    public String CreateTransaction(){
        this.lastReceipt = this.transactionService.CreateTransaction(this.customerModel.getCurrentSession(), this.customerAccountId, this.targetAccountId, this.message, this.value);
        
        // ToDo: handle results from receipt.
        if(this.lastReceipt.isSuccess()){
            return "transactionCreated";
        }

        return "transactionCreationFailed";
    }

    /**
     * Creates an new debit from the values entered in the register form.
     * @return 
     */
    public String CreateDebit(){
        this.lastReceipt = this.transactionService.CreateDebit(this.customerAccountId, this.targetAccountId, this.message, this.value, this.transactionDate);
        
        if(this.lastReceipt.isSuccess()){
            this.accountService.CheckForDebits(this.customerModel.getCurrentSession());
            return "debitCreated";
        }
        
        return "debitCreationFailed";
    }
            
    /**
     * Get the last transactions of account Y from a selected timespan
     * @param accountId
     * @param from
     * @param to
     * @return List of Transactions from this timespan.
     */
    public List<Transaction> GetLastTransactions(Long accountId, Date from, Date to){
        List<Transaction> list = new ArrayList<>();
        AccountInfo info = this.accountService.GetAccountInfo(accountId, from, to);
        
        if(info != null && !info.getTransactions().isEmpty()){
            list = info.getTransactions();
        }
        
        return list;
    }
    
    /**
     * Gets the transactions of the last 30 days from a certain account.
     * @param accountId
     * @return List of Transactions from this timespan.
     */
    public List<Transaction> GetLastTransactions(Long accountId){
        int daysBack = 30;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -daysBack);
        return this.GetLastTransactions(accountId, calendar.getTime(), new Date());
    }
    
    /**
     * Sets the direction of the transaction arrow to left for outgoing or red for incoming transactions
     * @param accountId
     * @param transaction
     * @return 
     */
    public String GetTransactionDirectionClass(Long accountId, Transaction transaction){
        if(this.IsTransactionFromAccount(accountId, transaction)){
            return "glyphicon-arrow-left";
        }
        return "glyphicon-arrow-right";
    }
    
    /**
     * Sets the color of the transaction value to green for positive or red for negative
     * @param accountId
     * @param transaction
     * @return 
     */
    public String GetTransactionValueClass(Long accountId, Transaction transaction){
        if(this.IsTransactionFromAccount(accountId, transaction)){
            return "color:red";
        }
        return "color:green";
    }
    
    /**
     * Checks if a transaction was made from a certain account
     * @param accountId
     * @param transaction
     * @return A value which indicates whether the transacton was done from a certain account or not.
     */
    private boolean IsTransactionFromAccount(Long accountId, Transaction transaction){
        return transaction.getAccountFrom().getId().equals(accountId);
    }
    
    /**
     * Navigates to transaction creation.
     * @return 
     */
    public String ShowTransactionCreation(){
        this.ResetForm();
        return "showTransactionCreation";
    }
    
    /**
     * Navigates to debit creation.
     * @return 
     */
    public String ShowDebitCreation(){
        this.ResetForm();
        return "showDebitCreation";
    }
    
    /**
     * Resets the form for the next cycle.
     */
    private void ResetForm(){
        this.message = "";
        this.value = 0;
        this.targetAccountId = new Long(0);
        this.customerAccountId = new Long(0);
        this.transactionDate = new Date();
    }
    
    public List<Account> GetAllAccounts(){
        List<Account> check = this.accountService.GetAllAccounts(this.customerModel.getCurrentSession());
        
        return check;
    }
    
    /**
     * Checks if the current user has any accounts registered.
     * @return a value wether a customer has a minimum of one account.
     */
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

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
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

    public Receipt getLastReceipt() {
        return lastReceipt;
    }

    public void setLastReceipt(Receipt lastReceipt) {
        this.lastReceipt = lastReceipt;
    }  
}
