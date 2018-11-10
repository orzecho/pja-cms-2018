package pl.edu.pja.nyan.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LessonFileShortDTO {
    private Long id;
    private String name;
    private Long lessonId;
    private String lessonName;
}
