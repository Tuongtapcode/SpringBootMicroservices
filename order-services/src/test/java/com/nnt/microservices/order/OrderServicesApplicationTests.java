package com.nnt.microservices.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServicesApplicationTests {
    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");

    @LocalServerPort
    private Integer port;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setupAll() {
        mySQLContainer.start();

        // Khởi động WireMock với cấu hình (WireMock 2.x syntax)
        wireMockServer = new WireMockServer(wireMockConfig().port(8089));
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @AfterAll
    static void tearDownAll() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Reset tất cả stubs trước mỗi test
        wireMockServer.resetAll();
    }

    @Test
    void shouldSubmitOrder() {
        String submitOrderJson = """
                {
                     "skuCode": "iphone_15",
                     "price": 1000,
                     "quantity": 1
                }
                """;

        // Stub inventory API response
        stubFor(get(urlEqualTo("/api/inventory?skuCode=iphone_15&quantity=1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));
    }
}