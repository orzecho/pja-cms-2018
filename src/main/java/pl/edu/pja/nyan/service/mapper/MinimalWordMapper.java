package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.service.dto.MinimalWordDTO;

@Service
@RequiredArgsConstructor
public class MinimalWordMapper {

    public MinimalWordDTO toDto(Word entity) {
        return MinimalWordDTO.builder()
            .id(entity.getId())
            .kanji(entity.getKanji())
            .kana(entity.getKana())
            .translation(entity.getTranslation())
            .romaji(entity.getRomaji())
            .build();
    }

    public List<MinimalWordDTO> toDto(Collection<Word> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
