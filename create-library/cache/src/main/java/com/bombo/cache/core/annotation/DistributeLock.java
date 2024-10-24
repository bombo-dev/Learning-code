package com.bombo.cache.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    /**
     * 락의 이름
     */
    String key();

    /**
     * 락의 시간 단위
     * <br>
     * DEFAULT SECONDS
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락의 대기 시간
     * <br>
     * 락 획득을 위해 waitTime 만큼 대기한다.
     * <br>
     * DEFAULT 5
     */
    long waitTime() default 5L;

    /**
     * 락 임대 시간
     * <br>
     * 락을 획득한 이후 leaseTime이 지나면 락을 해제한다.
     * <br>
     * DEFAULT 3
     */
    long leaseTime() default 3L;
}
