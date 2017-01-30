package de.othr.sw.lohrbank.service;

import de.othr.sw.lohrbank.entity.Account;
import de.othr.sw.lohrbank.entity.Customer;

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
        rootCustomers.add(new Customer("othadmin@st.oth-regensburg.de", "OTH", "Admin", "othadmin")); // Kunden-ID: 0
        rootCustomers.add(new Customer("johannes1.malsam@st.oth-regensburg.de", "Malsam", "Johannes", "idcheck"));// Kunden-ID: 1
        rootCustomers.add(new Customer("simon.schreiner@st.oth-regensburg.de", "Schreiner", "Simon", "shop")); // Kunden-ID: 2
        rootCustomers.add(new Customer("herbert.kunde@web.de", "Kunde", "Herbert", "kunde")); // Kunden-ID: 3
        rootCustomers.add(new Customer("matthias.doerfler@st.oth-regensburg.de", "Dörfler", "Matthias", "compare")); // Kunden-ID: 4

        rootCustomers.add(new Customer("admin@amazon.de", "Amazon", "Admin", "amazon")); // Kunden-ID: 5
        rootCustomers.add(new Customer("admin@saturn.de", "Saturn", "Admin", "saturn")); // Kunden-ID: 6
        rootCustomers.add(new Customer("admin@mediamarkt.de", "MediaMarkt", "Admin", "mediamarkt")); // Kunden-ID: 7
        
        rootCustomers.add(new Customer("max.mustermann@web.de", "Mustermann", "Max", "mustermann")); // Kunden-ID: 8
        rootCustomers.add(new Customer("volker.racho@web.de", "Racho", "Volker", "racho")); // Kunden-ID: 9
        rootCustomers.add(new Customer("andi.gewehre@web.de", "Gewehre", "Andi", "gewehre")); // Kunden-ID: 10
        
        /// Add some accounts.
        Account account = new Account(new Long(123456789));
        account.setOwner(rootCustomers.get(0));
        account.setBalance(10000);
        account.setDisposition(5000);
        account.setName("Admin-Giro");
        rootAccounts.add(account);
        
        account = new Account(new Long(111111111));
        account.setOwner(rootCustomers.get(1));
        account.setBalance(1000);
        account.setDisposition(500);
        account.setName("Johannes-Giro");
        rootAccounts.add(account);
        
        account = new Account(new Long(222222222));
        account.setOwner(rootCustomers.get(2));
        account.setBalance(1000);
        account.setDisposition(500);
        account.setName("Simon-Giro");
        rootAccounts.add(account);

        account = new Account(new Long(333333333));
        account.setOwner(rootCustomers.get(4));
        account.setBalance(1000);
        account.setDisposition(500);
        account.setName("Matthias-Giro");
        rootAccounts.add(account);
        
        account = new Account(new Long(122222222));
        account.setOwner(rootCustomers.get(3));
        account.setBalance(10000);
        account.setDisposition(500);
        account.setName("Shopkunden-Giro");
        rootAccounts.add(account);       
        
        // Accounts for shops
        account = new Account(new Long(111111112));
        account.setOwner(rootCustomers.get(5)); // AMAZON
        account.setBalance(50000);
        account.setDisposition(10000);
        account.setName("Amazon-Giro");
        rootAccounts.add(account); 

        account = new Account(new Long(111111113));
        account.setOwner(rootCustomers.get(6)); // SATURN
        account.setBalance(50000);
        account.setDisposition(10000);
        account.setName("Saturn-Giro");
        rootAccounts.add(account); 

        account = new Account(new Long(111111114));
        account.setOwner(rootCustomers.get(7)); // MEDIAMARKT
        account.setBalance(50000);
        account.setDisposition(10000);
        account.setName("MediaMarkt-Giro");
        rootAccounts.add(account); 

        // Accounts for default customers
        account = new Account(new Long(222222223));
        account.setOwner(rootCustomers.get(8));
        account.setBalance(10000);
        account.setDisposition(500);
        account.setName("Mustermann-Giro");
        rootAccounts.add(account); 

        account = new Account(new Long(222222224));
        account.setOwner(rootCustomers.get(9));
        account.setBalance(10000);
        account.setDisposition(500);
        account.setName("Racho-Giro");
        rootAccounts.add(account); 

        account = new Account(new Long(222222225));
        account.setOwner(rootCustomers.get(10));
        account.setBalance(10000);
        account.setDisposition(500);
        account.setName("Gewehre-Giro");
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
        
        // Persist accounts.
        rootAccounts.forEach(a -> entityManager.persist(a));
    }
}
