package subway.line.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineResponse {

    private long id;

    private String name;

    private String color;

    private List<LineStation> stations = new ArrayList<>();
}
