/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.entity;

import de.othr.sw.lohrbank.entity.util.RandomIdEntity;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Michael
 */
@Entity
@Table(name="Accounts")
public class Account extends RandomIdEntity implements Serializable{
    
    private String name;
    private double balance;
    private double disposition;
    
    @ManyToOne
    private Customer owner;
    
    @OneToMany(mappedBy="accountFrom")
    private Set<Transaction> ownTransactions;

    @OneToMany(mappedBy="accountTo")
    private Set<Transaction> transactions;
    
    public Account(){
    }
    
    public Account(Long id){
        this.id = id;
    }

    // Getter / Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getDisposition() {
        return disposition;
    }

    public void setDisposition(double disposition) {
        this.disposition = disposition;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }
}
