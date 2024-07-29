package subway.line.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.line.domain.Line;
import subway.line.domain.LineService;
import subway.line.domain.model.LineRequest;
import subway.line.domain.model.LineResponse;
import subway.line.domain.model.LineUpdateRequest;
import subway.section.SectionService;
import subway.section.model.SectionRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService service;
    private final SectionService sectionService;
    private final LineService lineService;

    @GetMapping
    public ResponseEntity<List<LineResponse>> lines() {
        return ResponseEntity.ok().body(service.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> line(@PathVariable long id) {
        return ResponseEntity.ok().body(service.findLineAndStations(id));
    }

    @PostMapping
    public ResponseEntity<LineResponse> addLine(@RequestBody LineRequest lineReq) {
        return ResponseEntity.created(URI.create("/lines")).body(service.addLine(lineReq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable long id, @RequestBody LineUpdateRequest lineReq) {
        service.editLine(id, lineReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable long id) {
        sectionService.deactivateSection(id);
        service.removeLine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<LineResponse> addSection(@PathVariable long id, @RequestBody SectionRequest sectionReq) {
        Line line = service.findLine(id);
        LineResponse res = LineResponse.of(line, sectionService.addSectionToLine(line, sectionReq));

        return ResponseEntity.created(URI.create("/lines" + id + "/sections")).body(res);
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable long id, @RequestParam long stationId) {
        sectionService.removeSection(id, stationId);

        Line line = lineService.findLine(id);
        line.refresh();

        return ResponseEntity.ok().build();
    }
}
