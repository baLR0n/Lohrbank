/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Michael
 */
@Entity
@Table(name="Debits")
public class Debit extends Transaction {

    private boolean isCompleted;

    public Debit() {
    }
    
    /**
     * Creates a debit transaction set on specified date.
     * @param accountFrom
     * @param accountTo
     * @param message
     * @param value
     * @param date 
     */
    public Debit(Account accountFrom, Account accountTo, String message, double value, Date date)
    {
        super(accountFrom, accountTo, message, value);
        this.transactionDate = date;
    }

    // Getter / Setter
    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
