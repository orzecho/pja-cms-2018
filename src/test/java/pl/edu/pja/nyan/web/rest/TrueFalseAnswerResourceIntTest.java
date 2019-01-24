package pl.edu.pja.nyan.web.rest;

import pl.edu.pja.nyan.NyanApp;

import pl.edu.pja.nyan.domain.TrueFalseAnswer;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.domain.ExamResult;
import pl.edu.pja.nyan.repository.TrueFalseAnswerRepository;
import pl.edu.pja.nyan.service.TrueFalseAnswerService;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerDTO;
import pl.edu.pja.nyan.service.mapper.TrueFalseAnswerMapper;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerCriteria;
import pl.edu.pja.nyan.service.TrueFalseAnswerQueryService;

import org.junit.Before;
import org.junit.Ignore;
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
 * Test class for the TrueFalseAnswerResource REST controller.
 *
 * @see TrueFalseAnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class TrueFalseAnswerResourceIntTest {

    private static final String DEFAULT_TRANSLATION_FROM = "AAAAAAAAAA";
    private static final String UPDATED_TRANSLATION_FROM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_RIGHT_ANSWER = false;
    private static final Boolean UPDATED_IS_RIGHT_ANSWER = true;

    @Autowired
    private TrueFalseAnswerRepository trueFalseAnswerRepository;


    @Autowired
    private TrueFalseAnswerMapper trueFalseAnswerMapper;


    @Autowired
    private TrueFalseAnswerService trueFalseAnswerService;

    @Autowired
    private TrueFalseAnswerQueryService trueFalseAnswerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrueFalseAnswerMockMvc;

    private TrueFalseAnswer trueFalseAnswer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrueFalseAnswerResource trueFalseAnswerResource = new TrueFalseAnswerResource(trueFalseAnswerService, trueFalseAnswerQueryService);
        this.restTrueFalseAnswerMockMvc = MockMvcBuilders.standaloneSetup(trueFalseAnswerResource)
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
    public static TrueFalseAnswer createEntity(EntityManager em) {
        TrueFalseAnswer trueFalseAnswer = new TrueFalseAnswer()
            .translationFrom(DEFAULT_TRANSLATION_FROM)
            .isRightAnswer(DEFAULT_IS_RIGHT_ANSWER);
        // Add required entity
        Word word = WordResourceIntTest.createEntity(em);
        em.persist(word);
        em.flush();
        trueFalseAnswer.setSrcWord(word);
        // Add required entity
        trueFalseAnswer.setTargetWord(word);
        // Add required entity
        ExamResult examResult = ExamResultResourceIntTest.createEntity(em);
        em.persist(examResult);
        em.flush();
        trueFalseAnswer.setExam(examResult);
        return trueFalseAnswer;
    }

    @Before
    public void initTest() {
        trueFalseAnswer = createEntity(em);
    }

    @Ignore
    @Transactional
    public void createTrueFalseAnswer() throws Exception {
        int databaseSizeBeforeCreate = trueFalseAnswerRepository.findAll().size();

        // Create the TrueFalseAnswer
        TrueFalseAnswerDTO trueFalseAnswerDTO = trueFalseAnswerMapper.toDto(trueFalseAnswer);
        restTrueFalseAnswerMockMvc.perform(post("/api/true-false-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trueFalseAnswerDTO)))
            .andExpect(status().isCreated());

        // Validate the TrueFalseAnswer in the database
        List<TrueFalseAnswer> trueFalseAnswerList = trueFalseAnswerRepository.findAll();
        assertThat(trueFalseAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        TrueFalseAnswer testTrueFalseAnswer = trueFalseAnswerList.get(trueFalseAnswerList.size() - 1);
        assertThat(testTrueFalseAnswer.getTranslationFrom()).isEqualTo(DEFAULT_TRANSLATION_FROM);
        assertThat(testTrueFalseAnswer.isRightAnswer()).isEqualTo(DEFAULT_IS_RIGHT_ANSWER);
    }

    @Ignore
    @Transactional
    public void createTrueFalseAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trueFalseAnswerRepository.findAll().size();

        // Create the TrueFalseAnswer with an existing ID
        trueFalseAnswer.setId(1L);
        TrueFalseAnswerDTO trueFalseAnswerDTO = trueFalseAnswerMapper.toDto(trueFalseAnswer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrueFalseAnswerMockMvc.perform(post("/api/true-false-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trueFalseAnswerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrueFalseAnswer in the database
        List<TrueFalseAnswer> trueFalseAnswerList = trueFalseAnswerRepository.findAll();
        assertThat(trueFalseAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Ignore
    @Transactional
    public void checkTranslationFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = trueFalseAnswerRepository.findAll().size();
        // set the field null
        trueFalseAnswer.setTranslationFrom(null);

        // Create the TrueFalseAnswer, which fails.
        TrueFalseAnswerDTO trueFalseAnswerDTO = trueFalseAnswerMapper.toDto(trueFalseAnswer);

        restTrueFalseAnswerMockMvc.perform(post("/api/true-false-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trueFalseAnswerDTO)))
            .andExpect(status().isBadRequest());

        List<TrueFalseAnswer> trueFalseAnswerList = trueFalseAnswerRepository.findAll();
        assertThat(trueFalseAnswerList).hasSize(databaseSizeBeforeTest);
    }

    @Ignore
    @Transactional
    public void checkIsRightAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = trueFalseAnswerRepository.findAll().size();
        // set the field null
        trueFalseAnswer.setIsRightAnswer(null);

        // Create the TrueFalseAnswer, which fails.
        TrueFalseAnswerDTO trueFalseAnswerDTO = trueFalseAnswerMapper.toDto(trueFalseAnswer);

        restTrueFalseAnswerMockMvc.perform(post("/api/true-false-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trueFalseAnswerDTO)))
            .andExpect(status().isBadRequest());

        List<TrueFalseAnswer> trueFalseAnswerList = trueFalseAnswerRepository.findAll();
        assertThat(trueFalseAnswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrueFalseAnswers() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        // Get all the trueFalseAnswerList
        restTrueFalseAnswerMockMvc.perform(get("/api/true-false-answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trueFalseAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].translationFrom").value(hasItem(DEFAULT_TRANSLATION_FROM.toString())))
            .andExpect(jsonPath("$.[*].isRightAnswer").value(hasItem(DEFAULT_IS_RIGHT_ANSWER.booleanValue())));
    }


    @Test
    @Transactional
    public void getTrueFalseAnswer() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        // Get the trueFalseAnswer
        restTrueFalseAnswerMockMvc.perform(get("/api/true-false-answers/{id}", trueFalseAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trueFalseAnswer.getId().intValue()))
            .andExpect(jsonPath("$.translationFrom").value(DEFAULT_TRANSLATION_FROM.toString()))
            .andExpect(jsonPath("$.isRightAnswer").value(DEFAULT_IS_RIGHT_ANSWER.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllTrueFalseAnswersByTranslationFromIsEqualToSomething() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        // Get all the trueFalseAnswerList where translationFrom equals to DEFAULT_TRANSLATION_FROM
        defaultTrueFalseAnswerShouldBeFound("translationFrom.equals=" + DEFAULT_TRANSLATION_FROM);

        // Get all the trueFalseAnswerList where translationFrom equals to UPDATED_TRANSLATION_FROM
        defaultTrueFalseAnswerShouldNotBeFound("translationFrom.equals=" + UPDATED_TRANSLATION_FROM);
    }

    @Test
    @Transactional
    public void getAllTrueFalseAnswersByTranslationFromIsInShouldWork() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        // Get all the trueFalseAnswerList where translationFrom in DEFAULT_TRANSLATION_FROM or UPDATED_TRANSLATION_FROM
        defaultTrueFalseAnswerShouldBeFound("translationFrom.in=" + DEFAULT_TRANSLATION_FROM + "," + UPDATED_TRANSLATION_FROM);

        // Get all the trueFalseAnswerList where translationFrom equals to UPDATED_TRANSLATION_FROM
        defaultTrueFalseAnswerShouldNotBeFound("translationFrom.in=" + UPDATED_TRANSLATION_FROM);
    }

    @Test
    @Transactional
    public void getAllTrueFalseAnswersByTranslationFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        // Get all the trueFalseAnswerList where translationFrom is not null
        defaultTrueFalseAnswerShouldBeFound("translationFrom.specified=true");

        // Get all the trueFalseAnswerList where translationFrom is null
        defaultTrueFalseAnswerShouldNotBeFound("translationFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllTrueFalseAnswersByIsRightAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        // Get all the trueFalseAnswerList where isRightAnswer equals to DEFAULT_IS_RIGHT_ANSWER
        defaultTrueFalseAnswerShouldBeFound("isRightAnswer.equals=" + DEFAULT_IS_RIGHT_ANSWER);

        // Get all the trueFalseAnswerList where isRightAnswer equals to UPDATED_IS_RIGHT_ANSWER
        defaultTrueFalseAnswerShouldNotBeFound("isRightAnswer.equals=" + UPDATED_IS_RIGHT_ANSWER);
    }

    @Test
    @Transactional
    public void getAllTrueFalseAnswersByIsRightAnswerIsInShouldWork() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        // Get all the trueFalseAnswerList where isRightAnswer in DEFAULT_IS_RIGHT_ANSWER or UPDATED_IS_RIGHT_ANSWER
        defaultTrueFalseAnswerShouldBeFound("isRightAnswer.in=" + DEFAULT_IS_RIGHT_ANSWER + "," + UPDATED_IS_RIGHT_ANSWER);

        // Get all the trueFalseAnswerList where isRightAnswer equals to UPDATED_IS_RIGHT_ANSWER
        defaultTrueFalseAnswerShouldNotBeFound("isRightAnswer.in=" + UPDATED_IS_RIGHT_ANSWER);
    }

    @Test
    @Transactional
    public void getAllTrueFalseAnswersByIsRightAnswerIsNullOrNotNull() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        // Get all the trueFalseAnswerList where isRightAnswer is not null
        defaultTrueFalseAnswerShouldBeFound("isRightAnswer.specified=true");

        // Get all the trueFalseAnswerList where isRightAnswer is null
        defaultTrueFalseAnswerShouldNotBeFound("isRightAnswer.specified=false");
    }

    @Test
    @Transactional
    public void getAllTrueFalseAnswersBySrcWordIsEqualToSomething() throws Exception {
        // Initialize the database
        Word srcWord = WordResourceIntTest.createEntity(em);
        em.persist(srcWord);
        em.flush();
        trueFalseAnswer.setSrcWord(srcWord);
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);
        Long srcWordId = srcWord.getId();

        // Get all the trueFalseAnswerList where srcWord equals to srcWordId
        defaultTrueFalseAnswerShouldBeFound("srcWordId.equals=" + srcWordId);

        // Get all the trueFalseAnswerList where srcWord equals to srcWordId + 1
        defaultTrueFalseAnswerShouldNotBeFound("srcWordId.equals=" + (srcWordId + 1));
    }


    @Test
    @Transactional
    public void getAllTrueFalseAnswersByTargetWordIsEqualToSomething() throws Exception {
        // Initialize the database
        Word targetWord = WordResourceIntTest.createEntity(em);
        em.persist(targetWord);
        em.flush();
        trueFalseAnswer.setTargetWord(targetWord);
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);
        Long targetWordId = targetWord.getId();

        // Get all the trueFalseAnswerList where targetWord equals to targetWordId
        defaultTrueFalseAnswerShouldBeFound("targetWordId.equals=" + targetWordId);

        // Get all the trueFalseAnswerList where targetWord equals to targetWordId + 1
        defaultTrueFalseAnswerShouldNotBeFound("targetWordId.equals=" + (targetWordId + 1));
    }


    @Test
    @Transactional
    public void getAllTrueFalseAnswersByExamIsEqualToSomething() throws Exception {
        // Initialize the database
        ExamResult exam = ExamResultResourceIntTest.createEntity(em);
        em.persist(exam);
        em.flush();
        trueFalseAnswer.setExam(exam);
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);
        Long examId = exam.getId();

        // Get all the trueFalseAnswerList where exam equals to examId
        defaultTrueFalseAnswerShouldBeFound("examId.equals=" + examId);

        // Get all the trueFalseAnswerList where exam equals to examId + 1
        defaultTrueFalseAnswerShouldNotBeFound("examId.equals=" + (examId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTrueFalseAnswerShouldBeFound(String filter) throws Exception {
        restTrueFalseAnswerMockMvc.perform(get("/api/true-false-answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trueFalseAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].translationFrom").value(hasItem(DEFAULT_TRANSLATION_FROM.toString())))
            .andExpect(jsonPath("$.[*].isRightAnswer").value(hasItem(DEFAULT_IS_RIGHT_ANSWER.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTrueFalseAnswerShouldNotBeFound(String filter) throws Exception {
        restTrueFalseAnswerMockMvc.perform(get("/api/true-false-answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingTrueFalseAnswer() throws Exception {
        // Get the trueFalseAnswer
        restTrueFalseAnswerMockMvc.perform(get("/api/true-false-answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Ignore
    @Transactional
    public void updateTrueFalseAnswer() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        int databaseSizeBeforeUpdate = trueFalseAnswerRepository.findAll().size();

        // Update the trueFalseAnswer
        TrueFalseAnswer updatedTrueFalseAnswer = trueFalseAnswerRepository.findById(trueFalseAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedTrueFalseAnswer are not directly saved in db
        em.detach(updatedTrueFalseAnswer);
        updatedTrueFalseAnswer
            .translationFrom(UPDATED_TRANSLATION_FROM)
            .isRightAnswer(UPDATED_IS_RIGHT_ANSWER);
        TrueFalseAnswerDTO trueFalseAnswerDTO = trueFalseAnswerMapper.toDto(updatedTrueFalseAnswer);

        restTrueFalseAnswerMockMvc.perform(put("/api/true-false-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trueFalseAnswerDTO)))
            .andExpect(status().isOk());

        // Validate the TrueFalseAnswer in the database
        List<TrueFalseAnswer> trueFalseAnswerList = trueFalseAnswerRepository.findAll();
        assertThat(trueFalseAnswerList).hasSize(databaseSizeBeforeUpdate);
        TrueFalseAnswer testTrueFalseAnswer = trueFalseAnswerList.get(trueFalseAnswerList.size() - 1);
        assertThat(testTrueFalseAnswer.getTranslationFrom()).isEqualTo(UPDATED_TRANSLATION_FROM);
        assertThat(testTrueFalseAnswer.isRightAnswer()).isEqualTo(UPDATED_IS_RIGHT_ANSWER);
    }

    @Ignore
    @Transactional
    public void updateNonExistingTrueFalseAnswer() throws Exception {
        int databaseSizeBeforeUpdate = trueFalseAnswerRepository.findAll().size();

        // Create the TrueFalseAnswer
        TrueFalseAnswerDTO trueFalseAnswerDTO = trueFalseAnswerMapper.toDto(trueFalseAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrueFalseAnswerMockMvc.perform(put("/api/true-false-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trueFalseAnswerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrueFalseAnswer in the database
        List<TrueFalseAnswer> trueFalseAnswerList = trueFalseAnswerRepository.findAll();
        assertThat(trueFalseAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrueFalseAnswer() throws Exception {
        // Initialize the database
        trueFalseAnswerRepository.saveAndFlush(trueFalseAnswer);

        int databaseSizeBeforeDelete = trueFalseAnswerRepository.findAll().size();

        // Get the trueFalseAnswer
        restTrueFalseAnswerMockMvc.perform(delete("/api/true-false-answers/{id}", trueFalseAnswer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TrueFalseAnswer> trueFalseAnswerList = trueFalseAnswerRepository.findAll();
        assertThat(trueFalseAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrueFalseAnswer.class);
        TrueFalseAnswer trueFalseAnswer1 = new TrueFalseAnswer();
        trueFalseAnswer1.setId(1L);
        TrueFalseAnswer trueFalseAnswer2 = new TrueFalseAnswer();
        trueFalseAnswer2.setId(trueFalseAnswer1.getId());
        assertThat(trueFalseAnswer1).isEqualTo(trueFalseAnswer2);
        trueFalseAnswer2.setId(2L);
        assertThat(trueFalseAnswer1).isNotEqualTo(trueFalseAnswer2);
        trueFalseAnswer1.setId(null);
        assertThat(trueFalseAnswer1).isNotEqualTo(trueFalseAnswer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TrueFalseAnswerDTO trueFalseAnswerDTO1 = TrueFalseAnswerDTO.builder().build();
        trueFalseAnswerDTO1.setId(1L);
        TrueFalseAnswerDTO trueFalseAnswerDTO2 = TrueFalseAnswerDTO.builder().build();
        assertThat(trueFalseAnswerDTO1).isNotEqualTo(trueFalseAnswerDTO2);
        trueFalseAnswerDTO2.setId(trueFalseAnswerDTO1.getId());
        assertThat(trueFalseAnswerDTO1).isEqualTo(trueFalseAnswerDTO2);
        trueFalseAnswerDTO2.setId(2L);
        assertThat(trueFalseAnswerDTO1).isNotEqualTo(trueFalseAnswerDTO2);
        trueFalseAnswerDTO1.setId(null);
        assertThat(trueFalseAnswerDTO1).isNotEqualTo(trueFalseAnswerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(trueFalseAnswerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(trueFalseAnswerMapper.fromId(null)).isNull();
    }
}
