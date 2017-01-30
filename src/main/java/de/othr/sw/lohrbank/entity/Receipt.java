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
    private String message;
    private Long accountFromId;
    private Long accountToId;
    private TransactionType transactionType;
    private double value;
    private Date date;
    private boolean success;
    
    public Receipt(){
    }
    
    public Receipt(String message){
        this.message = message;
    }
    
    public Receipt(Transaction transaction)
    {
        this.message = transaction.getMessage();
        this.accountFromId = transaction.accountFrom.getId();
        this.accountToId = transaction.accountTo.getId();
        this.date = transaction.getTransactionDate();
        this.value = transaction.getTransactionValue();
        this.success = true;
    }
    
    public Receipt(Debit debit)
    {
        this.message = debit.getMessage();
        this.accountFromId = debit.accountFrom.getId();
        this.accountToId = debit.accountTo.getId();
        this.date = debit.getTransactionDate();
        this.value = debit.getTransactionValue();
        this.transactionType = TransactionType.Debit;
        this.success = true;
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
