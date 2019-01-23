package pl.edu.pja.nyan.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import pl.edu.pja.nyan.domain.User;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.domain.enumeration.TestType;
import pl.edu.pja.nyan.repository.ExamRepository;
import pl.edu.pja.nyan.service.ExamQueryService;
import pl.edu.pja.nyan.service.ExamService;
import pl.edu.pja.nyan.service.TagService;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.WordService;
import pl.edu.pja.nyan.service.dto.ExamDTO;
import pl.edu.pja.nyan.service.mapper.ExamMapper;
import static pl.edu.pja.nyan.web.rest.TestUtil.createFormattingConversionService;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
/**
 * Test class for the ExamResource REST controller.
 *
 * @see ExamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class ExamResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TestType DEFAULT_TYPE = TestType.WRITTEN_PL;
    private static final TestType UPDATED_TYPE = TestType.WRITTEN_MIXED;

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    @Autowired
    private ExamRepository examRepository;
    @Mock
    private ExamRepository examRepositoryMock;

    @Autowired
    private ExamMapper examMapper;
    
    @Mock
    private ExamService examServiceMock;

    @Autowired
    private ExamService examService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private WordService wordService;

    @Autowired
    private ExamQueryService examQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExamMockMvc;

    private Exam exam;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExamResource examResource = new ExamResource(examService,
            examQueryService, userService, tagService, wordService);
        this.restExamMockMvc = MockMvcBuilders.standaloneSetup(examResource)
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
    public static Exam createEntity(EntityManager em) {
        Exam exam = new Exam()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .code(DEFAULT_CODE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        exam.setCreator(user);
        return exam;
    }

    @Before
    public void initTest() {
        exam = createEntity(em);
    }

    @Test
    @Transactional
    public void createExamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();

        // Create the Exam with an existing ID
        exam.setId(1L);
        ExamDTO examDTO = examMapper.toDto(exam);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setName(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(exam);

        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examDTO)))
            .andExpect(status().isBadRequest());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setType(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(exam);

        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examDTO)))
            .andExpect(status().isBadRequest());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setCode(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(exam);

        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examDTO)))
            .andExpect(status().isBadRequest());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExams() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList
        restExamMockMvc.perform(get("/api/exams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exam.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }
    
    public void getAllExamsWithEagerRelationshipsIsEnabled() throws Exception {
        ExamResource examResource = new ExamResource(examServiceMock,
            examQueryService, userService, tagService, wordService);
        when(examServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restExamMockMvc = MockMvcBuilders.standaloneSetup(examResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restExamMockMvc.perform(get("/api/exams?eagerload=true"))
        .andExpect(status().isOk());

        verify(examServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllExamsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ExamResource examResource = new ExamResource(examServiceMock,
            examQueryService, userService, tagService, wordService);
            when(examServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restExamMockMvc = MockMvcBuilders.standaloneSetup(examResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restExamMockMvc.perform(get("/api/exams?eagerload=true"))
        .andExpect(status().isOk());

            verify(examServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get the exam
        restExamMockMvc.perform(get("/api/exams/{id}", exam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exam.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getAllExamsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where name equals to DEFAULT_NAME
        defaultExamShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the examList where name equals to UPDATED_NAME
        defaultExamShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExamsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where name in DEFAULT_NAME or UPDATED_NAME
        defaultExamShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the examList where name equals to UPDATED_NAME
        defaultExamShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExamsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where name is not null
        defaultExamShouldBeFound("name.specified=true");

        // Get all the examList where name is null
        defaultExamShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllExamsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where type equals to DEFAULT_TYPE
        defaultExamShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the examList where type equals to UPDATED_TYPE
        defaultExamShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllExamsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultExamShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the examList where type equals to UPDATED_TYPE
        defaultExamShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllExamsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where type is not null
        defaultExamShouldBeFound("type.specified=true");

        // Get all the examList where type is null
        defaultExamShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllExamsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where code equals to DEFAULT_CODE
        defaultExamShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the examList where code equals to UPDATED_CODE
        defaultExamShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllExamsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where code in DEFAULT_CODE or UPDATED_CODE
        defaultExamShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the examList where code equals to UPDATED_CODE
        defaultExamShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllExamsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where code is not null
        defaultExamShouldBeFound("code.specified=true");

        // Get all the examList where code is null
        defaultExamShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllExamsByResultsIsEqualToSomething() throws Exception {
        // Initialize the database
        ExamResult results = ExamResultResourceIntTest.createEntity(em);
        em.persist(results);
        em.flush();
        exam.addResults(results);
        examRepository.saveAndFlush(exam);
        Long resultsId = results.getId();

        // Get all the examList where results equals to resultsId
        defaultExamShouldBeFound("resultsId.equals=" + resultsId);

        // Get all the examList where results equals to resultsId + 1
        defaultExamShouldNotBeFound("resultsId.equals=" + (resultsId + 1));
    }


    @Test
    @Transactional
    public void getAllExamsByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        exam.setCreator(creator);
        examRepository.saveAndFlush(exam);
        Long creatorId = creator.getId();

        // Get all the examList where creator equals to creatorId
        defaultExamShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the examList where creator equals to creatorId + 1
        defaultExamShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }


    @Test
    @Transactional
    public void getAllExamsByWordIsEqualToSomething() throws Exception {
        // Initialize the database
        Word word = WordResourceIntTest.createEntity(em);
        em.persist(word);
        em.flush();
        exam.addWord(word);
        examRepository.saveAndFlush(exam);
        Long wordId = word.getId();

        // Get all the examList where word equals to wordId
        defaultExamShouldBeFound("wordId.equals=" + wordId);

        // Get all the examList where word equals to wordId + 1
        defaultExamShouldNotBeFound("wordId.equals=" + (wordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultExamShouldBeFound(String filter) throws Exception {
        restExamMockMvc.perform(get("/api/exams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exam.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultExamShouldNotBeFound(String filter) throws Exception {
        restExamMockMvc.perform(get("/api/exams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingExam() throws Exception {
        // Get the exam
        restExamMockMvc.perform(get("/api/exams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam
        Exam updatedExam = examRepository.findById(exam.getId()).get();
        // Disconnect from session so that the updates on updatedExam are not directly saved in db
        em.detach(updatedExam);
        updatedExam
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .code(UPDATED_CODE);
        ExamDTO examDTO = examMapper.toDto(updatedExam);

        restExamMockMvc.perform(put("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examDTO)))
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExam.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testExam.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restExamMockMvc.perform(put("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        int databaseSizeBeforeDelete = examRepository.findAll().size();

        // Get the exam
        restExamMockMvc.perform(delete("/api/exams/{id}", exam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        Exam exam1 = new Exam();
        exam1.setId(1L);
        Exam exam2 = new Exam();
        exam2.setId(exam1.getId());
        assertThat(exam1).isEqualTo(exam2);
        exam2.setId(2L);
        assertThat(exam1).isNotEqualTo(exam2);
        exam1.setId(null);
        assertThat(exam1).isNotEqualTo(exam2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        ExamDTO examDTO1 = ExamDTO.builder().build();
        examDTO1.setId(1L);
        ExamDTO examDTO2 = ExamDTO.builder().build();
        assertThat(examDTO1).isNotEqualTo(examDTO2);
        examDTO2.setId(examDTO1.getId());
        assertThat(examDTO1).isEqualTo(examDTO2);
        examDTO2.setId(2L);
        assertThat(examDTO1).isNotEqualTo(examDTO2);
        examDTO1.setId(null);
        assertThat(examDTO1).isNotEqualTo(examDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(examMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(examMapper.fromId(null)).isNull();
    }
}
