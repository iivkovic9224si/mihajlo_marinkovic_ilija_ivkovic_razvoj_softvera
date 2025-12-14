package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import java.util.List;

@Data
public class PageResultDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number; // trenutna strana
    private int size;   // veličina strane
}
