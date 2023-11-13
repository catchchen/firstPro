package run.app.model.entity.support;

import java.io.Serializable;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import run.app.utils.ReflectionUtils;

/**
 * @author ryanwang
 * @date 2020-03-16
 */
public class CustomIdGenerator extends IdentityGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Object id = ReflectionUtils.getFieldValue("id", object);
        if (id != null) {
            return (Serializable) id;
        }
        return super.generate(session, object);
    }
}
