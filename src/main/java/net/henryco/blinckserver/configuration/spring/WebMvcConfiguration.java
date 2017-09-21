package net.henryco.blinckserver.configuration.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

import static java.io.File.separator;

/**
 * @author Henry on 23/08/17.
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	public static final String REL_FILE_PATH = System.getProperty("user.dir");
	public static final String ABS_FILE_PATH = System.getProperty("user.home");

	public static final String DATA_PATH_POSTFIX = separator + "res" + separator + "public" + separator;
	public static final String DATA_PATH_URL = "/rel" + DATA_PATH_POSTFIX;

	public static final String USER_IMAGE_POSTFIX = DATA_PATH_POSTFIX + "images" + separator;
	public static final String USER_IMAGE_URL = "/rel" + USER_IMAGE_POSTFIX;


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {


		registry.addResourceHandler("/rel/**")
				.addResourceLocations("file:" + REL_FILE_PATH + File.separator);

		registry.addResourceHandler("/abs/**")
				.addResourceLocations("file:" + ABS_FILE_PATH + File.separator);

		System.out.println(
				"\n***\n\n" +
						"WORK DIR: "+REL_FILE_PATH+" \t[mapping: /rel/**" + "]\n" +
						"HOME DIR: "+ABS_FILE_PATH+" \t[mapping: /abs/**" + "]\n"+
				"\n***\n"
		);

	}

}