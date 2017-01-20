/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.model;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.service.AccountService;
import de.othr.sw.lohrbank.service.CustomerService;

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
public class AccountModel implements Serializable {
    
    @Inject
    private CustomerService customerService;
    
    @Inject
    private AccountService accountService;
    
    @Inject
    private CustomerModel customerModel;
            
    private String accountName;
    private double disposition;
    private Account lastCreated;

    /// Creates an new account from the values entered in the register form.
    public String CreateAccount(){
        this.lastCreated = this.accountService.CreateAccount(this.customerModel.getCurrentSession(), this.accountName, 0, this.disposition);
        
        if(this.lastCreated != null){
            return "banking";
        }
        
        return "accountCreationFailed";
    }
    
    /// Shows the form to create an new account.
    public String ShowAccountCreationPage(){
        return "showAccountCreation";
    }
    
    /// Returns every account of the currently logged in customer.
    public List<Account> GetAllAccounts(){
        Customer current = this.customerModel.getCurrentSession();
        return this.accountService.GetAllAccounts(current);
    }
    
    public double GetOverallBalance(){
        List<Account> accounts = this.accountService.GetAllAccounts(this.customerModel.getCurrentSession());
        
        double balance = 0;
        balance = accounts.stream().map((acc) -> acc.getBalance()).reduce(balance, (accumulator, _item) -> accumulator + _item);
        
        return balance;
    }
    
    /// Determines if a value is positive or not.
    public String IsFloatPositive(double balance){
        if(balance >= 0){
            return "success";
        }
        
        return "danger";
    }
    
    /// Checks if the current user has any accounts registered.
    public boolean HasAccounts(){
        List<Account> check = this.accountService.GetAllAccounts(this.customerModel.getCurrentSession());
        
        return check != null && !check.isEmpty();
    }

    /// Getter / Setter
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getDisposition() {
        return disposition;
    }

    public void setDisposition(double disposition) {
        this.disposition = disposition;
    }
}
