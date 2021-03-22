package com.imooc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceLogAspect {
    /**
     * AOP通知：
     * 1.前置通知：在方法调用前执行
     * 2.后置通知：在方法正常调用之后执行
     * 3.环绕通知：在方法调用之前和之后，都分别可以执行的通知
     * 4.异常通知：如果在方法调用过程中发生异常，则通知
     * 5.最终通知：在方法调用之后执行
     */
    private final static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * 第一处 *代表方法返回类型 *代表所有类型
     * 第二处 包名代表aop监控的类所在的包
     * 第三处 .. 代表该包以及其子包下所有类方法
     * 第四 * 代表类名
     * 第五 *(..) *代表类中方法名 (..)代表方法中任何参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("========开始执行 {}.{}=========",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long takeTime = endTime - beginTime;

        if (takeTime > 3000) {
            logger.error("=====执行结束，耗时：{}毫秒===", takeTime);
        }else{
            logger.info("=====执行结束，耗时：{}毫秒===", takeTime);
        }
        return result;
    }
}
