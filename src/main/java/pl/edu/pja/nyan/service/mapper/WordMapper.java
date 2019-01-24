package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.repository.WordRepository;
import pl.edu.pja.nyan.service.dto.WordDTO;

@Service
@RequiredArgsConstructor
public class WordMapper implements EntityMapper<WordDTO, Word> {

    private final TagMapper tagMapper;

    private final WordRepository wordRepository;

    @Override
    public Word toEntity(WordDTO dto) {
        Word word;
        if (dto.getId() == null) {
            word = new Word();
        } else {
            word = wordRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        }
        word.setKana(dto.getKana());
        word.setKanji(dto.getKanji());
        word.setNote(dto.getNote());
        word.setRomaji(dto.getRomaji());
        word.setTranslation(dto.getTranslation());
        word.setTags(Sets.newHashSet(tagMapper.toEntity(dto.getTags())));

        return word;
    }

    @Override
    public WordDTO toDto(Word entity) {
        return WordDTO.builder()
            .id(entity.getId())
            .romaji(entity.getRomaji())
            .note(entity.getNote())
            .kanji(entity.getKanji())
            .kana(entity.getKana())
            .translation(entity.getTranslation())
            .tags(tagMapper.toDto(entity.getTags()))
            .rawTags(String.join(",", entity.getTags().stream().map(Tag::getName).collect(Collectors.toList())))
            .build();
    }

    @Override
    public List<Word> toEntity(Collection<WordDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<WordDTO> toDto(Collection<Word> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Word fromId(Long id) {
        if (id == null) {
            return null;
        }
        Word word = new Word();
        word.setId(id);
        return word;
    }

}
