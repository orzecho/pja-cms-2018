package pl.edu.pja.nyan.web.rest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import pl.edu.pja.nyan.domain.Exam;
import pl.edu.pja.nyan.domain.ExamResult;
import pl.edu.pja.nyan.domain.TrueFalseAnswer;
import pl.edu.pja.nyan.domain.User;
import pl.edu.pja.nyan.domain.WrittenAnswer;
import pl.edu.pja.nyan.repository.ExamResultRepository;
import pl.edu.pja.nyan.service.ExamResultQueryService;
import pl.edu.pja.nyan.service.ExamResultService;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.dto.ExamResultDTO;
import pl.edu.pja.nyan.service.mapper.ExamResultMapper;
import pl.edu.pja.nyan.service.mapper.UserMapper;
import static pl.edu.pja.nyan.web.rest.TestUtil.createFormattingConversionService;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ExamResultResource REST controller.
 *
 * @see ExamResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class ExamResultResourceIntTest {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RESULT = 1;
    private static final Integer UPDATED_RESULT = 2;

    @Autowired
    private ExamResultRepository examResultRepository;


    @Autowired
    private ExamResultMapper examResultMapper;
    

    @Autowired
    private ExamResultService examResultService;

    @Autowired
    private ExamResultQueryService examResultQueryService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExamResultMockMvc;

    private ExamResult examResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExamResultResource examResultResource = new ExamResultResource(examResultService,
            examResultQueryService, userService, userMapper);
        this.restExamResultMockMvc = MockMvcBuilders.standaloneSetup(examResultResource)
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
    public static ExamResult createEntity(EntityManager em) {
        ExamResult examResult = new ExamResult()
            .date(DEFAULT_DATE)
            .result(DEFAULT_RESULT);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        examResult.setStudent(user);
        // Add required entity
        Exam exam = ExamResourceIntTest.createEntity(em);
        em.persist(exam);
        em.flush();
        examResult.setExam(exam);
        return examResult;
    }

    @Before
    public void initTest() {
        examResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createExamResult() throws Exception {
        int databaseSizeBeforeCreate = examResultRepository.findAll().size();

        // Create the ExamResult
        ExamResultDTO examResultDTO = examResultMapper.toDto(examResult);
        restExamResultMockMvc.perform(post("/api/exam-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examResultDTO)))
            .andExpect(status().isCreated());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeCreate + 1);
        ExamResult testExamResult = examResultList.get(examResultList.size() - 1);
        assertThat(testExamResult.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testExamResult.getResult()).isEqualTo(DEFAULT_RESULT);
    }

    @Test
    @Transactional
    public void createExamResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examResultRepository.findAll().size();

        // Create the ExamResult with an existing ID
        examResult.setId(1L);
        ExamResultDTO examResultDTO = examResultMapper.toDto(examResult);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamResultMockMvc.perform(post("/api/exam-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = examResultRepository.findAll().size();
        // set the field null
        examResult.setDate(null);

        // Create the ExamResult, which fails.
        ExamResultDTO examResultDTO = examResultMapper.toDto(examResult);

        restExamResultMockMvc.perform(post("/api/exam-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examResultDTO)))
            .andExpect(status().isBadRequest());

        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = examResultRepository.findAll().size();
        // set the field null
        examResult.setResult(null);

        // Create the ExamResult, which fails.
        ExamResultDTO examResultDTO = examResultMapper.toDto(examResult);

        restExamResultMockMvc.perform(post("/api/exam-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examResultDTO)))
            .andExpect(status().isBadRequest());

        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExamResults() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList
        restExamResultMockMvc.perform(get("/api/exam-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)));
    }
    

    @Test
    @Transactional
    public void getExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get the examResult
        restExamResultMockMvc.perform(get("/api/exam-results/{id}", examResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(examResult.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT));
    }

    @Test
    @Transactional
    public void getAllExamResultsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList where date equals to DEFAULT_DATE
        defaultExamResultShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the examResultList where date equals to UPDATED_DATE
        defaultExamResultShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllExamResultsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList where date in DEFAULT_DATE or UPDATED_DATE
        defaultExamResultShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the examResultList where date equals to UPDATED_DATE
        defaultExamResultShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllExamResultsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList where date is not null
        defaultExamResultShouldBeFound("date.specified=true");

        // Get all the examResultList where date is null
        defaultExamResultShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllExamResultsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList where result equals to DEFAULT_RESULT
        defaultExamResultShouldBeFound("result.equals=" + DEFAULT_RESULT);

        // Get all the examResultList where result equals to UPDATED_RESULT
        defaultExamResultShouldNotBeFound("result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllExamResultsByResultIsInShouldWork() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList where result in DEFAULT_RESULT or UPDATED_RESULT
        defaultExamResultShouldBeFound("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT);

        // Get all the examResultList where result equals to UPDATED_RESULT
        defaultExamResultShouldNotBeFound("result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllExamResultsByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList where result is not null
        defaultExamResultShouldBeFound("result.specified=true");

        // Get all the examResultList where result is null
        defaultExamResultShouldNotBeFound("result.specified=false");
    }

    @Test
    @Transactional
    public void getAllExamResultsByResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList where result greater than or equals to DEFAULT_RESULT
        defaultExamResultShouldBeFound("result.greaterOrEqualThan=" + DEFAULT_RESULT);

        // Get all the examResultList where result greater than or equals to UPDATED_RESULT
        defaultExamResultShouldNotBeFound("result.greaterOrEqualThan=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void getAllExamResultsByResultIsLessThanSomething() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResultList where result less than or equals to DEFAULT_RESULT
        defaultExamResultShouldNotBeFound("result.lessThan=" + DEFAULT_RESULT);

        // Get all the examResultList where result less than or equals to UPDATED_RESULT
        defaultExamResultShouldBeFound("result.lessThan=" + UPDATED_RESULT);
    }


    @Test
    @Transactional
    public void getAllExamResultsByWrittenAnswersIsEqualToSomething() throws Exception {
        // Initialize the database
        WrittenAnswer writtenAnswers = WrittenAnswerResourceIntTest.createEntity(em);
        em.persist(writtenAnswers);
        em.flush();
        examResult.addWrittenAnswers(writtenAnswers);
        examResultRepository.saveAndFlush(examResult);
        Long writtenAnswersId = writtenAnswers.getId();

        // Get all the examResultList where writtenAnswers equals to writtenAnswersId
        defaultExamResultShouldBeFound("writtenAnswersId.equals=" + writtenAnswersId);

        // Get all the examResultList where writtenAnswers equals to writtenAnswersId + 1
        defaultExamResultShouldNotBeFound("writtenAnswersId.equals=" + (writtenAnswersId + 1));
    }


    @Test
    @Transactional
    public void getAllExamResultsByTrueFalseAnswersIsEqualToSomething() throws Exception {
        // Initialize the database
        TrueFalseAnswer trueFalseAnswers = TrueFalseAnswerResourceIntTest.createEntity(em);
        em.persist(trueFalseAnswers);
        em.flush();
        examResult.addTrueFalseAnswers(trueFalseAnswers);
        examResultRepository.saveAndFlush(examResult);
        Long trueFalseAnswersId = trueFalseAnswers.getId();

        // Get all the examResultList where trueFalseAnswers equals to trueFalseAnswersId
        defaultExamResultShouldBeFound("trueFalseAnswersId.equals=" + trueFalseAnswersId);

        // Get all the examResultList where trueFalseAnswers equals to trueFalseAnswersId + 1
        defaultExamResultShouldNotBeFound("trueFalseAnswersId.equals=" + (trueFalseAnswersId + 1));
    }


    @Test
    @Transactional
    public void getAllExamResultsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        User student = UserResourceIntTest.createEntity(em);
        em.persist(student);
        em.flush();
        examResult.setStudent(student);
        examResultRepository.saveAndFlush(examResult);
        Long studentId = student.getId();

        // Get all the examResultList where student equals to studentId
        defaultExamResultShouldBeFound("studentId.equals=" + studentId);

        // Get all the examResultList where student equals to studentId + 1
        defaultExamResultShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }


    @Test
    @Transactional
    public void getAllExamResultsByExamIsEqualToSomething() throws Exception {
        // Initialize the database
        Exam exam = ExamResourceIntTest.createEntity(em);
        em.persist(exam);
        em.flush();
        examResult.setExam(exam);
        examResultRepository.saveAndFlush(examResult);
        Long examId = exam.getId();

        // Get all the examResultList where exam equals to examId
        defaultExamResultShouldBeFound("examId.equals=" + examId);

        // Get all the examResultList where exam equals to examId + 1
        defaultExamResultShouldNotBeFound("examId.equals=" + (examId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultExamResultShouldBeFound(String filter) throws Exception {
        restExamResultMockMvc.perform(get("/api/exam-results?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultExamResultShouldNotBeFound(String filter) throws Exception {
        restExamResultMockMvc.perform(get("/api/exam-results?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingExamResult() throws Exception {
        // Get the examResult
        restExamResultMockMvc.perform(get("/api/exam-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();

        // Update the examResult
        ExamResult updatedExamResult = examResultRepository.findById(examResult.getId()).get();
        // Disconnect from session so that the updates on updatedExamResult are not directly saved in db
        em.detach(updatedExamResult);
        updatedExamResult
            .date(UPDATED_DATE)
            .result(UPDATED_RESULT);
        ExamResultDTO examResultDTO = examResultMapper.toDto(updatedExamResult);

        restExamResultMockMvc.perform(put("/api/exam-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examResultDTO)))
            .andExpect(status().isOk());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
        ExamResult testExamResult = examResultList.get(examResultList.size() - 1);
        assertThat(testExamResult.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testExamResult.getResult()).isEqualTo(UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void updateNonExistingExamResult() throws Exception {
        int databaseSizeBeforeUpdate = examResultRepository.findAll().size();

        // Create the ExamResult
        ExamResultDTO examResultDTO = examResultMapper.toDto(examResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restExamResultMockMvc.perform(put("/api/exam-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExamResult in the database
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        int databaseSizeBeforeDelete = examResultRepository.findAll().size();

        // Get the examResult
        restExamResultMockMvc.perform(delete("/api/exam-results/{id}", examResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExamResult> examResultList = examResultRepository.findAll();
        assertThat(examResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamResult.class);
        ExamResult examResult1 = new ExamResult();
        examResult1.setId(1L);
        ExamResult examResult2 = new ExamResult();
        examResult2.setId(examResult1.getId());
        assertThat(examResult1).isEqualTo(examResult2);
        examResult2.setId(2L);
        assertThat(examResult1).isNotEqualTo(examResult2);
        examResult1.setId(null);
        assertThat(examResult1).isNotEqualTo(examResult2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        ExamResultDTO examResultDTO1 = ExamResultDTO.builder().build();
        examResultDTO1.setId(1L);
        ExamResultDTO examResultDTO2 = ExamResultDTO.builder().build();
        assertThat(examResultDTO1).isNotEqualTo(examResultDTO2);
        examResultDTO2.setId(examResultDTO1.getId());
        assertThat(examResultDTO1).isEqualTo(examResultDTO2);
        examResultDTO2.setId(2L);
        assertThat(examResultDTO1).isNotEqualTo(examResultDTO2);
        examResultDTO1.setId(null);
        assertThat(examResultDTO1).isNotEqualTo(examResultDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(examResultMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(examResultMapper.fromId(null)).isNull();
    }
}
