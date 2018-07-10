package com.skyforce.aop;

import com.skyforce.models.Category;
import com.skyforce.models.City;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.net.SocketTimeoutException;

/*
*
 * Date 07.07.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 *
*/



@Aspect
@Component
@Slf4j
public class LoggingServiceLayer implements Ordered {

    private double total;
    private double passed;

        @Pointcut(value = "within(com.skyforce.services.implementations.ParseServiceImpl)")
        public void serviceLayer(){}

        @Before(value = "serviceLayer() && args(city, category)", argNames = "joinPoint,city,category")
        public void parsingServiceMethod(JoinPoint joinPoint, City city, Category category){
            log.info("Method: "+joinPoint.getSignature().getName());
            log.info("City is: "+ city.getName());
            log.info("Category is: "+ category.getTitle());
        }

        @AfterThrowing(pointcut = "serviceLayer()", throwing = "socketException")
        public void logAfterThrowingSocketTimeout(JoinPoint joinPoint, SocketTimeoutException socketException) throws Throwable{
            log.info(joinPoint.getSignature().getName() + " got timeout exception: "+ socketException.getMessage());
            log.info("--------------------------------------");
        }



        @Around(value = "execution(* com.skyforce.services.implementations.ParseServiceImpl.parseByCategoryAndCity(..))")
        public void logAround(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable{
            log.info("--------------------------------------");
            log.info("method: "+ proceedingJoinPoint.getSignature().getName());
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            proceedingJoinPoint.proceed();
            stopWatch.stop();
            long totalTimeMillis = stopWatch.getTotalTimeMillis();
            log.info("execution time: " + totalTimeMillis + "ms");
            total +=(double) totalTimeMillis;
            passed++;
            log.info("--------------------------------------");
            log.info("Average of execution is: "+ (total / passed));
            log.info("--------------------------------------");
        }

        @AfterThrowing(pointcut = "execution(* com.skyforce.services.implementations.ParseServiceImpl.*(..))", throwing = "exception")
        public void logAfterThrowingException(JoinPoint joinPoint, Exception exception) throws Throwable{
            log.info(joinPoint.getSignature().getName() + " got exception: "+ exception.getMessage());
            log.info("--------------------------------------");
        }

    @Override
    public int getOrder() {
        return 0;
    }
}
