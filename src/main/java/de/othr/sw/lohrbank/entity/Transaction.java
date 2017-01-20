/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.entity;

import de.othr.sw.lohrbank.entity.util.GeneratedIdEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
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
    
    protected String customerId;
    protected String message;
    protected Long accountFromId; 
    protected Long accountToId;
    protected double transactionValue;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date transactionDate;
    
    /// Creates a transaction.
    public Transaction(String customerId, String message, Long accountFromId, Long accountToId, double value){
        this.customerId = customerId;
        this.message = message;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.transactionValue = value;
        this.transactionDate = new Date();
    }
        
    // Getter
    
    public String getCustomerId() {
        return customerId;
    }

    public String getMessage() {
        return message;
    }
    
    public Long getAccountFromId() {
        return accountFromId;
    }

    public Long getAccountToId() {
        return accountToId;
    }

    public double getTransactionValue() {
        return transactionValue;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }
}
