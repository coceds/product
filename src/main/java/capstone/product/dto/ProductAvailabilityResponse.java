package capstone.product.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ProductAvailabilityResponse {

    private List<ProductAvailability> productAvailabilities;
    private HttpStatus httpStatus;

    public ProductAvailabilityResponse(List<ProductAvailability> productAvailabilities) {
        this.productAvailabilities = productAvailabilities;
    }

    public ProductAvailabilityResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<ProductAvailability> getProductAvailabilities() {
        return productAvailabilities;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
