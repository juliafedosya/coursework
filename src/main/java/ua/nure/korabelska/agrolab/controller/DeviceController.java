package ua.nure.korabelska.agrolab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("api/v1/devices")
public class DeviceController {
    private static  final String DEVICES_ENDPOINT = "http://localhost:8090/admin/devices/";
    RestTemplate restTemplate;
    HttpComponentsClientHttpRequestFactory requestFactory;


    @GetMapping("/{id}")
    public ResponseEntity<?> setDeviceStatus(@RequestParam(required = true) Boolean active, @PathVariable Long id) {
        restTemplate = new RestTemplate();
        requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(DEVICES_ENDPOINT + id)
                .queryParam("active", active);
        ResponseEntity<?> response  = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PATCH,
                null,
                String.class);
        return response;
    }



}
