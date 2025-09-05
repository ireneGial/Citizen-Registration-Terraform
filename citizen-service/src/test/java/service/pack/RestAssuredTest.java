package service.pack;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jpa.repository.JPACitizenRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;

import domain.pack.Citizen;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) 
public class RestAssuredTest {
	
	@Autowired
    private JPACitizenRepository citizenRepository;

    @BeforeAll
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080; 
        
        
        Citizen citizen = new Citizen("87654321", "ΑΝΝΑ", "ΠΑΠΑΔΗΜΗΤΡΙΟΥ", "ΓΥΝΑΙΚΑ",
                "15-05-1990", "987654321", "ΘΕΣΣΑΛΟΝΙΚΗ");
        citizenRepository.save(citizen);
    }   

    @Test
    void testCreateCitizen() {
        String newCitizenJson = """
        {
          "at": "55555555",
          "firstName": "ΔΗΜΗΤΡΗΣ",
          "lastName": "ΝΙΚΟΛΑΟΥ",
          "gender": "ΑΝΔΡΑΣ",
          "dateOfBirth": "10-10-1990",
          "afm": "123456789",
          "address": "ΑΘΗΝΑ"
        }-
        """;

        given()
          .contentType(ContentType.JSON)
          .body(newCitizenJson)
        .when()
          .post("/api/citizens")
        .then()
          .statusCode(201)
          .body("at", equalTo("55555555"))
          .body("firstName", equalTo("ΔΗΜΗΤΡΗΣ"));
    }

    @Test
    void testGetCitizenByAt() {
    	 String at = "87254111";
         Citizen citizen = new Citizen(at, "ΔΗΜΗΤΡΗΣ", "ΝΙΚΟΛΑΟΥ", "ΑΝΔΡΑΣ", "10-10-1990", "123456789", "ΑΘΗΝΑ");
         citizenRepository.save(citizen);
        when()
          .get("/api/citizens/{at}", at)
        .then()
          .statusCode(200)
          .body("at", equalTo(at));
    }

    @Test
    void testUpdateCitizen() {
        String at = "55555555";

        String updateJson = """
        {
          "afm": "987654321",
          "address": "ΘΕΣΣΑΛΟΝΙΚΗ"
        }
        """;

        given()
          .contentType(ContentType.JSON)
          .body(updateJson)
        .when()
          .put("/api/citizens/{at}", at)
        .then()
          .statusCode(200)
          .contentType("text/plain")
          .body(equalTo("Ο πολίτης ενημερώθηκε επιτυχώς."));
    }

    @Test
    void testDeleteCitizen() {
        String at = "87654111";
        Citizen citizen = new Citizen(at, "ΔΗΜΗΤΡΗΣ", "ΝΙΚΟΛΑΟΥ", "ΑΝΔΡΑΣ", "10-10-1990", "123456789", "ΑΘΗΝΑ");
        citizenRepository.save(citizen);
        when()
          .delete("/api/citizens/{at}", at)
        .then()
          .statusCode(200);

        when()
          .get("/api/citizens/{at}", at)
        .then()
          .statusCode(404);
    }

    @Test
    void testSearchCitizens() {
    	Citizen citizen = new Citizen("81154111", "ΔΗΜΗΤΡΗΣ", "ΝΙΚΟΛΑΟΥ", "ΑΝΔΡΑΣ", "10-10-1990", "123456789", "ΑΘΗΝΑ");
        citizenRepository.save(citizen);
        given()
          .queryParam("firstName", "ΔΗΜΗΤΡΗΣ")
        .when()
          .get("/api/citizens")
        .then()
          .statusCode(200)
          .body("size()", greaterThan(0));
    }
}
