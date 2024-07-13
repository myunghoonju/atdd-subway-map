package subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.domain.model.LineResponse;
import subway.model.LineTestRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DisplayName("지하철 노선 관리")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubwaySystemTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    //지하철 노선을 생성한다
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"classpath:data/insert-station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void line_creation_test() {
        //when
        save("신분당선", "bg-red-600", 1, 2, 10);

        //then
        LineResponse line = search("신분당선");
        Assertions.assertEquals("신분당선", line.getName());
    }

    //지하철 노선 목록을 조회한다
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_lines_test() {
        //given
        save("1호선", "bg-red-600", 1, 2, 10);
        save("2호선", "bg-green-600", 3, 4, 9);
        save("3호선", "bg-orange-600", 5, 6, 8);

        //when
        List<LineResponse> lines = search();

        //then
        Assertions.assertEquals(3, lines.size());
    }

    //특정 지하철 노선을 조회한다
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_line_test() {
        //given
        LineResponse line = save("1호선", "bg-red-600", 1, 2, 10);

        //when
        LineResponse res = search(line.getId());

        //then
        Assertions.assertEquals("1호선", res.getName());
    }

    //지하철 노선을 수정한다
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void edit_line_test() {
        //given
        LineResponse line = save("1호선", "bg-red-600", 1, 2, 10);

        //when
        edit(line.getId(), "수정호선", "bg-blue-600");

        //then
        LineResponse searched = search(line.getId());
        Assertions.assertEquals("수정호선", searched.getName());
        Assertions.assertEquals("bg-blue-600", searched.getColor());
    }

    //특정 지하철 노선을 삭제한다
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_line_test() {
        //given
        LineResponse line = save("1호선", "bg-red-600", 1, 2, 10);

        //when
        delete(line.getId());

        //then
        Assertions.assertEquals(List.of(), search());
    }

    private LineResponse save(String name,
                              String color,
                              int upStationId,
                              int downStationId,
                              int distance) {
        return RestAssured.given()
                          .body(new LineTestRequest(name, color, upStationId, downStationId, distance))
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines").body().as(LineResponse.class);
    }

    private List<LineResponse> search() {
        LineResponse[] lines = RestAssured.get("/lines").body().as(LineResponse[].class);
        return Arrays.stream(lines).collect(Collectors.toList());
    }

    private LineResponse search(long id) {
        return RestAssured.get("/lines/" + id).body().as(LineResponse.class);
    }

    private LineResponse search(String stationName) {
        LineResponse[] lines = RestAssured.get("/lines").body().as(LineResponse[].class);
        List<LineResponse> station = Arrays.stream(lines)
                                           .filter(it -> it.getName().equals(stationName))
                                           .collect(Collectors.toList());
        return station.get(0);
    }

    private void edit(long id, String name, String color) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("color", color);

        RestAssured.given()
                   .body(param)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .put("/lines/" + id);
    }

    private void delete(long id) {
        RestAssured.delete("/lines/" + id);
    }
}
