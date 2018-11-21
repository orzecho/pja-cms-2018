package pl.edu.pja.nyan.web.rest;

import static org.assertj.core.api.Assertions.not;
import pl.edu.pja.nyan.NyanApp;

import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.repository.WordRepository;
import pl.edu.pja.nyan.service.WordService;
import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.service.mapper.WordMapper;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
import pl.edu.pja.nyan.service.WordQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;


import static pl.edu.pja.nyan.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WordResource REST controller.
 *
 * @see WordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class WordResourceIntTest {

    private static final String DEFAULT_TRANSLATION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSLATION = "BBBBBBBBBB";

    private static final String DEFAULT_KANA = "AAAAAAAAAA";
    private static final String UPDATED_KANA = "BBBBBBBBBB";

    private static final String DEFAULT_KANJI = "AAAAAAAAAA";
    private static final String UPDATED_KANJI = "BBBBBBBBBB";

    private static final String DEFAULT_ROMAJI = "AAAAAAAAAA";
    private static final String UPDATED_ROMAJI = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private WordRepository wordRepository;
    @Mock
    private WordRepository wordRepositoryMock;

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private WordService wordService;

    @Autowired
    private WordQueryService wordQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWordMockMvc;

    private Word word;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WordResource wordResource = new WordResource(wordService, wordQueryService);
        this.restWordMockMvc = MockMvcBuilders.standaloneSetup(wordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Word createEntity(EntityManager em) {
        Word word = new Word()
            .translation(DEFAULT_TRANSLATION)
            .kana(DEFAULT_KANA)
            .kanji(DEFAULT_KANJI)
            .romaji(DEFAULT_ROMAJI)
            .note(DEFAULT_NOTE);
        return word;
    }

    @Before
    public void initTest() {
        word = createEntity(em);
    }

    @Test
    @Transactional
    public void createWord() throws Exception {
        int databaseSizeBeforeCreate = wordRepository.findAll().size();

        // Create the Word
        WordDTO wordDTO = wordMapper.toDto(word);
        restWordMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isCreated());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeCreate + 1);
        Word testWord = wordList.stream().filter(e -> e.getTranslation().equals(DEFAULT_TRANSLATION)).findAny().get();
        assertThat(testWord.getKana()).isEqualTo(DEFAULT_KANA);
        assertThat(testWord.getKanji()).isEqualTo(DEFAULT_KANJI);
        assertThat(testWord.getRomaji()).isEqualTo(DEFAULT_ROMAJI);
        assertThat(testWord.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createWordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wordRepository.findAll().size();

        // Create the Word with an existing ID
        word.setId(1L);
        WordDTO wordDTO = wordMapper.toDto(word);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTranslationIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordRepository.findAll().size();
        // set the field null
        word.setTranslation(null);

        // Create the Word, which fails.
        WordDTO wordDTO = wordMapper.toDto(word);

        restWordMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isBadRequest());

        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKanaIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordRepository.findAll().size();
        // set the field null
        word.setKana(null);

        // Create the Word, which fails.
        WordDTO wordDTO = wordMapper.toDto(word);

        restWordMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isBadRequest());

        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWords() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList
        restWordMockMvc.perform(get("/api/words?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(word.getId().intValue())))
            .andExpect(jsonPath("$.[*].translation").value(hasItem(DEFAULT_TRANSLATION.toString())))
            .andExpect(jsonPath("$.[*].kana").value(hasItem(DEFAULT_KANA.toString())))
            .andExpect(jsonPath("$.[*].kanji").value(hasItem(DEFAULT_KANJI.toString())))
            .andExpect(jsonPath("$.[*].romaji").value(hasItem(DEFAULT_ROMAJI.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    public void getWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get the word
        restWordMockMvc.perform(get("/api/words/{id}", word.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(word.getId().intValue()))
            .andExpect(jsonPath("$.translation").value(DEFAULT_TRANSLATION.toString()))
            .andExpect(jsonPath("$.kana").value(DEFAULT_KANA.toString()))
            .andExpect(jsonPath("$.kanji").value(DEFAULT_KANJI.toString()))
            .andExpect(jsonPath("$.romaji").value(DEFAULT_ROMAJI.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getAllWordsByTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where translation equals to DEFAULT_TRANSLATION
        defaultWordShouldBeFound("translation.equals=" + DEFAULT_TRANSLATION);

        // Get all the wordList where translation equals to UPDATED_TRANSLATION
        defaultWordShouldNotBeFound("translation.equals=" + UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    public void getAllWordsByTranslationIsInShouldWork() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where translation in DEFAULT_TRANSLATION or UPDATED_TRANSLATION
        defaultWordShouldBeFound("translation.in=" + DEFAULT_TRANSLATION + "," + UPDATED_TRANSLATION);

        // Get all the wordList where translation equals to UPDATED_TRANSLATION
        defaultWordShouldNotBeFound("translation.in=" + UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    public void getAllWordsByTranslationIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where translation is not null
        defaultWordShouldBeFound("translation.specified=true");

        // Get all the wordList where translation is null
        defaultWordShouldNotBeFound("translation.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsByKanaIsEqualToSomething() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where kana equals to DEFAULT_KANA
        defaultWordShouldBeFound("kana.equals=" + DEFAULT_KANA);

        // Get all the wordList where kana equals to UPDATED_KANA
        defaultWordShouldNotBeFound("kana.equals=" + UPDATED_KANA);
    }

    @Test
    @Transactional
    public void getAllWordsByKanaIsInShouldWork() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where kana in DEFAULT_KANA or UPDATED_KANA
        defaultWordShouldBeFound("kana.in=" + DEFAULT_KANA + "," + UPDATED_KANA);

        // Get all the wordList where kana equals to UPDATED_KANA
        defaultWordShouldNotBeFound("kana.in=" + UPDATED_KANA);
    }

    @Test
    @Transactional
    public void getAllWordsByKanaIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where kana is not null
        defaultWordShouldBeFound("kana.specified=true");

        // Get all the wordList where kana is null
        defaultWordShouldNotBeFound("kana.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsByKanjiIsEqualToSomething() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where kanji equals to DEFAULT_KANJI
        defaultWordShouldBeFound("kanji.equals=" + DEFAULT_KANJI);

        // Get all the wordList where kanji equals to UPDATED_KANJI
        defaultWordShouldNotBeFound("kanji.equals=" + UPDATED_KANJI);
    }

    @Test
    @Transactional
    public void getAllWordsByKanjiIsInShouldWork() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where kanji in DEFAULT_KANJI or UPDATED_KANJI
        defaultWordShouldBeFound("kanji.in=" + DEFAULT_KANJI + "," + UPDATED_KANJI);

        // Get all the wordList where kanji equals to UPDATED_KANJI
        defaultWordShouldNotBeFound("kanji.in=" + UPDATED_KANJI);
    }

    @Test
    @Transactional
    public void getAllWordsByKanjiIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where kanji is not null
        defaultWordShouldBeFound("kanji.specified=true");

        // Get all the wordList where kanji is null
        defaultWordShouldNotBeFound("kanji.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsByRomajiIsEqualToSomething() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where romaji equals to DEFAULT_ROMAJI
        defaultWordShouldBeFound("romaji.equals=" + DEFAULT_ROMAJI);

        // Get all the wordList where romaji equals to UPDATED_ROMAJI
        defaultWordShouldNotBeFound("romaji.equals=" + UPDATED_ROMAJI);
    }

    @Test
    @Transactional
    public void getAllWordsByRomajiIsInShouldWork() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where romaji in DEFAULT_ROMAJI or UPDATED_ROMAJI
        defaultWordShouldBeFound("romaji.in=" + DEFAULT_ROMAJI + "," + UPDATED_ROMAJI);

        // Get all the wordList where romaji equals to UPDATED_ROMAJI
        defaultWordShouldNotBeFound("romaji.in=" + UPDATED_ROMAJI);
    }

    @Test
    @Transactional
    public void getAllWordsByRomajiIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where romaji is not null
        defaultWordShouldBeFound("romaji.specified=true");
    }

    @Test
    @Transactional
    public void getAllWordsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where note equals to DEFAULT_NOTE
        defaultWordShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the wordList where note equals to UPDATED_NOTE
        defaultWordShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllWordsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultWordShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the wordList where note equals to UPDATED_NOTE
        defaultWordShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllWordsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList where note is not null
        defaultWordShouldBeFound("note.specified=true");
    }

    @Test
    @Transactional
    public void getAllWordsByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        Tag tag = TagResourceIntTest.createEntity(em);
        em.persist(tag);
        em.flush();
        word.addTag(tag);
        wordRepository.saveAndFlush(word);
        Long tagId = tag.getId();

        // Get all the wordList where tag equals to tagId
        defaultWordShouldBeFound("tagId.equals=" + tagId);

        // Get all the wordList where tag equals to tagId + 1
        defaultWordShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWordShouldBeFound(String filter) throws Exception {
        restWordMockMvc.perform(get("/api/words?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(word.getId().intValue())))
            .andExpect(jsonPath("$.[*].translation").value(hasItem(DEFAULT_TRANSLATION.toString())))
            .andExpect(jsonPath("$.[*].kana").value(hasItem(DEFAULT_KANA.toString())))
            .andExpect(jsonPath("$.[*].kanji").value(hasItem(DEFAULT_KANJI.toString())))
            .andExpect(jsonPath("$.[*].romaji").value(hasItem(DEFAULT_ROMAJI.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWordShouldNotBeFound(String filter) throws Exception {
        restWordMockMvc.perform(get("/api/words?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingWord() throws Exception {
        // Get the word
        restWordMockMvc.perform(get("/api/words/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word
        Word updatedWord = wordRepository.findById(word.getId()).get();
        // Disconnect from session so that the updates on updatedWord are not directly saved in db
        em.detach(updatedWord);
        updatedWord
            .translation(UPDATED_TRANSLATION)
            .kana(UPDATED_KANA)
            .kanji(UPDATED_KANJI)
            .romaji(UPDATED_ROMAJI)
            .note(UPDATED_NOTE);
        WordDTO wordDTO = wordMapper.toDto(updatedWord);

        restWordMockMvc.perform(put("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
        Word testWord = wordList.stream().filter(e -> e.getTranslation().equals(UPDATED_TRANSLATION)).findAny().get();
        assertThat(testWord.getTranslation()).isEqualTo(UPDATED_TRANSLATION);
        assertThat(testWord.getKana()).isEqualTo(UPDATED_KANA);
        assertThat(testWord.getKanji()).isEqualTo(UPDATED_KANJI);
        assertThat(testWord.getRomaji()).isEqualTo(UPDATED_ROMAJI);
        assertThat(testWord.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Create the Word
        WordDTO wordDTO = wordMapper.toDto(word);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordMockMvc.perform(put("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeDelete = wordRepository.findAll().size();

        // Get the word
        restWordMockMvc.perform(delete("/api/words/{id}", word.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Word.class);
        Word word1 = new Word();
        word1.setId(1L);
        Word word2 = new Word();
        word2.setId(word1.getId());
        assertThat(word1).isEqualTo(word2);
        word2.setId(2L);
        assertThat(word1).isNotEqualTo(word2);
        word1.setId(null);
        assertThat(word1).isNotEqualTo(word2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordDTO.class);
        WordDTO wordDTO1 = new WordDTO();
        wordDTO1.setId(1L);
        WordDTO wordDTO2 = new WordDTO();
        assertThat(wordDTO1).isNotEqualTo(wordDTO2);
        wordDTO2.setId(wordDTO1.getId());
        assertThat(wordDTO1).isEqualTo(wordDTO2);
        wordDTO2.setId(2L);
        assertThat(wordDTO1).isNotEqualTo(wordDTO2);
        wordDTO1.setId(null);
        assertThat(wordDTO1).isNotEqualTo(wordDTO2);
    }
}
