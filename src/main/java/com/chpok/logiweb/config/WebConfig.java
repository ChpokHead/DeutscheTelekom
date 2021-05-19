package com.chpok.logiweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.chpok.logiweb"})
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void init() {
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }

    @Bean
    public HandlerExceptionResolver customExceptionResolver () {
        SimpleMappingExceptionResolver s = new SimpleMappingExceptionResolver();
        Properties p = new Properties();

        p.setProperty(NoHandlerFoundException.class.getName(), "notFoundPage");
        p.setProperty(HttpRequestMethodNotSupportedException.class.getName(), "notFoundPage");

        s.setExceptionMappings(p);
        s.addStatusCode("notFoundPage", HttpStatus.NOT_FOUND.value());

        s.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return s;
    }

    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver
                = new SpringResourceTemplateResolver();

        templateResolver.setPrefix("/static/html/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);

        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();

        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");

        return resolver;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/css/**").addResourceLocations("/static/css/");

        registry.addResourceHandler("/static/images/**").addResourceLocations("/static/images/");
    }
}
