    package response;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.jetbrains.annotations.Nullable;

    import java.time.LocalDate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class StudentIndeksResponse {
        private Long id;
        private int broj;
        private int godina;
        private String studProgramOznaka;
        private String nacinFinansiranja;
        private boolean aktivan;
        private LocalDate vaziOd;
        @Nullable
        private StudentPodaciResponse student;
        private StudijskiProgramResponse studijskiProgramResponse;
        private Integer ostvarenoEspb;
    }
