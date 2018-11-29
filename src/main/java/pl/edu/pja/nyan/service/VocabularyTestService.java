package pl.edu.pja.nyan.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.repository.WordRepository;
import pl.edu.pja.nyan.service.dto.test.VocabularyTestItemDTO;
import pl.edu.pja.nyan.service.mapper.WordMapper;

@Service
@RequiredArgsConstructor
public class VocabularyTestService {

    private final WordRepository wordRepository;
    private final WordMapper wordMapper;

    public enum VocabularyTestType {
        TRANSLATION, KANA, KANJI, MIXED
    }

    private Map<VocabularyTestType, Function<List<Word>, List<VocabularyTestItemDTO>>> testGenerators =
        ImmutableMap.of(
            VocabularyTestType.TRANSLATION, this::getTestItemsForSimpleTranslationTest,
            VocabularyTestType.KANA, this::getTestItemsForKanaTest,
            VocabularyTestType.KANJI, this::getTestItemsForKanjiTest,
            VocabularyTestType.MIXED, this::getTestItemsForMixedTest
        );

    @Transactional
    public List<VocabularyTestItemDTO> generateTest(List<Tag> tags, VocabularyTestType type) {
        List<Word> words = getWordByTags(tags);

        return testGenerators.get(type).apply(words);
    }

    private List<Word> getWordByTags(List<Tag> tags) {
        //TODO być może da się to sensownie zrobić w bazie, ale na razie nie mam pomysłu jak bez QueryDSL
        return wordRepository.findAll().stream()
            .filter(word -> word.getTags().stream().anyMatch(tags::contains))
            .collect(Collectors.toList());
    }

    private List<VocabularyTestItemDTO> getTestItemsForSimpleTranslationTest(List<Word> words) {
        return words.stream()
            .map(this::buildForSimpleTranslationTest)
            .collect(Collectors.toList());
    }

    private VocabularyTestItemDTO buildForSimpleTranslationTest(Word word) {
        return VocabularyTestItemDTO.builder()
            .kanaFromSystem(word.getKana())
            .kanaFromUser("")
            .kanaCorrect(false)
            .translationFromSystem(word.getTranslation())
            .translationFromUser(word.getTranslation())
            .translationCorrect(true)
            .kanjiFromSystem(word.getKanji())
            .kanjiFromUser("")
            .kanjiCorrect(false)
            .word(wordMapper.toDto(word))
            .build();
    }

    private List<VocabularyTestItemDTO> getTestItemsForKanaTest(List<Word> words) {
        return words.stream()
            .map(this::buildForKanaTest)
            .collect(Collectors.toList());
    }

    private VocabularyTestItemDTO buildForKanaTest(Word word) {
        return VocabularyTestItemDTO.builder()
            .kanaFromSystem(word.getKana())
            .kanaFromUser(word.getKana())
            .kanaCorrect(true)
            .translationFromSystem(word.getTranslation())
            .translationFromUser("")
            .translationCorrect(false)
            .kanjiFromSystem(word.getKanji())
            .kanjiFromUser("")
            .kanjiCorrect(false)
            .word(wordMapper.toDto(word))
            .build();
    }

    private List<VocabularyTestItemDTO> getTestItemsForKanjiTest(List<Word> words) {
        return words.stream()
            .map(this::buildForKanjiTest)
            .collect(Collectors.toList());
    }

    private VocabularyTestItemDTO buildForKanjiTest(Word word) {
        return VocabularyTestItemDTO.builder()
            .kanaFromSystem(word.getKana())
            .kanaFromUser("")
            .kanaCorrect(false)
            .translationFromSystem(word.getTranslation())
            .translationFromUser("")
            .translationCorrect(false)
            .kanjiFromSystem(word.getKanji())
            .kanjiFromUser(word.getKanji())
            .kanjiCorrect(true)
            .word(wordMapper.toDto(word))
            .build();
    }

    private List<VocabularyTestItemDTO> getTestItemsForMixedTest(List<Word> words) {
        return words.stream()
            .map(word -> {
                Random random = new Random();
                int r = random.nextInt(2);
                if (r == 0) {
                    return buildForSimpleTranslationTest(word);
                } else if (r == 1) {
                    return buildForKanaTest(word);
                } else {
                    return buildForKanjiTest(word);
                }
            })
            .collect(Collectors.toList());
    }
}
