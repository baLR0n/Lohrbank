package de.othr.sw.lohrbank.entity;

import de.othr.sw.lohrbank.entity.util.StringIdEntity;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Michael
 */
@Entity
@Table(name="Customers")
@XmlAccessorType(XmlAccessType.NONE)
public class Customer extends StringIdEntity implements Serializable {

    // Member
    @XmlElement(name="name", required=true)
    private String name;
    @XmlElement(name="firstName", required=true)
    private String firstName;
    @XmlElement(name="password", required=true)
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
        this.setPassword(password);
    }
    
    // Getter, Setter
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
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
