package subway.line.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.domain.LineService;
import subway.line.domain.model.LineRequest;
import subway.line.domain.model.LineResponse;
import subway.line.domain.model.LineUpdateRequest;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {

    private final LineService service;

    public LineController(LineService service) {
        this.service = service;
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> lines() {
        return ResponseEntity.ok().body(service.lines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> line(@PathVariable long id) {
        return ResponseEntity.ok().body(service.line(id));
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> addLine(@RequestBody LineRequest lineReq) {
        return ResponseEntity.created(URI.create("/lines")).body(service.addLine(lineReq));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable long id,
                                           @RequestBody LineUpdateRequest lineReq) {
        service.editLine(id, lineReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable long id) {
        service.removeLine(id);
        return ResponseEntity.noContent().build();
    }
}
