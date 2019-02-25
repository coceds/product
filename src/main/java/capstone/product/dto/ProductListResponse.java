package capstone.product.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ProductListResponse {

    private List<Product> products;
    private HttpStatus httpStatus;

    public ProductListResponse(List<Product> products) {
        this.products = products;
    }

    public ProductListResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<Product> getProducts() {
        return products;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
