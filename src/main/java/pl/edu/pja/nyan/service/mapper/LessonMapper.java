package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.domain.LessonFile;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.repository.LessonFileRepository;
import pl.edu.pja.nyan.repository.LessonRepository;
import pl.edu.pja.nyan.service.dto.LessonDTO;
import pl.edu.pja.nyan.service.dto.LessonFileShortDTO;

@RequiredArgsConstructor
@Service
public class LessonMapper implements EntityMapper<LessonDTO,Lesson> {

    private final LessonFileMapper lessonFileMapper;
    private final LessonFileRepository lessonFileRepository;
    private final LessonRepository lessonRepository;
    private final TagMapper tagMapper;

    @Override
    public Lesson toEntity(LessonDTO dto) {
        Lesson lesson;
        if (dto.getId() != null) {
            lesson = lessonRepository.getOne(dto.getId());
        } else {
            lesson = new Lesson();
        }
        lesson.setId(dto.getId());
        lesson.setDescription(dto.getDescription());
        lesson.setName(dto.getName());
        Optional.ofNullable(dto.getLessonFiles()).ifPresent(e -> updateFiles(e, lesson));
        return lesson;
    }

    @Override
    public LessonDTO toDto(Lesson entity) {
        return LessonDTO.builder()
            .id(entity.getId())
            .description(entity.getDescription())
            .name(entity.getName())
            .lessonFiles(lessonFileMapper.toShortDto(lessonFileRepository.findByLesson(entity)))
            .tags(tagMapper.toDto(entity.getTags()))
            .rawTags(String.join(",", entity.getTags().stream().map(Tag::getName).collect(Collectors.toList())))
            .build();
    }

    @Override
    public List<Lesson> toEntity(Collection<LessonDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LessonDTO> toDto(Collection<Lesson> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    private void updateFiles(List<LessonFileShortDTO> lessonFiles, Lesson lesson) {
        lesson.getLessonFiles().forEach(e -> {
            e.setLesson(null);
        });
        lesson.setLessonFiles(new HashSet<>());
        lessonFiles
            .forEach(e -> {
                LessonFile lessonFile = lessonFileRepository.findById(e.getId())
                    .orElseThrow(EntityNotFoundException::new);
                lesson.addLessonFile(lessonFile);
            });
    }
}
