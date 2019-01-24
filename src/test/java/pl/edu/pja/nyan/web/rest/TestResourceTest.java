package pl.edu.pja.nyan.web.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import pl.edu.pja.nyan.NyanApp;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.repository.TagRepository;
import pl.edu.pja.nyan.repository.WordRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
@AutoConfigureMockMvc
public class TestResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private WordRepository wordRepository;

    @Test
    public void should_generate_translation_test() throws Exception {
        //given
        Word word = new Word();
        word.setTranslation("asdf");
        word.setKana("qwer");
        word.setKanji("zxcv");
        word = wordRepository.save(word);
        Tag tag = tagRepository.save(new Tag("tag1", word));
        wordRepository.save(word);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/test/vocabulary/WRITTEN_PL/" + tag.getName())
            .with(user("user").roles("USER")));

        //then
        resultActions.andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].translationFromSystem").value("asdf"))
            .andExpect(jsonPath("$[0].translationFromUser").value("asdf"))
            .andExpect(jsonPath("$[0].kanaFromUser").value(""))
            .andExpect(jsonPath("$[0].kanaFromSystem").value("qwer"))
            .andExpect(jsonPath("$[0].kanjiFromUser").value(""))
            .andExpect(jsonPath("$[0].kanjiFromSystem").value("zxcv"))
            .andExpect(jsonPath("$[0].kanaCorrect").value(false))
            .andExpect(jsonPath("$[0].kanjiCorrect").value(false))
            .andExpect(jsonPath("$[0].translationCorrect").value(true))
            .andDo(print());
    }

    @Test
    public void should_generate_kana_test() throws Exception {
        //given
        Word word = new Word();
        word.setTranslation("asdf");
        word.setKana("qwer");
        word.setKanji("zxcv");
        word = wordRepository.save(word);
        Tag tag = tagRepository.save(new Tag("tag2", word));
        wordRepository.save(word);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/test/vocabulary/WRITTEN_KANA/" + tag.getName())
            .with(user("user").roles("USER")));

        //then
        resultActions.andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].translationFromSystem").value("asdf"))
            .andExpect(jsonPath("$[0].translationFromUser").value(""))
            .andExpect(jsonPath("$[0].kanaFromUser").value("qwer"))
            .andExpect(jsonPath("$[0].kanaFromSystem").value("qwer"))
            .andExpect(jsonPath("$[0].kanjiFromUser").value(""))
            .andExpect(jsonPath("$[0].kanjiFromSystem").value("zxcv"))
            .andExpect(jsonPath("$[0].kanaCorrect").value(true))
            .andExpect(jsonPath("$[0].kanjiCorrect").value(false))
            .andExpect(jsonPath("$[0].translationCorrect").value(false))
            .andDo(print());
    }

    @Test
    public void should_generate_kanji_test() throws Exception {
        //given
        Word word = new Word();
        word.setTranslation("asdf");
        word.setKana("qwer");
        word.setKanji("zxcv");
        word = wordRepository.save(word);
        Tag tag = tagRepository.save(new Tag("tag3", word));
        wordRepository.save(word);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/test/vocabulary/WRITTEN_KANJI/" + tag.getName())
            .with(user("user").roles("USER")));

        //then
        resultActions.andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].translationFromSystem").value("asdf"))
            .andExpect(jsonPath("$[0].translationFromUser").value(""))
            .andExpect(jsonPath("$[0].kanaFromUser").value(""))
            .andExpect(jsonPath("$[0].kanaFromSystem").value("qwer"))
            .andExpect(jsonPath("$[0].kanjiFromUser").value("zxcv"))
            .andExpect(jsonPath("$[0].kanjiFromSystem").value("zxcv"))
            .andExpect(jsonPath("$[0].kanaCorrect").value(false))
            .andExpect(jsonPath("$[0].kanjiCorrect").value(true))
            .andExpect(jsonPath("$[0].translationCorrect").value(false))
            .andDo(print());
    }
}
