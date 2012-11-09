package au.com.permeance.clusterMonitor.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.ConstructorSignature;

@Aspect("pertypewithin(au.com.permeance.clusterMonitor..* && !au.com.permeance.clusterMonitor.logging.* )")
public class ConstructorLoggingAspect extends AbstractLoggingAspect {

    @Before("execution(*.new(..))")
    public void onEnter(final JoinPoint joinPoint) {

        if (logger.isTraceEnabled()) {

            final ConstructorSignature signature = (ConstructorSignature) joinPoint.getSignature();

            final String msg = this.buildPrefix(signature, joinPoint)
                                   .append(" - start")
                                   .toString();

            logger.trace(msg);
        }
    }

    @AfterReturning("execution(*.new(..))")
    public void onReturn(final JoinPoint joinPoint) {

        if (logger.isTraceEnabled()) {

            final ConstructorSignature signature = (ConstructorSignature) joinPoint.getSignature();

            final String msg = this.buildPrefix(signature, joinPoint)
                                   .append(" - end")
                                   .toString();

            logger.trace(msg);
        }
    }

    @AfterThrowing(value = "execution(*.new(..))", throwing = "error")
    public void onThrow(final JoinPoint joinPoint,
                        final Throwable error) {

        if (logger.isErrorEnabled()) {

            final ConstructorSignature signature = (ConstructorSignature) joinPoint.getSignature();

            final String name = error.getClass().getSimpleName();

            final String msg = this.buildPrefix(signature, joinPoint)
                                   .append(" - end - throwing: ")
                                   .append(name)
                                   .toString();

            logger.error(msg, error);
        }
    }

    protected StringBuilder buildPrefix(final ConstructorSignature signature,
                                        final JoinPoint joinPoint) {

        final String simpleName = signature.getConstructor()
                                           .getDeclaringClass()
                                           .getSimpleName();

        final String[] parameterNames = signature.getParameterNames();

        final Object[] args = joinPoint.getArgs();

        return this.buildPrefix(simpleName,
                                parameterNames,
                                args);
    }

}
