package pl.edu.pja.nyan.web.rest;

import pl.edu.pja.nyan.NyanApp;

import pl.edu.pja.nyan.domain.ProposedWord;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.domain.User;
import pl.edu.pja.nyan.repository.ProposedWordRepository;
import pl.edu.pja.nyan.service.ProposedWordService;
import pl.edu.pja.nyan.service.dto.ProposedWordDTO;
import pl.edu.pja.nyan.service.mapper.ProposedWordMapper;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
import pl.edu.pja.nyan.service.dto.ProposedWordCriteria;
import pl.edu.pja.nyan.service.ProposedWordQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static pl.edu.pja.nyan.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProposedWordResource REST controller.
 *
 * @see ProposedWordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class ProposedWordResourceIntTest {

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
    private ProposedWordRepository proposedWordRepository;
    @Mock
    private ProposedWordRepository proposedWordRepositoryMock;

    @Autowired
    private ProposedWordMapper proposedWordMapper;
    
    @Mock
    private ProposedWordService proposedWordServiceMock;

    @Autowired
    private ProposedWordService proposedWordService;

    @Autowired
    private ProposedWordQueryService proposedWordQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProposedWordMockMvc;

    private ProposedWord proposedWord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProposedWordResource proposedWordResource = new ProposedWordResource(proposedWordService, proposedWordQueryService);
        this.restProposedWordMockMvc = MockMvcBuilders.standaloneSetup(proposedWordResource)
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
    public static ProposedWord createEntity(EntityManager em) {
        ProposedWord proposedWord = new ProposedWord()
            .translation(DEFAULT_TRANSLATION)
            .kana(DEFAULT_KANA)
            .kanji(DEFAULT_KANJI)
            .romaji(DEFAULT_ROMAJI)
            .note(DEFAULT_NOTE);
        return proposedWord;
    }

    @Before
    public void initTest() {
        proposedWord = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposedWord() throws Exception {
        int databaseSizeBeforeCreate = proposedWordRepository.findAll().size();

        // Create the ProposedWord
        ProposedWordDTO proposedWordDTO = proposedWordMapper.toDto(proposedWord);
        restProposedWordMockMvc.perform(post("/api/proposed-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposedWordDTO)))
            .andExpect(status().isCreated());

        // Validate the ProposedWord in the database
        List<ProposedWord> proposedWordList = proposedWordRepository.findAll();
        assertThat(proposedWordList).hasSize(databaseSizeBeforeCreate + 1);
        ProposedWord testProposedWord = proposedWordList.get(proposedWordList.size() - 1);
        assertThat(testProposedWord.getTranslation()).isEqualTo(DEFAULT_TRANSLATION);
        assertThat(testProposedWord.getKana()).isEqualTo(DEFAULT_KANA);
        assertThat(testProposedWord.getKanji()).isEqualTo(DEFAULT_KANJI);
        assertThat(testProposedWord.getRomaji()).isEqualTo(DEFAULT_ROMAJI);
        assertThat(testProposedWord.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createProposedWordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proposedWordRepository.findAll().size();

        // Create the ProposedWord with an existing ID
        proposedWord.setId(1L);
        ProposedWordDTO proposedWordDTO = proposedWordMapper.toDto(proposedWord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposedWordMockMvc.perform(post("/api/proposed-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposedWordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProposedWord in the database
        List<ProposedWord> proposedWordList = proposedWordRepository.findAll();
        assertThat(proposedWordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTranslationIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposedWordRepository.findAll().size();
        // set the field null
        proposedWord.setTranslation(null);

        // Create the ProposedWord, which fails.
        ProposedWordDTO proposedWordDTO = proposedWordMapper.toDto(proposedWord);

        restProposedWordMockMvc.perform(post("/api/proposed-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposedWordDTO)))
            .andExpect(status().isBadRequest());

        List<ProposedWord> proposedWordList = proposedWordRepository.findAll();
        assertThat(proposedWordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKanaIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposedWordRepository.findAll().size();
        // set the field null
        proposedWord.setKana(null);

        // Create the ProposedWord, which fails.
        ProposedWordDTO proposedWordDTO = proposedWordMapper.toDto(proposedWord);

        restProposedWordMockMvc.perform(post("/api/proposed-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposedWordDTO)))
            .andExpect(status().isBadRequest());

        List<ProposedWord> proposedWordList = proposedWordRepository.findAll();
        assertThat(proposedWordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProposedWords() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList
        restProposedWordMockMvc.perform(get("/api/proposed-words?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposedWord.getId().intValue())))
            .andExpect(jsonPath("$.[*].translation").value(hasItem(DEFAULT_TRANSLATION.toString())))
            .andExpect(jsonPath("$.[*].kana").value(hasItem(DEFAULT_KANA.toString())))
            .andExpect(jsonPath("$.[*].kanji").value(hasItem(DEFAULT_KANJI.toString())))
            .andExpect(jsonPath("$.[*].romaji").value(hasItem(DEFAULT_ROMAJI.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }
    
    public void getAllProposedWordsWithEagerRelationshipsIsEnabled() throws Exception {
        ProposedWordResource proposedWordResource = new ProposedWordResource(proposedWordServiceMock, proposedWordQueryService);
        when(proposedWordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restProposedWordMockMvc = MockMvcBuilders.standaloneSetup(proposedWordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProposedWordMockMvc.perform(get("/api/proposed-words?eagerload=true"))
        .andExpect(status().isOk());

        verify(proposedWordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllProposedWordsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ProposedWordResource proposedWordResource = new ProposedWordResource(proposedWordServiceMock, proposedWordQueryService);
            when(proposedWordServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restProposedWordMockMvc = MockMvcBuilders.standaloneSetup(proposedWordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProposedWordMockMvc.perform(get("/api/proposed-words?eagerload=true"))
        .andExpect(status().isOk());

            verify(proposedWordServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProposedWord() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get the proposedWord
        restProposedWordMockMvc.perform(get("/api/proposed-words/{id}", proposedWord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proposedWord.getId().intValue()))
            .andExpect(jsonPath("$.translation").value(DEFAULT_TRANSLATION.toString()))
            .andExpect(jsonPath("$.kana").value(DEFAULT_KANA.toString()))
            .andExpect(jsonPath("$.kanji").value(DEFAULT_KANJI.toString()))
            .andExpect(jsonPath("$.romaji").value(DEFAULT_ROMAJI.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getAllProposedWordsByTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where translation equals to DEFAULT_TRANSLATION
        defaultProposedWordShouldBeFound("translation.equals=" + DEFAULT_TRANSLATION);

        // Get all the proposedWordList where translation equals to UPDATED_TRANSLATION
        defaultProposedWordShouldNotBeFound("translation.equals=" + UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByTranslationIsInShouldWork() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where translation in DEFAULT_TRANSLATION or UPDATED_TRANSLATION
        defaultProposedWordShouldBeFound("translation.in=" + DEFAULT_TRANSLATION + "," + UPDATED_TRANSLATION);

        // Get all the proposedWordList where translation equals to UPDATED_TRANSLATION
        defaultProposedWordShouldNotBeFound("translation.in=" + UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByTranslationIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where translation is not null
        defaultProposedWordShouldBeFound("translation.specified=true");

        // Get all the proposedWordList where translation is null
        defaultProposedWordShouldNotBeFound("translation.specified=false");
    }

    @Test
    @Transactional
    public void getAllProposedWordsByKanaIsEqualToSomething() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where kana equals to DEFAULT_KANA
        defaultProposedWordShouldBeFound("kana.equals=" + DEFAULT_KANA);

        // Get all the proposedWordList where kana equals to UPDATED_KANA
        defaultProposedWordShouldNotBeFound("kana.equals=" + UPDATED_KANA);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByKanaIsInShouldWork() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where kana in DEFAULT_KANA or UPDATED_KANA
        defaultProposedWordShouldBeFound("kana.in=" + DEFAULT_KANA + "," + UPDATED_KANA);

        // Get all the proposedWordList where kana equals to UPDATED_KANA
        defaultProposedWordShouldNotBeFound("kana.in=" + UPDATED_KANA);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByKanaIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where kana is not null
        defaultProposedWordShouldBeFound("kana.specified=true");

        // Get all the proposedWordList where kana is null
        defaultProposedWordShouldNotBeFound("kana.specified=false");
    }

    @Test
    @Transactional
    public void getAllProposedWordsByKanjiIsEqualToSomething() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where kanji equals to DEFAULT_KANJI
        defaultProposedWordShouldBeFound("kanji.equals=" + DEFAULT_KANJI);

        // Get all the proposedWordList where kanji equals to UPDATED_KANJI
        defaultProposedWordShouldNotBeFound("kanji.equals=" + UPDATED_KANJI);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByKanjiIsInShouldWork() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where kanji in DEFAULT_KANJI or UPDATED_KANJI
        defaultProposedWordShouldBeFound("kanji.in=" + DEFAULT_KANJI + "," + UPDATED_KANJI);

        // Get all the proposedWordList where kanji equals to UPDATED_KANJI
        defaultProposedWordShouldNotBeFound("kanji.in=" + UPDATED_KANJI);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByKanjiIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where kanji is not null
        defaultProposedWordShouldBeFound("kanji.specified=true");

        // Get all the proposedWordList where kanji is null
        defaultProposedWordShouldNotBeFound("kanji.specified=false");
    }

    @Test
    @Transactional
    public void getAllProposedWordsByRomajiIsEqualToSomething() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where romaji equals to DEFAULT_ROMAJI
        defaultProposedWordShouldBeFound("romaji.equals=" + DEFAULT_ROMAJI);

        // Get all the proposedWordList where romaji equals to UPDATED_ROMAJI
        defaultProposedWordShouldNotBeFound("romaji.equals=" + UPDATED_ROMAJI);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByRomajiIsInShouldWork() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where romaji in DEFAULT_ROMAJI or UPDATED_ROMAJI
        defaultProposedWordShouldBeFound("romaji.in=" + DEFAULT_ROMAJI + "," + UPDATED_ROMAJI);

        // Get all the proposedWordList where romaji equals to UPDATED_ROMAJI
        defaultProposedWordShouldNotBeFound("romaji.in=" + UPDATED_ROMAJI);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByRomajiIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where romaji is not null
        defaultProposedWordShouldBeFound("romaji.specified=true");

        // Get all the proposedWordList where romaji is null
        defaultProposedWordShouldNotBeFound("romaji.specified=false");
    }

    @Test
    @Transactional
    public void getAllProposedWordsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where note equals to DEFAULT_NOTE
        defaultProposedWordShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the proposedWordList where note equals to UPDATED_NOTE
        defaultProposedWordShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultProposedWordShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the proposedWordList where note equals to UPDATED_NOTE
        defaultProposedWordShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllProposedWordsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        // Get all the proposedWordList where note is not null
        defaultProposedWordShouldBeFound("note.specified=true");

        // Get all the proposedWordList where note is null
        defaultProposedWordShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    public void getAllProposedWordsByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        Tag tag = TagResourceIntTest.createEntity(em);
        em.persist(tag);
        em.flush();
        proposedWord.addTag(tag);
        proposedWordRepository.saveAndFlush(proposedWord);
        Long tagId = tag.getId();

        // Get all the proposedWordList where tag equals to tagId
        defaultProposedWordShouldBeFound("tagId.equals=" + tagId);

        // Get all the proposedWordList where tag equals to tagId + 1
        defaultProposedWordShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }


    @Test
    @Transactional
    public void getAllProposedWordsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        User addedBy = UserResourceIntTest.createEntity(em);
        em.persist(addedBy);
        em.flush();
        proposedWord.setAddedBy(addedBy);
        proposedWordRepository.saveAndFlush(proposedWord);
        Long addedById = addedBy.getId();

        // Get all the proposedWordList where addedBy equals to addedById
        defaultProposedWordShouldBeFound("addedById.equals=" + addedById);

        // Get all the proposedWordList where addedBy equals to addedById + 1
        defaultProposedWordShouldNotBeFound("addedById.equals=" + (addedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProposedWordShouldBeFound(String filter) throws Exception {
        restProposedWordMockMvc.perform(get("/api/proposed-words?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposedWord.getId().intValue())))
            .andExpect(jsonPath("$.[*].translation").value(hasItem(DEFAULT_TRANSLATION.toString())))
            .andExpect(jsonPath("$.[*].kana").value(hasItem(DEFAULT_KANA.toString())))
            .andExpect(jsonPath("$.[*].kanji").value(hasItem(DEFAULT_KANJI.toString())))
            .andExpect(jsonPath("$.[*].romaji").value(hasItem(DEFAULT_ROMAJI.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProposedWordShouldNotBeFound(String filter) throws Exception {
        restProposedWordMockMvc.perform(get("/api/proposed-words?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingProposedWord() throws Exception {
        // Get the proposedWord
        restProposedWordMockMvc.perform(get("/api/proposed-words/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposedWord() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        int databaseSizeBeforeUpdate = proposedWordRepository.findAll().size();

        // Update the proposedWord
        ProposedWord updatedProposedWord = proposedWordRepository.findById(proposedWord.getId()).get();
        // Disconnect from session so that the updates on updatedProposedWord are not directly saved in db
        em.detach(updatedProposedWord);
        updatedProposedWord
            .translation(UPDATED_TRANSLATION)
            .kana(UPDATED_KANA)
            .kanji(UPDATED_KANJI)
            .romaji(UPDATED_ROMAJI)
            .note(UPDATED_NOTE);
        ProposedWordDTO proposedWordDTO = proposedWordMapper.toDto(updatedProposedWord);

        restProposedWordMockMvc.perform(put("/api/proposed-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposedWordDTO)))
            .andExpect(status().isOk());

        // Validate the ProposedWord in the database
        List<ProposedWord> proposedWordList = proposedWordRepository.findAll();
        assertThat(proposedWordList).hasSize(databaseSizeBeforeUpdate);
        ProposedWord testProposedWord = proposedWordList.get(proposedWordList.size() - 1);
        assertThat(testProposedWord.getTranslation()).isEqualTo(UPDATED_TRANSLATION);
        assertThat(testProposedWord.getKana()).isEqualTo(UPDATED_KANA);
        assertThat(testProposedWord.getKanji()).isEqualTo(UPDATED_KANJI);
        assertThat(testProposedWord.getRomaji()).isEqualTo(UPDATED_ROMAJI);
        assertThat(testProposedWord.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingProposedWord() throws Exception {
        int databaseSizeBeforeUpdate = proposedWordRepository.findAll().size();

        // Create the ProposedWord
        ProposedWordDTO proposedWordDTO = proposedWordMapper.toDto(proposedWord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restProposedWordMockMvc.perform(put("/api/proposed-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposedWordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProposedWord in the database
        List<ProposedWord> proposedWordList = proposedWordRepository.findAll();
        assertThat(proposedWordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProposedWord() throws Exception {
        // Initialize the database
        proposedWordRepository.saveAndFlush(proposedWord);

        int databaseSizeBeforeDelete = proposedWordRepository.findAll().size();

        // Get the proposedWord
        restProposedWordMockMvc.perform(delete("/api/proposed-words/{id}", proposedWord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProposedWord> proposedWordList = proposedWordRepository.findAll();
        assertThat(proposedWordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProposedWord.class);
        ProposedWord proposedWord1 = new ProposedWord();
        proposedWord1.setId(1L);
        ProposedWord proposedWord2 = new ProposedWord();
        proposedWord2.setId(proposedWord1.getId());
        assertThat(proposedWord1).isEqualTo(proposedWord2);
        proposedWord2.setId(2L);
        assertThat(proposedWord1).isNotEqualTo(proposedWord2);
        proposedWord1.setId(null);
        assertThat(proposedWord1).isNotEqualTo(proposedWord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProposedWordDTO.class);
        ProposedWordDTO proposedWordDTO1 = new ProposedWordDTO();
        proposedWordDTO1.setId(1L);
        ProposedWordDTO proposedWordDTO2 = new ProposedWordDTO();
        assertThat(proposedWordDTO1).isNotEqualTo(proposedWordDTO2);
        proposedWordDTO2.setId(proposedWordDTO1.getId());
        assertThat(proposedWordDTO1).isEqualTo(proposedWordDTO2);
        proposedWordDTO2.setId(2L);
        assertThat(proposedWordDTO1).isNotEqualTo(proposedWordDTO2);
        proposedWordDTO1.setId(null);
        assertThat(proposedWordDTO1).isNotEqualTo(proposedWordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(proposedWordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(proposedWordMapper.fromId(null)).isNull();
    }
}
