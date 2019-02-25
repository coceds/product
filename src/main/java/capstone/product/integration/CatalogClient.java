package capstone.product.integration;

import capstone.product.dto.Product;
import capstone.product.dto.ProductListResponse;
import capstone.product.dto.ProductResponse;
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
public class CatalogClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestTemplate restTemplate;

    @Autowired
    public CatalogClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "defaultBehaviorProduct", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "20000")
    })
    public ProductResponse getProductById(String productId) {
        try {
            ResponseEntity<Product> responseEntity =
                    restTemplate.getForEntity("http://catalog-service/catalog/product/{productId}",
                            Product.class,
                            productId);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return new ProductResponse(responseEntity.getBody());
            } else {
                return new ProductResponse(responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to call catalog service product by id", e);
            return new ProductResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    ProductResponse defaultBehaviorProduct(String productId) {
        return new ProductResponse(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @HystrixCommand(fallbackMethod = "defaultBehaviorSKU", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "20000")
    })
    public ProductListResponse getProductBySKU(String sku) {
        try {
            ResponseEntity<List<Product>> responseEntity =
                    restTemplate.exchange("http://catalog-service/catalog/product/sku/" + sku,
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<List<Product>>() {
                            });
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return new ProductListResponse(responseEntity.getBody());
            } else {
                return new ProductListResponse(responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to call catalog service product by sku", e);
            return new ProductListResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    ProductListResponse defaultBehaviorSKU(String productId) {
        return new ProductListResponse(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
