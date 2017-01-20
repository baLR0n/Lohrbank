/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.AccountInfo;
import de.othr.sw.lohrbank.entity.Customer;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Michael
 */
public interface IAccountService {
    Account CreateAccount(Customer customer, String name);
    Account CreateAccount(Customer customer, String name, double balance, double disposition);
    Account CreateAccount(Customer customer, Long id, String name, double balance, double disposition);
    
    List<Account> GetAllAccounts(Customer customer);
    
    double ChangeAccountBalance(Long accountId, double value);
    double ChangeAccountDisposition(Long accountId, double disposition);
    AccountInfo GetAccountInfo(Long accountId, Date from, Date to);
}
