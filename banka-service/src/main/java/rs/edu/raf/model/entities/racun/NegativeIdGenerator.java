package rs.edu.raf.model.entities.racun;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.io.Serializable;

public class NegativeIdGenerator extends SequenceStyleGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long id = (Long) super.generate(session, object);
        return -id; // Return the negative value of the generated ID
    }
}
