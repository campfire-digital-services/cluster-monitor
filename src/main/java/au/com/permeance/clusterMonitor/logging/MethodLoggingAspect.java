package au.com.permeance.clusterMonitor.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect("pertypewithin(au.com.permeance.clusterMonitor..* && !au.com.permeance.clusterMonitor.logging.*)")
public class MethodLoggingAspect extends AbstractLoggingAspect {

    @Before("execution(* *(..))")
    public void onEnter(final JoinPoint joinPoint) {

        if (logger.isTraceEnabled()) {

            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            final String msg = this.buildPrefix(signature, joinPoint)
                                   .append(" - start")
                                   .toString();

            logger.trace(msg);
        }
    }

    @AfterReturning(value = "execution(* *(..))", returning = "result")
    public void onReturn(final JoinPoint joinPoint,
                         final Object result) {

        if (logger.isTraceEnabled()) {

            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            final StringBuilder builder = this.buildPrefix(signature, joinPoint)
                                              .append(" - end");

            if (signature.getReturnType() != Void.TYPE) {
                builder.append(" - returning: ")
                       .append(result);
            }

            final String msg = builder.toString();

            logger.trace(msg);
        }
    }

    @AfterThrowing(value = "execution(* *(..))", throwing = "error")
    public void onThrow(final JoinPoint joinPoint,
                        final Throwable error) {

        if (logger.isErrorEnabled()) {

            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            final String name = error.getClass()
                                     .getSimpleName();

            final String msg = this.buildPrefix(signature, joinPoint)
                                   .append(" - end - throwing: ")
                                   .append(name)
                                   .toString();

            logger.error(msg, error);
        }
    }

    protected StringBuilder buildPrefix(final MethodSignature signature,
                                        final JoinPoint joinPoint) {

        final String name = signature.getMethod()
                                     .getName();

        final String[] parameterNames = signature.getParameterNames();

        final Object[] args = joinPoint.getArgs();

        return buildPrefix(name,
                           parameterNames,
                           args);
    }

}
