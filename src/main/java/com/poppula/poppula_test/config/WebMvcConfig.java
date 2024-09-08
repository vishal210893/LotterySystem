package com.poppula.poppula_test.config;

import com.poppula.poppula_test.util.PoppuloUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.HashMap;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowedOrigins("*")
                        .maxAge(Long.MAX_VALUE)
                        .exposedHeaders("X-total-count", "X-size", "X-totalElements", "X-totalPages", "X-number");
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLogInterceptor());
    }

    public class RequestLogInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            try {
                if (request.getMethod().equals(HttpMethod.GET.name()) || request.getMethod().equals(HttpMethod.DELETE.name())) {
                    log.info("[Request]\n " +
                                    "URI - {}\n " +
                                    "QUERY PARAMETERS - {}\n " +
                                    "METHOD - {}\n " +
                                    "HEADERS - {}",
                            request.getRequestURI(),
                            getParameterMap(request),
                            request.getMethod(),
                            PoppuloUtil.printHeader(request, null));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return true;
        }

        private HashMap<String, String> getParameterMap(HttpServletRequest request) {
            HashMap<String, String> queryParams = new HashMap<>();
            request.getParameterMap().forEach((k, v) -> queryParams.put(k, Arrays.toString(v)));
            return queryParams;
        }
    }

}