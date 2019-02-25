package capstone.product.dto;

import java.math.BigDecimal;

public class Product {

    private String productId;
    private String sku;
    private String nameTitle;
    private String description;
    private BigDecimal listPrice;
    private BigDecimal salePrice;
    private String category;
    private String categoryTree;
    private String averageProductRating;
    private String productUrl;
    private String productImageUrls;
    private String brand;
    private BigDecimal totalNumberReviews;
    private String reviews;

    public String getProductId() {
        return productId;
    }

    public String getSku() {
        return sku;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryTree() {
        return categoryTree;
    }

    public String getAverageProductRating() {
        return averageProductRating;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductImageUrls() {
        return productImageUrls;
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getTotalNumberReviews() {
        return totalNumberReviews;
    }

    public String getReviews() {
        return reviews;
    }
}
