package net.henryco.blinckserver.util.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * @author Henry on 27/08/17.
 */
public interface BlinckTestUtil {


	static Class<?> getClass(Class owner, String name) {

		if (name == null || name.isEmpty()) throw new RuntimeException();
		Class[] classes = owner.getDeclaredClasses();

		for (Class c : classes) {
			Annotation annotation = c.getAnnotation(BlinckTestName.class);
			if (annotation != null && ((BlinckTestName) annotation).value().equals(name))
				return c;
		}

		for (Class c : classes)
			if (c.getAnnotation(BlinckTestName.class) != null)
				if (c.getSimpleName().equals(name)) return c;

		return owner;
	}


	/**
	 *	Get method, by annotated name.
	 *	Inner classes expression work for static methods only.
	 */
	static Method getMethod(Class classOwner, String methodName) {

		if (methodName == null || methodName.isEmpty()) throw new RuntimeException();

		String[] split = methodName.split("\\.");
		Class owner = classOwner;
		for (int i = 0; i < split.length - 1; i++) {
			owner = getClass(owner, split[i].trim());
		}

		final Class finalOwner = owner;
		final String name = split[split.length - 1];

		Function<BiFunction<Method, BlinckTestName, Method>, Method> function = methodFunction -> {
			for (Method method : finalOwner.getDeclaredMethods()) {
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