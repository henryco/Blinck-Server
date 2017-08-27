package net.henryco.blinckserver.util.test;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * @author Henry on 27/08/17.
 */
public interface BlinckTestUtil {


	static Method getMethod(Class owner, String name) {

		if (name == null || name.isEmpty()) throw new RuntimeException();


		Function<BiFunction<Method, BlinckTestName, Method>, Method> function = methodFunction -> {
			for (Method method : owner.getDeclaredMethods()) {
				Method r = methodFunction.apply(method, method.getAnnotation(BlinckTestName.class));
				if (r != null) {
					r.setAccessible(true);
					return r;
				}
			}
			return null;
		};

		Method result = function.apply((method, annotation) -> annotation == null || !name.equals(annotation.value())
				? null
				: method
		);

		return result != null
				? result
				: function.apply((method, annotation) -> annotation == null || !name.equals(method.getName())
					? null
					: method
		);

	}

}