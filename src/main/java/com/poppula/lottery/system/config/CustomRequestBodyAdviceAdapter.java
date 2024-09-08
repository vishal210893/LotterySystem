package com.poppula.lottery.system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poppula.lottery.system.util.PoppuloUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    private final HttpServletRequest request;

    private final ObjectMapper mapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            log.info("[Request]\n " +
                            "URI - {}\n " +
                            "QUERY PARAMETERS - {}\n " +
                            "METHOD - {}\n " +
                            "REQUEST BODY - {}\n " +
                            "HEADERS - {}",
                    request.getRequestURI(),
                    getParameterMap(request),
                    request.getMethod(),
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body),
                    PoppuloUtil.printHeader(request, null));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    private HashMap<String, String> getParameterMap(HttpServletRequest request) {
        HashMap<String, String> queryParams = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> queryParams.put(k, Arrays.toString(v)));
        return queryParams;
    }

}