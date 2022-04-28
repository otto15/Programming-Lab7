package com.otto15.server.logging;


import com.otto15.common.network.Request;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(void com.otto15.server.ConnectionHandler.listen(..))")
    public void beforeListenAdvice() {
        LOGGER.info("Server launched");
    }

    @AfterThrowing(pointcut = "execution(* com.otto15.server.*.*(..))",
            throwing = "exception")
    public void afterThrowingAnyExceptionAdvice(Throwable exception) {
        LOGGER.error("Exception thrown: \n{}", exception.getMessage());
    }

    @AfterReturning(pointcut = "execution(* com.otto15.server.ConnectionHandler.run(..))")
    public void afterReturningRunAdvice() {
        LOGGER.info("Server closed");
    }

    @AfterReturning(pointcut = "execution(* com.otto15.server.ConnectionHandler.kill(..))",
            returning = "address")
    public void afterReturningKillAdvice(SocketAddress address) {
        LOGGER.info("Connection closed {}", address);
    }

    @AfterReturning(pointcut = "execution(* com.otto15.server.ConnectionHandler.accept(..))",
            returning = "address")
    public void afterReturningAcceptAdvice(SocketAddress address) {
        LOGGER.info("New connection accepted {}", address);
    }

    @AfterReturning(pointcut = "execution(* com.otto15.server.ConnectionHandler.read(..))",
            returning = "request")
    public void afterReturningReadAdvice(Request request) {
        if (request != null) {
            LOGGER.info("Got {}", request);
        }
    }

    @AfterReturning(pointcut = "execution(* com.otto15.server.ConnectionHandler.write(..))",
            returning = "responseLen")
    public void afterReturningWriteAdvice(int responseLen) {
        LOGGER.info("Sent response with len {}", responseLen);
    }
}
