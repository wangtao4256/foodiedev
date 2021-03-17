package com.imooc.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyCorsFilter extends CorsFilter {

    public MyCorsFilter(CorsConfigurationSource configSource) {
        super(configSource);
    }
}