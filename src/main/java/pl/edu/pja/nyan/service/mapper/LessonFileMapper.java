package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.domain.LessonFile;
import pl.edu.pja.nyan.repository.LessonRepository;
import pl.edu.pja.nyan.service.dto.LessonFileDTO;
import pl.edu.pja.nyan.service.dto.LessonFileShortDTO;

@RequiredArgsConstructor
@Service
public class LessonFileMapper implements EntityMapper<LessonFileDTO,LessonFile> {
    private final LessonRepository lessonRepository;

    @Override
    public LessonFile toEntity(LessonFileDTO dto) {
        LessonFile lessonFile = new LessonFile();
        lessonFile.setName(dto.getName());
        lessonFile.setId(dto.getId());
        lessonFile.setContent(dto.getContent());
        lessonFile.setContentContentType(dto.getContentContentType());
        lessonFile.setLesson(Optional.ofNullable(dto.getLessonId())
            .map(id -> lessonRepository.findById(id).orElse(null)).orElse(null));

        return lessonFile;
    }

    @Override
    public LessonFileDTO toDto(LessonFile entity) {
        return LessonFileDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .content(entity.getContent())
            .contentContentType(entity.getContentContentType())
            .lessonId(Optional.ofNullable(entity.getLesson()).map(Lesson::getId).orElse(null))
            .lessonName(Optional.ofNullable(entity.getLesson()).map(Lesson::getName).orElse(null))
            .build();
    }

    public LessonFileShortDTO toShortDto(LessonFile entity) {
        return LessonFileShortDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .lessonId(Optional.ofNullable(entity.getLesson()).map(Lesson::getId).orElse(null))
            .lessonName(Optional.ofNullable(entity.getLesson()).map(Lesson::getName).orElse(null))
            .build();
    }

    @Override
    public List<LessonFile> toEntity(Collection<LessonFileDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LessonFileDTO> toDto(Collection<LessonFile> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<LessonFileShortDTO> toShortDto(Collection<LessonFile> entityList) {
        return entityList.stream().map(this::toShortDto).collect(Collectors.toList());
    }
}
