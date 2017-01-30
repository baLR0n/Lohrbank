/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.model;

import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.service.AccountService;
import de.othr.sw.lohrbank.service.CustomerService;
import de.othr.sw.lohrbank.service.RootDataService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Michael
 */
@Named
@SessionScoped
public class CustomerModel implements Serializable {
    
    @Inject
    private CustomerService customerService;
    
    @Inject
    private AccountService accountService;

    @Inject
    private RootDataService rootDataService;
    
    @PostConstruct
    public void rootData() {
        rootDataService.GenerateRootData();
    }
    
    private String userId, name, firstName, password, passwordAgain, citizenId, street, city, errorMessage;
    private Integer postcode;
    private Customer lastGenerated = null;
    private Customer currentSession = null;

    /// Creates a new customer from the values entered in the register form.
    public String CreateCustomer(){
        this.lastGenerated = customerService.CreateCustomer(new Customer(this.userId, this.name, this.firstName, this.password));
        this.ClearForm();
        
        if(this.lastGenerated == null){
            this.errorMessage = "Kundenkonto konnte nicht angelegt werden.";
            return "customerCreatedFailed";
        }
        
        this.currentSession = this.lastGenerated;
        return "customerCreated";
    }
    
    /// Checks user auth and proceeds with login.
    public void Login(){
        this.currentSession = this.customerService.CheckCustomerAuth(this.userId, this.password);
        
        // Start job to check for debits which change the balance of one of the users accounts.
        this.accountService.CheckForDebits(this.currentSession);
    }
    
    /// Logout
    public String Logout(){
        this.currentSession = null;
        return "welcome";
    }
    
    /// Checks citizen ID, form data and creates a new customer.
    // ToDo: namen und vornamen in die Form
    public String Register(){
        if(this.customerService.IdentityRequest(this.citizenId, this.name, this.firstName)){
            if(this.password.equals(this.passwordAgain)){
                return this.CreateCustomer();
            }else{
                this.password = "";
                this.passwordAgain = "";
                this.errorMessage = "Passwörter stimmen nicht überein!";
            }
        }else{
            this.citizenId = "";
            this.errorMessage = "Ungültige Meldeamt-ID!";
        }
        
        return "registerFailed";
    }
    
    /// Shows the register page.
    public String ShowRegisterPage(){
        return "showRegister";
    }
    
    public boolean HasLoggedInCustomer() {
        return this.currentSession != null;
    }
    
    
    private void ClearForm(){
        this.errorMessage = "";
        this.citizenId = "";
        this.firstName = "";
        this.name = "";
        this.password = "";
        this.passwordAgain = "";
    }
    
    /// Getter / Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }
    
    public Customer getLastGenerated() {
        return lastGenerated;
    }

    public void setLastGenerated(Customer lastGenerated) {
        this.lastGenerated = lastGenerated;
    }

    public Customer getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Customer currentSession) {
        this.currentSession = currentSession;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
