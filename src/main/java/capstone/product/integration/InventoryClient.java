package capstone.product.integration;

import capstone.product.dto.ProductAvailability;
import capstone.product.dto.ProductAvailabilityResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class InventoryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestTemplate restTemplate;

    @Autowired
    public InventoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "defaultBehavior", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "20000")
    })
    public ProductAvailabilityResponse checkAvailability(List<String> productIds) {
        try {
            HttpEntity<List<String>> request = new HttpEntity<>(productIds);
            ResponseEntity<List<ProductAvailability>> responseEntity =
                    restTemplate.exchange("http://inventory-service/inventory/availability",
                            HttpMethod.POST,
                            request,
                            new ParameterizedTypeReference<List<ProductAvailability>>() {
                            });
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return new ProductAvailabilityResponse(responseEntity.getBody());
            }
            return new ProductAvailabilityResponse(responseEntity.getStatusCode());
        } catch (Exception e) {
            LOGGER.error("Failed to call inventory service", e);
            return new ProductAvailabilityResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    ProductAvailabilityResponse defaultBehavior(List<String> productIds) {
        return new ProductAvailabilityResponse(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
