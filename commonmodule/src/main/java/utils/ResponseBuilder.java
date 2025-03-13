package utils;

import models.Datasets;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResponseBuilder {

    public static Map<String, Object> buildResponse(String resmsgId,String statusMsg,int code,String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", "Api.read");
        response.put("ver", "3.0.5");
        response.put("time", LocalDateTime.now());

        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("resmsgId", resmsgId);
        params.put("status", statusMsg);

        response.put("params", params);
        response.put("status", code);
        response.put("message", message);

        return response;
    }

    public static Map<String, Object> buildResponseOnget(String statusMsg,int code,String message,List datasets) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", "Api.read");
        response.put("ver", "3.0.5");
        response.put("time", LocalDateTime.now());

        HashMap<String, Object> params = new LinkedHashMap<>();
//        params.put("resmsgId", resmsgId);
        params.put("status", statusMsg);

        response.put("params", params);
        response.put("status", code);
        response.put("message", message);
        response.put("result",datasets);

        return response;
    }

    public static Map<String, Object> buildResponseOngetById(String statusMsg,int code,String message,Datasets datasets) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", "Api.read");
        response.put("ver", "3.0.5");
        response.put("time", LocalDateTime.now());

        HashMap<String, Object> params = new LinkedHashMap<>();
//    params.put("resmsgId", resmsgId);
        params.put("status", statusMsg);

        response.put("params", params);
        response.put("status", code);
        response.put("message", message);
        response.put("result",datasets);

        return response;
    }
}

