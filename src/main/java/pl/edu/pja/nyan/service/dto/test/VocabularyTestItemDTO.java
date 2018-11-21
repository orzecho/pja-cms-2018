package pl.edu.pja.nyan.service.dto.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VocabularyTestItemDTO {
    private String translationFromSystem;
    private String translationFromUser;
    private Boolean translationCorrect;

    private String kanaFromSystem;
    private String kanaFromUser;
    private Boolean kanaCorrect;

    private String kanjiFromSystem;
    private String kanjiFromUser;
    private Boolean kanjiCorrect;
}
