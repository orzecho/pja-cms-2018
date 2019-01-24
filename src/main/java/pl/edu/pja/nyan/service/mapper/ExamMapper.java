package pl.edu.pja.nyan.service.mapper;

import java.util.HashSet;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Exam;
import pl.edu.pja.nyan.repository.ExamRepository;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.dto.ExamDTO;

/**
 * Mapper for the entity Exam and its DTO ExamDTO.
 */
@Service
@RequiredArgsConstructor
public class ExamMapper implements EntityMapper<ExamDTO, Exam> {

    private final ExamRepository examRepository;
    private final ExamResultMapper examResultMapper;
    private final WordMapper wordMapper;
    private final UserService userService;

    @Override
    public Exam toEntity(ExamDTO dto) {
        Exam entity;
        if (dto.getId() == null) {
            entity = new Exam();
        } else {
            entity = examRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);
        }
        entity.setCode(dto.getCode());
        entity.setCreator(userService.getUserWithAuthorities(dto.getCreatorId())
                .orElseThrow(EntityNotFoundException::new));
        entity.setName(dto.getName());
        entity.setResults(dto.getResults() == null ? null :
            new HashSet<>(examResultMapper.toEntity(dto.getResults())));
        entity.setType(dto.getType());
        entity.setWords(dto.getWords() == null ? null :
            new HashSet<>(wordMapper.toEntity(dto.getWords())));
        return entity;
    }

    @Override
    public ExamDTO toDto(Exam entity) {
        return ExamDTO.builder()
            .id(entity.getId())
            .code(entity.getCode())
            .creatorLogin(entity.getCreator().getLogin())
            .creatorId(entity.getCreator().getId())
            .name(entity.getName())
            .results(examResultMapper.toDto(entity.getResults()))
            .type(entity.getType())
            .words(entity.getWords() == null ? null :
                new HashSet<>(wordMapper.toDto(entity.getWords())))
            .build();
    }

    public Exam fromId(Long id) {
        if (id == null) {
            return null;
        }
        Exam exam = new Exam();
        exam.setId(id);
        return exam;
    }

}
