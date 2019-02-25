package capstone.product.controller;

import capstone.product.dto.Product;
import capstone.product.dto.ProductListResponse;
import capstone.product.dto.ProductResponse;
import capstone.product.manager.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class ProductController {

    private final ProductManager manger;

    @Autowired
    public ProductController(ProductManager manger) {
        this.manger = manger;
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Product> getById(
            @PathVariable(value = "productId", required = true) String productId
    ) {
        ProductResponse response = manger.getProductById(productId);
        return response.getProduct() == null ? new ResponseEntity<>(response.getHttpStatus()) : new ResponseEntity<>(response.getProduct(), HttpStatus.OK);
    }

    @RequestMapping(value = "/product/sku/{sku}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Product>> getBySKU(
            @PathVariable(value = "sku", required = true) String sku
    ) {
        ProductListResponse response = manger.getBySKU(sku);
        return response.getProducts() == null ? new ResponseEntity<>(response.getHttpStatus()) : new ResponseEntity<>(response.getProducts(), HttpStatus.OK);
    }

}