package net.henryco.blinckserver.component;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Henry on 30/08/17.
 */
@Configuration @Import({
		DetailsServiceBeans.class
}) public abstract class BlinckBeansConfiguration {

}