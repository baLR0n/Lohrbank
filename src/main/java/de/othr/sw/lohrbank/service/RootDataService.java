package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.Customer;
import de.othr.sw.lohrbank.entity.Transaction;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@RequestScoped
public class RootDataService {
   
    @Inject
    private AccountService accountService;
    
    @Inject
    private TransactionService transactionService;
    
    private static final List<Customer> rootCustomers = new ArrayList<>();
    private static final List<Account> rootAccounts = new ArrayList<>();
    
    static {
        // Add root customers.
        rootCustomers.add(new Customer("othadmin@st.oth-regensburg.de", "OTH", "Admin", "othadmin"));
        rootCustomers.add(new Customer("johannes1.malsam@st.oth-regensburg.de", "Malsam", "Johannes", "idcheck"));
        rootCustomers.add(new Customer("simon.schreiner@st.oth-regensburg.de", "Schreiner", "Simon", "shop"));
        rootCustomers.add(new Customer("matthias.doerfler@st.oth-regensburg.de", "Dörfler", "Matthias", "compare"));
        
        Account account = new Account(new Long(123456789));
        account.setOwner(rootCustomers.get(0));
        account.setBalance(1000);
        account.setDisposition(500);
        rootAccounts.add(account);
    }
    
    @PersistenceContext(unitName="Lohrbank")
    private EntityManager entityManager;
    
    @Transactional(TxType.REQUIRES_NEW)
    public void GenerateRootData() {
        Customer check = entityManager.find(Customer.class, "othadmin@st.oth-regensburg.de");
        if(check != null)
            return;
        
        // Persist customers.
        rootCustomers.forEach( c -> entityManager.persist(c) );
        
        //rootAccounts.forEach(a -> entityManager.persist(a));
        
        // Give them an account each.
        // Admin gets 1000€ starting money.
        
//        this.accountService.CreateAccount(rootCustomers.get(0), new Long(123456789), 1000, 500);
//        this.accountService.CreateAccount(rootCustomers.get(1), new Long(111111111), 0, 500);
//        this.accountService.CreateAccount(rootCustomers.get(2), new Long(222222222), 0, 500);
//        this.accountService.CreateAccount(rootCustomers.get(3), new Long(333333333), 0, 500);
        
        // Lets share the wealth!
        //this.transactionService.CreateTransaction(new Transaction("othadmin@st.oth-regensburg.de", new Long(123456789), new Long(111111111), 250));
    }
}
