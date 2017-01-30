package de.othr.sw.lohrbank.entity.util;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public class StringIdEntity extends SingleIdEntity<String> {

    @Id
    @XmlElement(name="id")
    private String id;
    
    protected StringIdEntity() {
        this.id = "unset";
    }
    
    protected StringIdEntity(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
