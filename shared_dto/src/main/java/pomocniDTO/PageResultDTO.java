package pomocniDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResultDTO<T> {

    private List<T> content;  // lista elemenata na strani
    private int totalPages;
    private long totalElements;  // ukupan broj elemenata
    private int pageNumber;   // trenutna strana
    private int pageSize;     // veličina strane
}
