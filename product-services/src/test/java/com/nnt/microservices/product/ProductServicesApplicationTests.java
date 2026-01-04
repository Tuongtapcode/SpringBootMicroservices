package com.nnt.microservices.product;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.mongodb.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServicesApplicationTests {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

    @LocalServerPort
    private Integer port;


    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldCreateProduct() {
        String requestBody = """
                {
                    "name": "iPhone 15",
                    "description":"iPhone from Apple",
                    "price":1000
                }""";
        RestAssured.given().contentType(ContentType.JSON)
                .body(requestBody).when().post("/api/product").then().statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.is("iPhone 15"))
                .body("description", Matchers.is("iPhone from Apple"))
                .body("price", Matchers.is(1000));
    }

}
