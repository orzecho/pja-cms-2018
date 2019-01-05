package pl.edu.pja.nyan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.WordsTest;
import pl.edu.pja.nyan.domain.enumeration.TestType;
import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.service.dto.WordsTestDTO;
import pl.edu.pja.nyan.service.dto.test.VocabularyTestItemDTO;

@Service
@RequiredArgsConstructor
public class VocabularyTestService {

    private Map<TestType, Function<List<WordDTO>, List<VocabularyTestItemDTO>>> testGenerators =
        ImmutableMap.of(
            TestType.WRITTEN_PL, this::getTestItemsForSimpleTranslationTest,
            TestType.WRITTEN_KANA, this::getTestItemsForKanaTest,
            TestType.WRITTEN_KANJI, this::getTestItemsForKanjiTest,
            TestType.WRITTEN_MIXED, this::getTestItemsForMixedTest
        );

    public boolean isWrittenTypeOfTest(TestType testType) {
        return testGenerators.containsKey(testType);
    }

    @Transactional
    public List<VocabularyTestItemDTO> generateTest(WordsTestDTO test) {
        Function<List<WordDTO>, List<VocabularyTestItemDTO>> generator = testGenerators.get(test.getType());
        if (generator == null) {
            throw new IllegalArgumentException("This service doesn't support "
                + "the generation such kind of tests: " + test.getType());
        }

        return generator.apply(new ArrayList<>(test.getWords()));
    }

    private List<VocabularyTestItemDTO> getTestItemsForSimpleTranslationTest(List<WordDTO> words) {
        return words.stream()
            .map(this::buildForSimpleTranslationTest)
            .collect(Collectors.toList());
    }

    private VocabularyTestItemDTO buildForSimpleTranslationTest(WordDTO word) {
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
            .word(word)
            .build();
    }

    private List<VocabularyTestItemDTO> getTestItemsForKanaTest(List<WordDTO> words) {
        return words.stream()
            .map(this::buildForKanaTest)
            .collect(Collectors.toList());
    }

    private VocabularyTestItemDTO buildForKanaTest(WordDTO word) {
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
            .word(word)
            .build();
    }

    private List<VocabularyTestItemDTO> getTestItemsForKanjiTest(List<WordDTO> words) {
        return words.stream()
            .map(this::buildForKanjiTest)
            .collect(Collectors.toList());
    }

    private VocabularyTestItemDTO buildForKanjiTest(WordDTO word) {
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
            .word(word)
            .build();
    }

    private List<VocabularyTestItemDTO> getTestItemsForMixedTest(List<WordDTO> words) {
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
