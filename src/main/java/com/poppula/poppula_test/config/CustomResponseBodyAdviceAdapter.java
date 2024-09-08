package com.poppula.poppula_test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poppula.poppula_test.util.PoppuloUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    private final ObjectMapper mapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object responseMessage, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            if (request instanceof ServletServerHttpRequest && response instanceof ServletServerHttpResponse) {
                final HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
                final HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
                log.info("[Response]\n " +
                                "URI - {}\n " +
                                "METHOD - {}\n " +
                                "STATUS CODE - {}\n " +
                                "RESPONSE MESSAGE - {}\n" +
                                "HEADERS - {}",
                        servletRequest.getRequestURI(),
                        servletRequest.getMethod(),
                        servletResponse.getStatus(),
                        servletRequest.getMethod().equals(HttpMethod.GET.name()) ?
                                HttpStatus.resolve(servletResponse.getStatus()).name() :
                                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMessage),
                        PoppuloUtil.printHeader(null, response));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }

}