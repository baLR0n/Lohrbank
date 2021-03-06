/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.model;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.service.AccountService;

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
    private AccountService accountService;
    
    @Inject
    private CustomerModel customerModel;
            
    private String accountName;
    private double disposition;
    private Account lastCreated;

    /**
     * Creates an new account from the values entered in the register form.
     * @return 
     */
    public String CreateAccount(){
        this.lastCreated = this.accountService.CreateAccount(this.customerModel.getCurrentSession(), this.accountName, 0, this.disposition);
        
        if(this.lastCreated != null){
            return "banking";
        }
        
        return "accountCreationFailed";
    }
    
    /**
     * Shows the form to create an new account.
     * @return 
     */
    public String ShowAccountCreationPage(){
        this.ResetForm();
        return "showAccountCreation";
    }
    
    /**
     * Resets the form for the next cycle.
     */
    private void ResetForm(){
        this.accountName = "";
        this.disposition = 0;
    }
    
    /**
     * Returns every account of the currently logged in customer.
     * @return 
     */
    public List<Account> GetAllAccounts(){
        Customer current = this.customerModel.getCurrentSession();
        return this.accountService.GetAllAccounts(current);
    }
    
    /**
     * Gets the balance of all accounts of the logged in customer combined.
     * @return 
     */
    public double GetOverallBalance(){
        List<Account> accounts = this.accountService.GetAllAccounts(this.customerModel.getCurrentSession());
        
        double balance = 0;
        balance = accounts.stream().map((acc) -> acc.getBalance()).reduce(balance, (accumulator, _item) -> accumulator + _item);
        
        return balance;
    }
    
    /**
     * Determines if a value is positive or not.
     * @param balance
     * @return 
     */
    public String IsFloatPositive(double balance){
        if(balance >= 0){
            return "success";
        }
        
        return "danger";
    }
    
    /**
     * Checks if the current user has any accounts registered.
     * @return 
     */
    public boolean HasAccounts(){
        if(!this.customerModel.HasLoggedInCustomer())
        {
            return false;
        }
        
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
