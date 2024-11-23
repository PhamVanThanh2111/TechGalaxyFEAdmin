//package iuh.fit.se.techgalaxy.frontend.admin.services.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import iuh.fit.se.techgalaxy.frontend.admin.dto.request.TrademarkRequest;
//import iuh.fit.se.techgalaxy.frontend.admin.dto.response.DataResponse;
//import iuh.fit.se.techgalaxy.frontend.admin.dto.response.TrademarkResponse;
//import iuh.fit.se.techgalaxy.frontend.admin.services.TrademarkService;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Collections;
//import java.util.List;
//
//@Service
//public class TrademarkImpl implements TrademarkService {
//
//    private RestClient restClient;
//    private ObjectMapper objectMapper;
//    private static final String ENDPOINT = "http://localhost:8081";
//
//    public TrademarkImpl(RestClient restClient, ObjectMapper objectMapper) {
//        this.restClient = restClient;
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public DataResponse<TrademarkResponse> getTrademarkById(String id) {
//        return restClient.get()
//                .uri(ENDPOINT + "/trademarks/" + id)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//    }
//
//    @Override
//    public DataResponse<TrademarkResponse> getTrademarkByName(String name) {
//        return restClient.get()
//                .uri(ENDPOINT + "/trademarks/" + name)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//    }
//
//    @Override
//    public DataResponse<TrademarkResponse> getAllTrademarks() {
//        return restClient.get()
//                .uri(ENDPOINT + "/trademarks")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//    }
//
//    @Override
//    public DataResponse<TrademarkResponse> findAll() {
//        return restClient.get()
//                .uri(ENDPOINT + "/trademarks")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//    }
//
//    @Override
//    public DataResponse<TrademarkResponse> findById(String id) {
//        return restClient.get()
//                .uri(ENDPOINT + "/trademarks/id/" + id)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//    }
//
//    @Override
//    public DataResponse<TrademarkResponse> save(String name) {
//        return restClient.post()
//                .uri(ENDPOINT + "/trademarks?name=" + name)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//    }
//
//    @Override
//    public DataResponse<Object> delete(String id) {
//        return restClient.delete()
//                .uri(ENDPOINT + "/trademarks/" + id)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//    }
//
//    @Override
//    public DataResponse<TrademarkResponse> update(TrademarkRequest trademarkRequest) {
//        return restClient.put()
//                .uri(ENDPOINT + "/trademarks")
//                .accept(MediaType.APPLICATION_JSON)
//                .body(trademarkRequest)
//                .retrieve()
//                .body(new ParameterizedTypeReference<>() {
//                });
//    }
//}