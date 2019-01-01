package pl.edu.pja.nyan.web.rest;

import pl.edu.pja.nyan.NyanApp;

import pl.edu.pja.nyan.domain.WordsTest;
import pl.edu.pja.nyan.domain.User;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.repository.WordsTestRepository;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.WordsTestService;
import pl.edu.pja.nyan.service.dto.WordsTestDTO;
import pl.edu.pja.nyan.service.mapper.WordsTestMapper;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
import pl.edu.pja.nyan.service.WordsTestQueryService;

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

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static pl.edu.pja.nyan.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pl.edu.pja.nyan.domain.enumeration.TestType;
/**
 * Test class for the WordsTestResource REST controller.
 *
 * @see WordsTestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class WordsTestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TestType DEFAULT_TYPE = TestType.WRITTEN_PL;
    private static final TestType UPDATED_TYPE = TestType.WRITTEN_MIXED;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private WordsTestRepository wordsTestRepository;
    @Mock
    private WordsTestRepository wordsTestRepositoryMock;

    @Autowired
    private WordsTestMapper wordsTestMapper;
    
    @Mock
    private WordsTestService wordsTestServiceMock;

    @Autowired
    private WordsTestService wordsTestService;

    @Autowired
    private WordsTestQueryService wordsTestQueryService;

    @Autowired
    private UserService userService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWordsTestMockMvc;

    private WordsTest wordsTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WordsTestResource wordsTestResource = new WordsTestResource(wordsTestService, wordsTestQueryService,
            userService, tagService, wordService);
        this.restWordsTestMockMvc = MockMvcBuilders.standaloneSetup(wordsTestResource)
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
    public static WordsTest createEntity(EntityManager em) {
        WordsTest wordsTest = new WordsTest()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .code(DEFAULT_CODE);
        return wordsTest;
    }

    @Before
    public void initTest() {
        wordsTest = createEntity(em);
    }

    @Test
    @Transactional
    public void createWordsTest() throws Exception {
        int databaseSizeBeforeCreate = wordsTestRepository.findAll().size();

        // Create the WordsTest
        WordsTestDTO wordsTestDTO = wordsTestMapper.toDto(wordsTest);
        restWordsTestMockMvc.perform(post("/api/words-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordsTestDTO)))
            .andExpect(status().isCreated());

        // Validate the WordsTest in the database
        List<WordsTest> wordsTestList = wordsTestRepository.findAll();
        assertThat(wordsTestList).hasSize(databaseSizeBeforeCreate + 1);
        WordsTest testWordsTest = wordsTestList.get(wordsTestList.size() - 1);
        assertThat(testWordsTest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWordsTest.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWordsTest.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createWordsTestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wordsTestRepository.findAll().size();

        // Create the WordsTest with an existing ID
        wordsTest.setId(1L);
        WordsTestDTO wordsTestDTO = wordsTestMapper.toDto(wordsTest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordsTestMockMvc.perform(post("/api/words-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordsTestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WordsTest in the database
        List<WordsTest> wordsTestList = wordsTestRepository.findAll();
        assertThat(wordsTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordsTestRepository.findAll().size();
        // set the field null
        wordsTest.setName(null);

        // Create the WordsTest, which fails.
        WordsTestDTO wordsTestDTO = wordsTestMapper.toDto(wordsTest);

        restWordsTestMockMvc.perform(post("/api/words-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordsTestDTO)))
            .andExpect(status().isBadRequest());

        List<WordsTest> wordsTestList = wordsTestRepository.findAll();
        assertThat(wordsTestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordsTestRepository.findAll().size();
        // set the field null
        wordsTest.setType(null);

        // Create the WordsTest, which fails.
        WordsTestDTO wordsTestDTO = wordsTestMapper.toDto(wordsTest);

        restWordsTestMockMvc.perform(post("/api/words-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordsTestDTO)))
            .andExpect(status().isBadRequest());

        List<WordsTest> wordsTestList = wordsTestRepository.findAll();
        assertThat(wordsTestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = wordsTestRepository.findAll().size();
        // set the field null
        wordsTest.setCode(null);

        // Create the WordsTest, which fails.
        WordsTestDTO wordsTestDTO = wordsTestMapper.toDto(wordsTest);

        restWordsTestMockMvc.perform(post("/api/words-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordsTestDTO)))
            .andExpect(status().isBadRequest());

        List<WordsTest> wordsTestList = wordsTestRepository.findAll();
        assertThat(wordsTestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWordsTests() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList
        restWordsTestMockMvc.perform(get("/api/words-tests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordsTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }
    
    public void getAllWordsTestsWithEagerRelationshipsIsEnabled() throws Exception {
        WordsTestResource wordsTestResource = new WordsTestResource(wordsTestServiceMock, wordsTestQueryService,
            userService, tagService, wordService);
        when(wordsTestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restWordsTestMockMvc = MockMvcBuilders.standaloneSetup(wordsTestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restWordsTestMockMvc.perform(get("/api/words-tests?eagerload=true"))
        .andExpect(status().isOk());

        verify(wordsTestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllWordsTestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        WordsTestResource wordsTestResource = new WordsTestResource(wordsTestServiceMock, wordsTestQueryService,
            userService, tagService, wordService);
            when(wordsTestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restWordsTestMockMvc = MockMvcBuilders.standaloneSetup(wordsTestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restWordsTestMockMvc.perform(get("/api/words-tests?eagerload=true"))
        .andExpect(status().isOk());

            verify(wordsTestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getWordsTest() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get the wordsTest
        restWordsTestMockMvc.perform(get("/api/words-tests/{id}", wordsTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wordsTest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getAllWordsTestsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where name equals to DEFAULT_NAME
        defaultWordsTestShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the wordsTestList where name equals to UPDATED_NAME
        defaultWordsTestShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWordsTestsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWordsTestShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the wordsTestList where name equals to UPDATED_NAME
        defaultWordsTestShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWordsTestsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where name is not null
        defaultWordsTestShouldBeFound("name.specified=true");

        // Get all the wordsTestList where name is null
        defaultWordsTestShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsTestsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where type equals to DEFAULT_TYPE
        defaultWordsTestShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the wordsTestList where type equals to UPDATED_TYPE
        defaultWordsTestShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllWordsTestsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultWordsTestShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the wordsTestList where type equals to UPDATED_TYPE
        defaultWordsTestShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllWordsTestsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where type is not null
        defaultWordsTestShouldBeFound("type.specified=true");

        // Get all the wordsTestList where type is null
        defaultWordsTestShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsTestsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where code equals to DEFAULT_CODE
        defaultWordsTestShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the wordsTestList where code equals to UPDATED_CODE
        defaultWordsTestShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllWordsTestsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where code in DEFAULT_CODE or UPDATED_CODE
        defaultWordsTestShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the wordsTestList where code equals to UPDATED_CODE
        defaultWordsTestShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllWordsTestsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        // Get all the wordsTestList where code is not null
        defaultWordsTestShouldBeFound("code.specified=true");

        // Get all the wordsTestList where code is null
        defaultWordsTestShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllWordsTestsByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        User creator = UserResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        wordsTest.setCreator(creator);
        wordsTestRepository.saveAndFlush(wordsTest);
        Long creatorId = creator.getId();

        // Get all the wordsTestList where creator equals to creatorId
        defaultWordsTestShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the wordsTestList where creator equals to creatorId + 1
        defaultWordsTestShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }


    @Test
    @Transactional
    public void getAllWordsTestsByWordIsEqualToSomething() throws Exception {
        // Initialize the database
        Word word = WordResourceIntTest.createEntity(em);
        em.persist(word);
        em.flush();
        wordsTest.addWord(word);
        wordsTestRepository.saveAndFlush(wordsTest);
        Long wordId = word.getId();

        // Get all the wordsTestList where word equals to wordId
        defaultWordsTestShouldBeFound("wordId.equals=" + wordId);

        // Get all the wordsTestList where word equals to wordId + 1
        defaultWordsTestShouldNotBeFound("wordId.equals=" + (wordId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWordsTestShouldBeFound(String filter) throws Exception {
        restWordsTestMockMvc.perform(get("/api/words-tests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wordsTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWordsTestShouldNotBeFound(String filter) throws Exception {
        restWordsTestMockMvc.perform(get("/api/words-tests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingWordsTest() throws Exception {
        // Get the wordsTest
        restWordsTestMockMvc.perform(get("/api/words-tests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWordsTest() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        int databaseSizeBeforeUpdate = wordsTestRepository.findAll().size();

        // Update the wordsTest
        WordsTest updatedWordsTest = wordsTestRepository.findById(wordsTest.getId()).get();
        // Disconnect from session so that the updates on updatedWordsTest are not directly saved in db
        em.detach(updatedWordsTest);
        updatedWordsTest
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .code(UPDATED_CODE);
        WordsTestDTO wordsTestDTO = wordsTestMapper.toDto(updatedWordsTest);

        restWordsTestMockMvc.perform(put("/api/words-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordsTestDTO)))
            .andExpect(status().isOk());

        // Validate the WordsTest in the database
        List<WordsTest> wordsTestList = wordsTestRepository.findAll();
        assertThat(wordsTestList).hasSize(databaseSizeBeforeUpdate);
        WordsTest testWordsTest = wordsTestList.get(wordsTestList.size() - 1);
        assertThat(testWordsTest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWordsTest.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWordsTest.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingWordsTest() throws Exception {
        int databaseSizeBeforeUpdate = wordsTestRepository.findAll().size();

        // Create the WordsTest
        WordsTestDTO wordsTestDTO = wordsTestMapper.toDto(wordsTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restWordsTestMockMvc.perform(put("/api/words-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wordsTestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WordsTest in the database
        List<WordsTest> wordsTestList = wordsTestRepository.findAll();
        assertThat(wordsTestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWordsTest() throws Exception {
        // Initialize the database
        wordsTestRepository.saveAndFlush(wordsTest);

        int databaseSizeBeforeDelete = wordsTestRepository.findAll().size();

        // Get the wordsTest
        restWordsTestMockMvc.perform(delete("/api/words-tests/{id}", wordsTest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WordsTest> wordsTestList = wordsTestRepository.findAll();
        assertThat(wordsTestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordsTest.class);
        WordsTest wordsTest1 = new WordsTest();
        wordsTest1.setId(1L);
        WordsTest wordsTest2 = new WordsTest();
        wordsTest2.setId(wordsTest1.getId());
        assertThat(wordsTest1).isEqualTo(wordsTest2);
        wordsTest2.setId(2L);
        assertThat(wordsTest1).isNotEqualTo(wordsTest2);
        wordsTest1.setId(null);
        assertThat(wordsTest1).isNotEqualTo(wordsTest2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WordsTestDTO.class);
        WordsTestDTO wordsTestDTO1 = new WordsTestDTO();
        wordsTestDTO1.setId(1L);
        WordsTestDTO wordsTestDTO2 = new WordsTestDTO();
        assertThat(wordsTestDTO1).isNotEqualTo(wordsTestDTO2);
        wordsTestDTO2.setId(wordsTestDTO1.getId());
        assertThat(wordsTestDTO1).isEqualTo(wordsTestDTO2);
        wordsTestDTO2.setId(2L);
        assertThat(wordsTestDTO1).isNotEqualTo(wordsTestDTO2);
        wordsTestDTO1.setId(null);
        assertThat(wordsTestDTO1).isNotEqualTo(wordsTestDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(wordsTestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(wordsTestMapper.fromId(null)).isNull();
    }
}
