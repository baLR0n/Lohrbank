/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.entity;

import de.othr.sw.lohrbank.entity.util.GeneratedIdEntity;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Michael
 */
@Entity
@Table(name="Transactions")
public class Transaction extends GeneratedIdEntity implements Serializable{
        
    @ManyToOne
    @JoinColumn(name="client_id")
    protected Account accountFrom; 
    
    @ManyToOne
    @JoinColumn(name="target_id")
    protected Account accountTo;
    
    protected String message;
    protected double transactionValue;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date transactionDate;
    
    public Transaction(){}
    
    /// Creates a transaction.
    public Transaction(Account accountFrom, Account accountTo, String message, double value){
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.message = message;
        this.transactionValue = value;
        this.transactionDate = new Date();
    }
    
    /**
     * Returns the date as a formated string
     * @return 
     */
    public String GetFormatedDateString(){
        Format format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.format(this.transactionDate);
    }
    
    /**
     * Returns the name of the other involved customer.
     * @param accountId
     * @return 
     */
    public String GetOtherCustomerName(Long accountId){
        if(this.accountFrom.getId().equals(accountId)){
            return String.format("%s %s (Knt.Nr: %s)", this.accountTo.getOwner().getFirstName(), this.accountTo.getOwner().getName(), this.accountTo.getId().toString());
        }
        else{
            return String.format("%s %s (Knt.Nr: %s)", this.accountFrom.getOwner().getFirstName(), this.accountFrom.getOwner().getName(), this.accountFrom.getId().toString());
        }
    }
        
    // Getter / Setter

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public double getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(double transactionValue) {
        this.transactionValue = transactionValue;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    
 
}
