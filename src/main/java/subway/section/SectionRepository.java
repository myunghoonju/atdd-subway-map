package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Section findByEndAndLineId(long end, long lineId);

    Section findTopByLineIdAndActiveOrderByEndDesc(long lineId, boolean active);
}
