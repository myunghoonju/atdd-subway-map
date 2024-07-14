package subway;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.domain.model.LineResponse;
import subway.section.model.SectionRequest;

@DisplayName("지하철 구간 관리")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubWaySectionTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("지하철 1호선 구간을 등록하는 테스트")
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"classpath:data/insert-station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void save_section_test() {
        LineResponse lineOld = save(1, 2, 10);
        LineResponse lineNew = save(2, 3, 12);

        System.out.println(lineOld);
        System.out.println("*****");
        System.out.println(lineNew);
    }

    @DisplayName("지하철 1호선 마지막 역을 제거하고, 구간도 이전 구간으로 되돌리는 테스트")
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"classpath:data/insert-station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_section_test() {
        //given
        // 구간이 한개인 경우 삭제 불가하기 때문에 구역 증가
        save(1, 2, 10);
        save(2, 3, 12);

        //when
        // 최근 추가된 마지막 역 삭제
        delete(3);

        // 이전 추가된 마지막 역 id와(2) 일치
        Assertions.assertThat(search(1).getDownStationId()).isEqualTo(2);
    }

    private LineResponse save(int upStation,
                              int downStation,
                              int distance) {
        return RestAssured.given()
                          .body(new SectionRequest(upStation, downStation, distance))
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines/" + 1 + "/sections").body().as(LineResponse.class);
    }

    private void delete(long stationId) {
        RestAssured.given()
                   .param("stationId", stationId)
                   .delete("/lines/" + 1 + "/sections");
    }

    private LineResponse search(long id) {
        return RestAssured.get("/lines/" + id).body().as(LineResponse.class);
    }
}
