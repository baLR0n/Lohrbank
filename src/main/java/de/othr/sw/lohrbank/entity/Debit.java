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

    /// Creates a debit transaction set on specified date.
    public Debit(String customerId, String message, Long accountFromId, Long accountToId, float value, Date date)
    {
        super(customerId, message, accountFromId, accountToId, value);
        this.transactionDate = date;
    }
}
