
package com.govtech.platform.web.autoconfigure;

import com.govtech.platform.web.correlation.CorrelationIdFilter;
import com.govtech.platform.web.correlation.CorrelationIdInterceptor;
import com.govtech.platform.web.error.GlobalExceptionHandler;

import io.micrometer.tracing.Tracer;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(GlobalExceptionHandler.class)
public class PlatformWebAutoConfiguration {

    @Bean
    public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {

        FilterRegistrationBean<CorrelationIdFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new CorrelationIdFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);

        return registration;
    }

    @Bean
    public CorrelationIdInterceptor correlationIdInterceptor(Tracer tracer) {
        return new CorrelationIdInterceptor(tracer);
    }
}
