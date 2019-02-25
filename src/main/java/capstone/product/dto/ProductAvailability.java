package capstone.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductAvailability {

    private final String productId;
    private final int available;

    @JsonCreator
    public ProductAvailability(@JsonProperty("productId") String productId,
                               @JsonProperty("available") int available) {
        this.productId = productId;
        this.available = available;
    }

    public String getProductId() {
        return productId;
    }

    public int getAvailable() {
        return available;
    }
}
