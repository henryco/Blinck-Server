package net.henryco.blinckserver.util.test;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface BlinckTestName {
	String value() default "";
}