package com.poppulo.lottery.system.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.server.ServerHttpResponse;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

@UtilityClass
public class PoppuloUtil {

    public static String printHeader(HttpServletRequest request, ServerHttpResponse response) throws JsonProcessingException {
        HashMap<String, String> hm = new HashMap<>();
        if (request != null && response == null) {
            final Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                hm.put(headerName, request.getHeader(headerName));
            }
        } else {
            final Collection<String> headerNames = response.getHeaders().keySet();
            headerNames.forEach(v -> hm.put(v, response.getHeaders().get(v).toString()));
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(hm);
    }

    public int calculateResult(List<Integer> numbers) {
        int sum = numbers.stream().mapToInt(Integer::intValue).sum();
        if (sum == 2) {
            return 10;
        } else if (numbers.stream().distinct().count() == 1) {
            return 5;
        } else if (!numbers.get(1).equals(numbers.get(0)) && !numbers.get(2).equals(numbers.get(0))) {
            return 1;
        } else {
            return 0;
        }
    }

}