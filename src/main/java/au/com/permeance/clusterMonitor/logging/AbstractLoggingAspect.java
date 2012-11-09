package au.com.permeance.clusterMonitor.logging;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.slf4j.helpers.NOPLogger.NOP_LOGGER;

@Aspect
public abstract class AbstractLoggingAspect {

    protected Logger logger = NOP_LOGGER;

    @After("staticinitialization(*)")
    public void afterStaticInitialization(final StaticPart staticPart) {

        final Class clazz = staticPart.getSourceLocation()
                                      .getWithinType();

        logger = LoggerFactory.getLogger(clazz);
    }

    protected StringBuilder buildPrefix(final String name,
                                        final String[] parameterNames,
                                        final Object[] parameterValues) {

        final StringBuilder builder = new StringBuilder().append(name)
                                                         .append('(');

        for (int i = 0; i < parameterNames.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i])
                   .append('=')
                   .append(parameterValues[i]);
        }

        return builder.append(')');
    }

}
