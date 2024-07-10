package subway.line.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.line.domain.LineService;
import subway.line.domain.model.LineRequest;
import subway.line.domain.model.LineResponse;
import subway.line.domain.model.LineUpdateRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService service;

    @GetMapping
    public ResponseEntity<List<LineResponse>> lines() {
        return ResponseEntity.ok().body(service.lines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> line(@PathVariable long id) {
        return ResponseEntity.ok().body(service.searchLine(id));
    }

    @PostMapping
    public ResponseEntity<LineResponse> addLine(@RequestBody LineRequest lineReq) {
        return ResponseEntity.created(URI.create("/lines")).body(service.addLine(lineReq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable long id,
                                           @RequestBody LineUpdateRequest lineReq) {
        service.editLine(id, lineReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable long id) {
        service.removeLine(id);
        return ResponseEntity.noContent().build();
    }
}
