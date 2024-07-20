package subway;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.common.exception.SubWayException;
import subway.line.domain.model.LineResponse;
import subway.section.model.SectionRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
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
        LineResponse lineOld = addSection(1, 2, 10);
        LineResponse lineNew = addSection(2, 3, 12);

        log.info("old line - {}", lineOld);
        log.info("new line - {}", lineNew);
    }

    @DisplayName("지하철 1호선 마지막 역을 제거하고(3번역), 구간도 이전 구간으로(1~2번역) 되돌리는 테스트")
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"classpath:data/insert-station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_section_test() {
        //given
        // 구간이 한개인 경우 삭제 불가하기 때문에 구역 증가
        addSection(1, 2, 10);
        addSection(2, 3, 12);

        //when
        // 최근 추가된 마지막 역 삭제
        delete(3);

        // 이전 추가된 마지막 역 id와(2) 일치
        boolean result = search(1).getStations().stream().anyMatch(it -> it.getId() == 2);
        assertTrue(result);
    }

    @DisplayName("지하철 1호선 구간이 하니인 경우 실패하는 테스트")
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"classpath:data/insert-station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_section_failure_test() {
        //when
        addSection(1, 1, 10);
        //then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value() ,delete(1));
    }

    @DisplayName("지하철 1호선 새로운 구간의 상행역이 기존 구간의 하행역 종착역이 아닌 경우 실패하는 테스트")
    @Test
    @Sql(value = {"classpath:data/reset.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"classpath:data/insert-station.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_section_failure_test2() {
        //when
        addSection(1, 3, 10);
        //then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value() ,delete(2));
    }

    private LineResponse addSection(int upStation,
                                    int downStation,
                                    int distance) {
        return RestAssured.given()
                          .body(new SectionRequest(upStation, downStation, distance))
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/lines/" + 1 + "/sections").body().as(LineResponse.class);
    }

    private int delete(long stationId) {
        return RestAssured.given()
                          .param("stationId", stationId)
                          .delete("/lines/" + 1 + "/sections").getStatusCode();
    }

    private LineResponse search(long id) {
        return RestAssured.get("/lines/" + id).body().as(LineResponse.class);
    }
}
