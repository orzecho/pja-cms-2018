package pl.edu.pja.nyan.web.rest;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import pl.edu.pja.nyan.NyanApp;
import pl.edu.pja.nyan.domain.ExamResult;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.domain.WrittenAnswer;
import pl.edu.pja.nyan.repository.WrittenAnswerRepository;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.WrittenAnswerQueryService;
import pl.edu.pja.nyan.service.WrittenAnswerService;
import pl.edu.pja.nyan.service.dto.WrittenAnswerDTO;
import pl.edu.pja.nyan.service.mapper.WrittenAnswerMapper;
import static pl.edu.pja.nyan.web.rest.TestUtil.createFormattingConversionService;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
import pl.edu.pja.nyan.web.rest.errors.UserNotLoggedInException;

/**
 * Test class for the WrittenAnswerResource REST controller.
 *
 * @see WrittenAnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class WrittenAnswerResourceIntTest {

    private static final String DEFAULT_TRANSLATION_FROM = "AAAAAAAAAA";
    private static final String UPDATED_TRANSLATION_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSLATION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSLATION = "BBBBBBBBBB";

    private static final String DEFAULT_KANA = "AAAAAAAAAA";
    private static final String UPDATED_KANA = "BBBBBBBBBB";

    private static final String DEFAULT_KANJI = "AAAAAAAAAA";
    private static final String UPDATED_KANJI = "BBBBBBBBBB";

    private static final String DEFAULT_ROMAJI = "AAAAAAAAAA";
    private static final String UPDATED_ROMAJI = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_RIGHT_ANSWER = false;
    private static final Boolean UPDATED_IS_RIGHT_ANSWER = true;

    @Autowired
    private WrittenAnswerRepository writtenAnswerRepository;

    @Autowired
    private WrittenAnswerMapper writtenAnswerMapper;

    @Autowired
    private WrittenAnswerService writtenAnswerService;

    @Autowired
    private WrittenAnswerQueryService writtenAnswerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager em;

    private MockMvc restWrittenAnswerMockMvc;

    private WrittenAnswer writtenAnswer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WrittenAnswerResource writtenAnswerResource = new WrittenAnswerResource(writtenAnswerService, writtenAnswerQueryService);
        this.restWrittenAnswerMockMvc = MockMvcBuilders.standaloneSetup(writtenAnswerResource)
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
    public static WrittenAnswer createEntity(EntityManager em) {
        WrittenAnswer writtenAnswer = new WrittenAnswer()
            .translationFrom(DEFAULT_TRANSLATION_FROM)
            .translation(DEFAULT_TRANSLATION)
            .kana(DEFAULT_KANA)
            .kanji(DEFAULT_KANJI)
            .romaji(DEFAULT_ROMAJI)
            .isRightAnswer(DEFAULT_IS_RIGHT_ANSWER);
        return writtenAnswer;
    }

    @Before
    public void initTest() {
        writtenAnswer = createEntity(em);
    }

    @Test
    @Transactional
    public void createWrittenAnswer() throws Exception {
        int databaseSizeBeforeCreate = writtenAnswerRepository.findAll().size();

        // Create the WrittenAnswer
        WrittenAnswerDTO writtenAnswerDTO = writtenAnswerMapper.toDto(writtenAnswer);
        restWrittenAnswerMockMvc.perform(post("/api/written-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writtenAnswerDTO)))
            .andExpect(status().isCreated());

        // Validate the WrittenAnswer in the database
        List<WrittenAnswer> writtenAnswerList = writtenAnswerRepository.findAll();
        assertThat(writtenAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        WrittenAnswer testWrittenAnswer = writtenAnswerList.get(writtenAnswerList.size() - 1);
        assertThat(testWrittenAnswer.getTranslationFrom()).isEqualTo(DEFAULT_TRANSLATION_FROM);
        assertThat(testWrittenAnswer.getTranslation()).isEqualTo(DEFAULT_TRANSLATION);
        assertThat(testWrittenAnswer.getKana()).isEqualTo(DEFAULT_KANA);
        assertThat(testWrittenAnswer.getKanji()).isEqualTo(DEFAULT_KANJI);
        assertThat(testWrittenAnswer.getRomaji()).isEqualTo(DEFAULT_ROMAJI);
        assertThat(testWrittenAnswer.isRightAnswer()).isEqualTo(DEFAULT_IS_RIGHT_ANSWER);
    }

    @Test
    @Transactional
    public void createWrittenAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = writtenAnswerRepository.findAll().size();

        // Create the WrittenAnswer with an existing ID
        writtenAnswer.setId(1L);
        WrittenAnswerDTO writtenAnswerDTO = writtenAnswerMapper.toDto(writtenAnswer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWrittenAnswerMockMvc.perform(post("/api/written-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writtenAnswerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WrittenAnswer in the database
        List<WrittenAnswer> writtenAnswerList = writtenAnswerRepository.findAll();
        assertThat(writtenAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswers() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList
        restWrittenAnswerMockMvc.perform(get("/api/written-answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(writtenAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].translationFrom").value(hasItem(DEFAULT_TRANSLATION_FROM.toString())))
            .andExpect(jsonPath("$.[*].translation").value(hasItem(DEFAULT_TRANSLATION.toString())))
            .andExpect(jsonPath("$.[*].kana").value(hasItem(DEFAULT_KANA.toString())))
            .andExpect(jsonPath("$.[*].kanji").value(hasItem(DEFAULT_KANJI.toString())))
            .andExpect(jsonPath("$.[*].romaji").value(hasItem(DEFAULT_ROMAJI.toString())))
            .andExpect(jsonPath("$.[*].isRightAnswer").value(hasItem(DEFAULT_IS_RIGHT_ANSWER.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getWrittenAnswer() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get the writtenAnswer
        restWrittenAnswerMockMvc.perform(get("/api/written-answers/{id}", writtenAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(writtenAnswer.getId().intValue()))
            .andExpect(jsonPath("$.translationFrom").value(DEFAULT_TRANSLATION_FROM.toString()))
            .andExpect(jsonPath("$.translation").value(DEFAULT_TRANSLATION.toString()))
            .andExpect(jsonPath("$.kana").value(DEFAULT_KANA.toString()))
            .andExpect(jsonPath("$.kanji").value(DEFAULT_KANJI.toString()))
            .andExpect(jsonPath("$.romaji").value(DEFAULT_ROMAJI.toString()))
            .andExpect(jsonPath("$.isRightAnswer").value(DEFAULT_IS_RIGHT_ANSWER.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByTranslationFromIsEqualToSomething() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where translationFrom equals to DEFAULT_TRANSLATION_FROM
        defaultWrittenAnswerShouldBeFound("translationFrom.equals=" + DEFAULT_TRANSLATION_FROM);

        // Get all the writtenAnswerList where translationFrom equals to UPDATED_TRANSLATION_FROM
        defaultWrittenAnswerShouldNotBeFound("translationFrom.equals=" + UPDATED_TRANSLATION_FROM);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByTranslationFromIsInShouldWork() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where translationFrom in DEFAULT_TRANSLATION_FROM or UPDATED_TRANSLATION_FROM
        defaultWrittenAnswerShouldBeFound("translationFrom.in=" + DEFAULT_TRANSLATION_FROM + "," + UPDATED_TRANSLATION_FROM);

        // Get all the writtenAnswerList where translationFrom equals to UPDATED_TRANSLATION_FROM
        defaultWrittenAnswerShouldNotBeFound("translationFrom.in=" + UPDATED_TRANSLATION_FROM);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByTranslationFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where translationFrom is not null
        defaultWrittenAnswerShouldBeFound("translationFrom.specified=true");

        // Get all the writtenAnswerList where translationFrom is null
        defaultWrittenAnswerShouldNotBeFound("translationFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where translation equals to DEFAULT_TRANSLATION
        defaultWrittenAnswerShouldBeFound("translation.equals=" + DEFAULT_TRANSLATION);

        // Get all the writtenAnswerList where translation equals to UPDATED_TRANSLATION
        defaultWrittenAnswerShouldNotBeFound("translation.equals=" + UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByTranslationIsInShouldWork() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where translation in DEFAULT_TRANSLATION or UPDATED_TRANSLATION
        defaultWrittenAnswerShouldBeFound("translation.in=" + DEFAULT_TRANSLATION + "," + UPDATED_TRANSLATION);

        // Get all the writtenAnswerList where translation equals to UPDATED_TRANSLATION
        defaultWrittenAnswerShouldNotBeFound("translation.in=" + UPDATED_TRANSLATION);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByTranslationIsNullOrNotNull() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where translation is not null
        defaultWrittenAnswerShouldBeFound("translation.specified=true");

        // Get all the writtenAnswerList where translation is null
        defaultWrittenAnswerShouldNotBeFound("translation.specified=false");
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByKanaIsEqualToSomething() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where kana equals to DEFAULT_KANA
        defaultWrittenAnswerShouldBeFound("kana.equals=" + DEFAULT_KANA);

        // Get all the writtenAnswerList where kana equals to UPDATED_KANA
        defaultWrittenAnswerShouldNotBeFound("kana.equals=" + UPDATED_KANA);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByKanaIsInShouldWork() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where kana in DEFAULT_KANA or UPDATED_KANA
        defaultWrittenAnswerShouldBeFound("kana.in=" + DEFAULT_KANA + "," + UPDATED_KANA);

        // Get all the writtenAnswerList where kana equals to UPDATED_KANA
        defaultWrittenAnswerShouldNotBeFound("kana.in=" + UPDATED_KANA);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByKanaIsNullOrNotNull() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where kana is not null
        defaultWrittenAnswerShouldBeFound("kana.specified=true");

        // Get all the writtenAnswerList where kana is null
        defaultWrittenAnswerShouldNotBeFound("kana.specified=false");
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByKanjiIsEqualToSomething() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where kanji equals to DEFAULT_KANJI
        defaultWrittenAnswerShouldBeFound("kanji.equals=" + DEFAULT_KANJI);

        // Get all the writtenAnswerList where kanji equals to UPDATED_KANJI
        defaultWrittenAnswerShouldNotBeFound("kanji.equals=" + UPDATED_KANJI);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByKanjiIsInShouldWork() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where kanji in DEFAULT_KANJI or UPDATED_KANJI
        defaultWrittenAnswerShouldBeFound("kanji.in=" + DEFAULT_KANJI + "," + UPDATED_KANJI);

        // Get all the writtenAnswerList where kanji equals to UPDATED_KANJI
        defaultWrittenAnswerShouldNotBeFound("kanji.in=" + UPDATED_KANJI);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByKanjiIsNullOrNotNull() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where kanji is not null
        defaultWrittenAnswerShouldBeFound("kanji.specified=true");

        // Get all the writtenAnswerList where kanji is null
        defaultWrittenAnswerShouldNotBeFound("kanji.specified=false");
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByRomajiIsEqualToSomething() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where romaji equals to DEFAULT_ROMAJI
        defaultWrittenAnswerShouldBeFound("romaji.equals=" + DEFAULT_ROMAJI);

        // Get all the writtenAnswerList where romaji equals to UPDATED_ROMAJI
        defaultWrittenAnswerShouldNotBeFound("romaji.equals=" + UPDATED_ROMAJI);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByRomajiIsInShouldWork() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where romaji in DEFAULT_ROMAJI or UPDATED_ROMAJI
        defaultWrittenAnswerShouldBeFound("romaji.in=" + DEFAULT_ROMAJI + "," + UPDATED_ROMAJI);

        // Get all the writtenAnswerList where romaji equals to UPDATED_ROMAJI
        defaultWrittenAnswerShouldNotBeFound("romaji.in=" + UPDATED_ROMAJI);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByRomajiIsNullOrNotNull() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where romaji is not null
        defaultWrittenAnswerShouldBeFound("romaji.specified=true");

        // Get all the writtenAnswerList where romaji is null
        defaultWrittenAnswerShouldNotBeFound("romaji.specified=false");
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByIsRightAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where isRightAnswer equals to DEFAULT_IS_RIGHT_ANSWER
        defaultWrittenAnswerShouldBeFound("isRightAnswer.equals=" + DEFAULT_IS_RIGHT_ANSWER);

        // Get all the writtenAnswerList where isRightAnswer equals to UPDATED_IS_RIGHT_ANSWER
        defaultWrittenAnswerShouldNotBeFound("isRightAnswer.equals=" + UPDATED_IS_RIGHT_ANSWER);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByIsRightAnswerIsInShouldWork() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where isRightAnswer in DEFAULT_IS_RIGHT_ANSWER or UPDATED_IS_RIGHT_ANSWER
        defaultWrittenAnswerShouldBeFound("isRightAnswer.in=" + DEFAULT_IS_RIGHT_ANSWER + "," + UPDATED_IS_RIGHT_ANSWER);

        // Get all the writtenAnswerList where isRightAnswer equals to UPDATED_IS_RIGHT_ANSWER
        defaultWrittenAnswerShouldNotBeFound("isRightAnswer.in=" + UPDATED_IS_RIGHT_ANSWER);
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByIsRightAnswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        // Get all the writtenAnswerList where isRightAnswer is not null
        defaultWrittenAnswerShouldBeFound("isRightAnswer.specified=true");

        // Get all the writtenAnswerList where isRightAnswer is null
        defaultWrittenAnswerShouldNotBeFound("isRightAnswer.specified=false");
    }

    @Test
    @Transactional
    public void getAllWrittenAnswersByAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        Word word = WordResourceIntTest.createEntity(em);
        em.persist(word);
        em.flush();
        writtenAnswer.setWord(word);
        writtenAnswerRepository.saveAndFlush(writtenAnswer);
        Long answerId = word.getId();

        // Get all the writtenAnswerList where answer equals to answerId
        defaultWrittenAnswerShouldBeFound("answerId.equals=" + answerId);

        // Get all the writtenAnswerList where answer equals to answerId + 1
        defaultWrittenAnswerShouldNotBeFound("answerId.equals=" + (answerId + 1));
    }


    @Test
    @Transactional
    public void getAllWrittenAnswersByExamIsEqualToSomething() throws Exception {
        // Initialize the database
        ExamResult exam = ExamResultResourceIntTest.createEntity(em);
        em.persist(exam);
        em.flush();
        writtenAnswer.setExam(exam);
        writtenAnswerRepository.saveAndFlush(writtenAnswer);
        Long examId = exam.getId();

        // Get all the writtenAnswerList where exam equals to examId
        defaultWrittenAnswerShouldBeFound("examId.equals=" + examId);

        // Get all the writtenAnswerList where exam equals to examId + 1
        defaultWrittenAnswerShouldNotBeFound("examId.equals=" + (examId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWrittenAnswerShouldBeFound(String filter) throws Exception {
        System.out.println("Current user: " + userService.getUserWithAuthorities().orElseThrow(
            UserNotLoggedInException::new));
        restWrittenAnswerMockMvc.perform(get("/api/written-answers?sort=id,desc&" + filter))
            .andDo(MockMvcResultHandlers.log())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(writtenAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].translationFrom").value(hasItem(DEFAULT_TRANSLATION_FROM.toString())))
            .andExpect(jsonPath("$.[*].translation").value(hasItem(DEFAULT_TRANSLATION.toString())))
            .andExpect(jsonPath("$.[*].kana").value(hasItem(DEFAULT_KANA.toString())))
            .andExpect(jsonPath("$.[*].kanji").value(hasItem(DEFAULT_KANJI.toString())))
            .andExpect(jsonPath("$.[*].romaji").value(hasItem(DEFAULT_ROMAJI.toString())))
            .andExpect(jsonPath("$.[*].isRightAnswer").value(hasItem(DEFAULT_IS_RIGHT_ANSWER.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWrittenAnswerShouldNotBeFound(String filter) throws Exception {
        restWrittenAnswerMockMvc.perform(get("/api/written-answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingWrittenAnswer() throws Exception {
        // Get the writtenAnswer
        restWrittenAnswerMockMvc.perform(get("/api/written-answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWrittenAnswer() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        int databaseSizeBeforeUpdate = writtenAnswerRepository.findAll().size();

        // Update the writtenAnswer
        WrittenAnswer updatedWrittenAnswer = writtenAnswerRepository.findById(writtenAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedWrittenAnswer are not directly saved in db
        em.detach(updatedWrittenAnswer);
        updatedWrittenAnswer
            .translationFrom(UPDATED_TRANSLATION_FROM)
            .translation(UPDATED_TRANSLATION)
            .kana(UPDATED_KANA)
            .kanji(UPDATED_KANJI)
            .romaji(UPDATED_ROMAJI)
            .isRightAnswer(UPDATED_IS_RIGHT_ANSWER);
        WrittenAnswerDTO writtenAnswerDTO = writtenAnswerMapper.toDto(updatedWrittenAnswer);

        restWrittenAnswerMockMvc.perform(put("/api/written-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writtenAnswerDTO)))
            .andExpect(status().isOk());

        // Validate the WrittenAnswer in the database
        List<WrittenAnswer> writtenAnswerList = writtenAnswerRepository.findAll();
        assertThat(writtenAnswerList).hasSize(databaseSizeBeforeUpdate);
        WrittenAnswer testWrittenAnswer = writtenAnswerList.get(writtenAnswerList.size() - 1);
        assertThat(testWrittenAnswer.getTranslationFrom()).isEqualTo(UPDATED_TRANSLATION_FROM);
        assertThat(testWrittenAnswer.getTranslation()).isEqualTo(UPDATED_TRANSLATION);
        assertThat(testWrittenAnswer.getKana()).isEqualTo(UPDATED_KANA);
        assertThat(testWrittenAnswer.getKanji()).isEqualTo(UPDATED_KANJI);
        assertThat(testWrittenAnswer.getRomaji()).isEqualTo(UPDATED_ROMAJI);
        assertThat(testWrittenAnswer.isRightAnswer()).isEqualTo(UPDATED_IS_RIGHT_ANSWER);
    }

    @Test
    @Transactional
    public void updateNonExistingWrittenAnswer() throws Exception {
        int databaseSizeBeforeUpdate = writtenAnswerRepository.findAll().size();

        // Create the WrittenAnswer
        WrittenAnswerDTO writtenAnswerDTO = writtenAnswerMapper.toDto(writtenAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restWrittenAnswerMockMvc.perform(put("/api/written-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(writtenAnswerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WrittenAnswer in the database
        List<WrittenAnswer> writtenAnswerList = writtenAnswerRepository.findAll();
        assertThat(writtenAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWrittenAnswer() throws Exception {
        // Initialize the database
        writtenAnswerRepository.saveAndFlush(writtenAnswer);

        int databaseSizeBeforeDelete = writtenAnswerRepository.findAll().size();

        // Get the writtenAnswer
        restWrittenAnswerMockMvc.perform(delete("/api/written-answers/{id}", writtenAnswer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WrittenAnswer> writtenAnswerList = writtenAnswerRepository.findAll();
        assertThat(writtenAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WrittenAnswer.class);
        WrittenAnswer writtenAnswer1 = new WrittenAnswer();
        writtenAnswer1.setId(1L);
        WrittenAnswer writtenAnswer2 = new WrittenAnswer();
        writtenAnswer2.setId(writtenAnswer1.getId());
        assertThat(writtenAnswer1).isEqualTo(writtenAnswer2);
        writtenAnswer2.setId(2L);
        assertThat(writtenAnswer1).isNotEqualTo(writtenAnswer2);
        writtenAnswer1.setId(null);
        assertThat(writtenAnswer1).isNotEqualTo(writtenAnswer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WrittenAnswerDTO.class);
        WrittenAnswerDTO writtenAnswerDTO1 = WrittenAnswerDTO.builder().build();
        writtenAnswerDTO1.setId(1L);
        WrittenAnswerDTO writtenAnswerDTO2 = WrittenAnswerDTO.builder().build();
        assertThat(writtenAnswerDTO1).isNotEqualTo(writtenAnswerDTO2);
        writtenAnswerDTO2.setId(writtenAnswerDTO1.getId());
        assertThat(writtenAnswerDTO1).isEqualTo(writtenAnswerDTO2);
        writtenAnswerDTO2.setId(2L);
        assertThat(writtenAnswerDTO1).isNotEqualTo(writtenAnswerDTO2);
        writtenAnswerDTO1.setId(null);
        assertThat(writtenAnswerDTO1).isNotEqualTo(writtenAnswerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(writtenAnswerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(writtenAnswerMapper.fromId(null)).isNull();
    }
}
