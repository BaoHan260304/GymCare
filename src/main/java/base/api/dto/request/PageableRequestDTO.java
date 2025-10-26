package base.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequestDTO {
    private int pageNumber = 0;
    private int pageSize = 10;
    private String keyword = null;
    public Pageable toPageable() {
        return PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // Validation
    public void validate() {
        if (pageNumber < 0) this.pageNumber = 0;
        if (pageSize <= 0 || pageSize > 100) this.pageSize = 10;

    }
}
