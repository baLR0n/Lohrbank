package de.othr.sw.lohrbank.entity;

import de.othr.sw.lohrbank.entity.util.StringIdEntity;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Michael
 */
@Entity
@Table(name="Customers")
public class Customer extends StringIdEntity {

    // Member
    private String name;
    private String firstName;
    private String password;
    
    @OneToMany(mappedBy="owner", fetch=FetchType.EAGER)
    private Set<Account> accounts;
    
    public Customer(){
        super();
    }
    
    public Customer(String id, String name, String firstName, String password){
        super(id);
        this.setName(name);
        this.setFirstName(firstName);
        this.password = password;
    }
    
    // Getter, Setter
    public String getCustomerId() {
        return super.getId();
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
}
