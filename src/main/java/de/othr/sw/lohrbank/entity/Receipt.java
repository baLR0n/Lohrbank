/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.entity;

import java.util.Date;

/**
 *
 * @author Michael
 */
public class Receipt {
    private String customerId;
    private String message;
    private Long accountFromId;
    private Long accountToId;
    private TransactionType transactionType;
    private double value;
    private Date date;
    private boolean success;
    
    public Receipt(){
    }
    
    public Receipt(Transaction transaction)
    {
        this.customerId = transaction.getCustomerId();
        this.message = transaction.getMessage();
        this.accountFromId = transaction.getAccountFromId();
        this.accountToId = transaction.getAccountToId();
        this.accountFromId = transaction.getAccountFromId();
        this.date = transaction.getTransactionDate();
        this.value = transaction.getTransactionValue();
        this.success = true;
    }
    
    public Receipt(Debit debit)
    {
        this.customerId = debit.getCustomerId();
        this.message = debit.getMessage();
        this.accountFromId = debit.getAccountFromId();
        this.accountToId = debit.getAccountToId();
        this.accountFromId = debit.getAccountFromId();
        this.date = debit.getTransactionDate();
        this.value = debit.getTransactionValue();
        this.transactionType = TransactionType.Debit;
        this.success = true;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Long getAccountFromId() {
        return accountFromId;
    }

    public Long getAccountToId() {
        return accountToId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
