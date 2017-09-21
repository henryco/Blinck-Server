package net.henryco.blinckserver.util.test;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, TYPE})
public @interface BlinckTestName {
	String value() default "";
}