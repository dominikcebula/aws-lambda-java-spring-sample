package com.dominikcebula.samples.aws.lambda.products.catalog;

import com.dominikcebula.samples.aws.lambda.products.catalog.db.entity.Product;
import com.dominikcebula.samples.aws.lambda.products.catalog.shared.http.product.ProductDTO;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.DescribeStacksRequest;
import software.amazon.awssdk.services.cloudformation.model.DescribeStacksResponse;
import software.amazon.awssdk.services.cloudformation.model.Output;
import software.amazon.awssdk.services.cloudformation.model.Stack;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.text.IsBlankString.blankString;
import static org.springframework.http.HttpHeaders.LOCATION;

public class ProductsCatalogTest {
    private static final String ENV_STAGE_NAME = "dev";

    private static String apiEndpoint;

    @BeforeAll
    static void setUp() {
        apiEndpoint = getApiEndpoint();
    }

    @Test
    void shouldRetrieveAnyListOfProducts() {
        given()
                .baseUri(apiEndpoint)
                .when()
                .get("/products")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(not(blankString()));
    }

    @Test
    void shouldCreateProduct() {
        ProductDTO productDTO = buildProductDTO();

        Product createdProduct = given()
                .baseUri(apiEndpoint)
                .when()
                .body(productDTO)
                .post("/products")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .header(LOCATION, not(blankString()))
                .extract()
                .as(Product.class);

        assertProductData(createdProduct);
    }

    @Test
    void shouldCreateAndRetrieveProductByHeader() {
        ProductDTO productDTO = buildProductDTO();

        Response response = createProduct(productDTO);

        Product retrievedProduct = retrieveProductByLocationHeader(response);
        assertProductData(retrievedProduct);
    }

    @Test
    void shouldCreateAndRetrieveProductById() {
        ProductDTO productDTO = buildProductDTO();

        Response response = createProduct(productDTO);

        Product retrievedProduct = retrieveProductById(response);
        assertProductData(retrievedProduct);
    }

    @Test
    void shouldCreateAndListMultipleProducts() {
        ProductDTO productDTO1 = buildProductDTO();
        ProductDTO productDTO2 = buildProductDTO();
        ProductDTO productDTO3 = buildProductDTO();

        Set<String> createdProductIds = Set.of(
                createProductAndExtractId(productDTO1),
                createProductAndExtractId(productDTO2),
                createProductAndExtractId(productDTO3)
        );

        List<Product> retrievedProduct = retrievedProducts();

        List<Product> filteredProducts = retrievedProduct.stream()
                .filter(product -> createdProductIds.contains(product.getId()))
                .toList();

        assertProductsData(filteredProducts);
    }

    private static String getApiEndpoint() {
        try (CloudFormationClient cloudFormationClient = CloudFormationClient.builder().httpClient(ApacheHttpClient.create()).build()) {
            DescribeStacksResponse response = cloudFormationClient.describeStacks(DescribeStacksRequest.builder()
                    .stackName("products-catalog-lambda-app")
                    .build());

            return response.stacks().stream()
                    .filter(stack -> stack.stackName().equals("products-catalog-lambda-app"))
                    .map(Stack::outputs)
                    .flatMap(List::stream)
                    .filter(output -> output.outputKey().equals("ApiEndpoint"))
                    .map(Output::outputValue)
                    .findFirst()
                    .map(endpoint -> endpoint + ENV_STAGE_NAME)
                    .orElseThrow(() -> new IllegalStateException("Unable to find API Endpoint for testing based on Cloud Formation Stack"));
        }
    }

    private ProductDTO buildProductDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(PRODUCT_NAME);
        productDTO.setDescription(PRODUCT_DESCRIPTION);
        productDTO.setCategory(PRODUCT_CATEGORY);
        productDTO.setSku(PRODUCT_SKU);
        productDTO.setPrice(PRODUCT_PRICE);
        return productDTO;
    }

    private String createProductAndExtractId(ProductDTO productDTO) {
        Response response = createProduct(productDTO);
        return response.body().as(Product.class).getId();
    }

    private Response createProduct(ProductDTO productDTO) {
        Response response = given()
                .baseUri(apiEndpoint)
                .when()
                .body(productDTO)
                .post("/products");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.body().asString()).isNotBlank();

        return response;
    }

    private List<Product> retrievedProducts() {
        return given()
                .baseUri(apiEndpoint)
                .when()
                .get("/products")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });
    }

    private void assertProductsData(List<Product> filteredProducts) {
        filteredProducts.forEach(this::assertProductData);
    }

    private void assertProductData(Product createdProduct) {
        assertThat(createdProduct.getId()).isNotBlank();
        assertThat(createdProduct.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(createdProduct.getDescription()).isEqualTo(PRODUCT_DESCRIPTION);
        assertThat(createdProduct.getCategory()).isEqualTo(PRODUCT_CATEGORY);
        assertThat(createdProduct.getSku()).isEqualTo(PRODUCT_SKU);
        assertThat(createdProduct.getPrice()).isEqualTo(PRODUCT_PRICE);
    }

    private Product retrieveProductByLocationHeader(Response response) {
        return given()
                .baseUri(apiEndpoint)
                .when()
                .get(response.getHeader(LOCATION))
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Product.class);
    }

    private Product retrieveProductById(Response response) {
        String createdProductId = response.body().as(Product.class).getId();

        return given()
                .baseUri(apiEndpoint)
                .when()
                .get("/products/" + createdProductId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Product.class);
    }

    private static final String PRODUCT_NAME = "Stainless Steel Water Bottle";
    private static final String PRODUCT_DESCRIPTION = "Double-wall insulated, leak-proof water bottle with 24-hour cold and 12-hour hot retention.";
    private static final String PRODUCT_CATEGORY = "Home & Kitchen";
    private static final String PRODUCT_SKU = "WB-98765-SS";
    private static final BigDecimal PRODUCT_PRICE = new BigDecimal("24.99");
}
