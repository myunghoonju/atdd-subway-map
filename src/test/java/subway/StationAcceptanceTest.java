package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.domain.model.StationResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("지하철역을 생성한다.")
    void createStation() {
        //When 지하철역을 생성하면
        Map<String, String> params = new HashMap<>();
        params.put("name", "jamsil");

        //Then 지하철역이 생성된다
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(params)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when().post("/stations")
                                                            .then().log().all()
                                                            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        List<String> stationNames = RestAssured.given().log().all()
                                               .when().get("/stations")
                                               .then().log().all()
                                               .statusCode(HttpStatus.OK.value())
                                               .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("jamsil");
    }

    @Test
    @DisplayName("지하철역 목록 조회 인수 테스트")
    void search_stations() {
        //Given 2개의 지하철역을 생성하고
        save("Jang-Am");
        save("Nowon");

        //when 지하철역 목록을 조회하면
        List<String> stations = searchAll();

        //then 2개의 지하철역을 응답 받는다
        assertThat(stations).containsAnyOf("Nowon", "Jang-Am");
    }

    @Test
    @DisplayName("지하철역 제거 인수 테스트 메서드 생성")
    void delete_station() {
        //Given 지하철역을 생성하고
        save("Madeul");

        //when 그 지하철 역을 삭제하면
        delete("Madeul");

        //then 그 지하철 역 목록 조회 시 찾을 수 없다
        List<String> stations = searchAll();
        assertThat(stations).isNotIn("Madeul");
    }



    private void save(String name) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);

        RestAssured.given()
                   .body(param)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .post("/stations");
    }

    private List<String> searchAll() {
        return RestAssured.get("/stations").body().jsonPath().getList("name");
    }

    private void delete(String name) {
        RestAssured.delete("/stations/" + search(name));
    }

    private long search(String name) {
        StationResponse[] stations = RestAssured.get("/stations").body().as(StationResponse[].class);

        List<StationResponse> station = Arrays.stream(stations)
                                              .filter(it -> name.equals(it.getName()))
                                              .collect(Collectors.toList());

        return station.get(0).getId();
    }
}
