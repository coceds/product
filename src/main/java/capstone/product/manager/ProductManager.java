package capstone.product.manager;

import capstone.product.dto.*;
import capstone.product.integration.CatalogClient;
import capstone.product.integration.InventoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductManager {

    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;

    @Autowired
    public ProductManager(CatalogClient catalogClient, InventoryClient inventoryClient) {
        this.catalogClient = catalogClient;
        this.inventoryClient = inventoryClient;
    }

    public ProductResponse getProductById(String productId) {
        ProductResponse productResponse = catalogClient.getProductById(productId);
        if (productResponse.getProduct() == null) {
            return productResponse;
        }
        Product product = productResponse.getProduct();
        ProductAvailabilityResponse availabilities = inventoryClient.checkAvailability(Collections.singletonList(product.getProductId()));
        if (availabilities.getProductAvailabilities() == null) {
            return new ProductResponse(availabilities.getHttpStatus());
        }
        if (availabilities.getProductAvailabilities().size() > 0 && availabilities.getProductAvailabilities().get(0).getAvailable() > 0) {
            return productResponse;
        }
        return new ProductResponse(HttpStatus.NOT_FOUND);

    }

    public ProductListResponse getBySKU(String sku) {
        ProductListResponse productListResponse = catalogClient.getProductBySKU(sku);
        if (productListResponse.getProducts() == null) {
            return productListResponse;
        } else if (productListResponse.getProducts().isEmpty()) {
            return new ProductListResponse(Collections.emptyList());
        }
        List<Product> products = productListResponse.getProducts();
        final ProductAvailabilityResponse availabilities = inventoryClient.checkAvailability(
                products.stream().map(Product::getProductId).collect(Collectors.toList())
        );
        if (availabilities.getProductAvailabilities() == null) {
            return new ProductListResponse(availabilities.getHttpStatus());
        }
        final Set<String> available = availabilities.getProductAvailabilities().stream()
                .filter(p -> p.getAvailable() > 0)
                .map(ProductAvailability::getProductId)
                .collect(Collectors.toSet());

        return new ProductListResponse(
                products.stream()
                        .filter(p -> available.contains(p.getProductId()))
                        .collect(Collectors.toList())
        );
    }
}
