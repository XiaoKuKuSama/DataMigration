package com.ydx.datamigration.aop;


import com.ydx.datamigration.enums.DateEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 记录service方法执行结果、时常
 */

@Aspect
@Component
public class PrintServiceMethodTime {

    private static final Logger logger = LoggerFactory.getLogger("mt");


    private static final String POINT = "execution(* com.ydx.datamigration.service..*(..))";

    @Around(POINT)
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        long startTime = getNow();
        long endTime;
        Object obj = null;
        try {
            obj = joinPoint.proceed(joinPoint.getArgs());
            endTime = getNow();
            logger.info("{} info {}.{} - - end success {} -",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateEnum.YMDHMS.getValue())),
                    className, methodName, (endTime - startTime) + "ms");

            return obj;
        } catch (Throwable e) {
            endTime = getNow();
            logger.error("{} error {}.{} - - end error {} -",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateEnum.YMDHMS.getValue())),
                    className, methodName, (endTime - startTime) + "ms");
            throw e;
        }
    }

    private long getNow() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

}
