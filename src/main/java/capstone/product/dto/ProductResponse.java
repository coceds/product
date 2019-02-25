package capstone.product.dto;

import org.springframework.http.HttpStatus;

public class ProductResponse {

    private Product product;
    private HttpStatus httpStatus;

    public ProductResponse(Product product) {
        this.product = product;
    }

    public ProductResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Product getProduct() {
        return product;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
