package de.othr.sw.lohrbank.entity.util;

import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class RandomIdEntity extends SingleIdEntity<Long> {

    @Id
    protected Long id;
    
    protected RandomIdEntity() {
        this.id = ThreadLocalRandom.current().nextLong(100000, 999999);
    }
            
    @Override
    public Long getId() {
        return this.id;
    }
    
}
