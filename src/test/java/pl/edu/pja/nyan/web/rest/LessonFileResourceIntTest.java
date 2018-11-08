package pl.edu.pja.nyan.web.rest;

import pl.edu.pja.nyan.NyanApp;

import pl.edu.pja.nyan.domain.LessonFile;
import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.repository.LessonFileRepository;
import pl.edu.pja.nyan.service.LessonFileService;
import pl.edu.pja.nyan.service.dto.LessonFileDTO;
import pl.edu.pja.nyan.service.mapper.LessonFileMapper;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
import pl.edu.pja.nyan.service.dto.LessonFileCriteria;
import pl.edu.pja.nyan.service.LessonFileQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static pl.edu.pja.nyan.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LessonFileResource REST controller.
 *
 * @see LessonFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class LessonFileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private LessonFileRepository lessonFileRepository;


    @Autowired
    private LessonFileMapper lessonFileMapper;
    

    @Autowired
    private LessonFileService lessonFileService;

    @Autowired
    private LessonFileQueryService lessonFileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLessonFileMockMvc;

    private LessonFile lessonFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LessonFileResource lessonFileResource = new LessonFileResource(lessonFileService, lessonFileQueryService);
        this.restLessonFileMockMvc = MockMvcBuilders.standaloneSetup(lessonFileResource)
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
    public static LessonFile createEntity(EntityManager em) {
        LessonFile lessonFile = new LessonFile()
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return lessonFile;
    }

    @Before
    public void initTest() {
        lessonFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createLessonFile() throws Exception {
        int databaseSizeBeforeCreate = lessonFileRepository.findAll().size();

        // Create the LessonFile
        LessonFileDTO lessonFileDTO = lessonFileMapper.toDto(lessonFile);
        restLessonFileMockMvc.perform(post("/api/lesson-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonFileDTO)))
            .andExpect(status().isCreated());

        // Validate the LessonFile in the database
        List<LessonFile> lessonFileList = lessonFileRepository.findAll();
        assertThat(lessonFileList).hasSize(databaseSizeBeforeCreate + 1);
        LessonFile testLessonFile = lessonFileList.get(lessonFileList.size() - 1);
        assertThat(testLessonFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLessonFile.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testLessonFile.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createLessonFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lessonFileRepository.findAll().size();

        // Create the LessonFile with an existing ID
        lessonFile.setId(1L);
        LessonFileDTO lessonFileDTO = lessonFileMapper.toDto(lessonFile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonFileMockMvc.perform(post("/api/lesson-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LessonFile in the database
        List<LessonFile> lessonFileList = lessonFileRepository.findAll();
        assertThat(lessonFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonFileRepository.findAll().size();
        // set the field null
        lessonFile.setName(null);

        // Create the LessonFile, which fails.
        LessonFileDTO lessonFileDTO = lessonFileMapper.toDto(lessonFile);

        restLessonFileMockMvc.perform(post("/api/lesson-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonFileDTO)))
            .andExpect(status().isBadRequest());

        List<LessonFile> lessonFileList = lessonFileRepository.findAll();
        assertThat(lessonFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLessonFiles() throws Exception {
        // Initialize the database
        lessonFileRepository.saveAndFlush(lessonFile);

        // Get all the lessonFileList
        restLessonFileMockMvc.perform(get("/api/lesson-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }
    

    @Test
    @Transactional
    public void getLessonFile() throws Exception {
        // Initialize the database
        lessonFileRepository.saveAndFlush(lessonFile);

        // Get the lessonFile
        restLessonFileMockMvc.perform(get("/api/lesson-files/{id}", lessonFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lessonFile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getAllLessonFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonFileRepository.saveAndFlush(lessonFile);

        // Get all the lessonFileList where name equals to DEFAULT_NAME
        defaultLessonFileShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the lessonFileList where name equals to UPDATED_NAME
        defaultLessonFileShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLessonFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        lessonFileRepository.saveAndFlush(lessonFile);

        // Get all the lessonFileList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLessonFileShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the lessonFileList where name equals to UPDATED_NAME
        defaultLessonFileShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLessonFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonFileRepository.saveAndFlush(lessonFile);

        // Get all the lessonFileList where name is not null
        defaultLessonFileShouldBeFound("name.specified=true");

        // Get all the lessonFileList where name is null
        defaultLessonFileShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllLessonFilesByLessonIsEqualToSomething() throws Exception {
        // Initialize the database
        Lesson lesson = LessonResourceIntTest.createEntity(em);
        em.persist(lesson);
        em.flush();
        lessonFile.setLesson(lesson);
        lessonFileRepository.saveAndFlush(lessonFile);
        Long lessonId = lesson.getId();

        // Get all the lessonFileList where lesson equals to lessonId
        defaultLessonFileShouldBeFound("lessonId.equals=" + lessonId);

        // Get all the lessonFileList where lesson equals to lessonId + 1
        defaultLessonFileShouldNotBeFound("lessonId.equals=" + (lessonId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLessonFileShouldBeFound(String filter) throws Exception {
        restLessonFileMockMvc.perform(get("/api/lesson-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLessonFileShouldNotBeFound(String filter) throws Exception {
        restLessonFileMockMvc.perform(get("/api/lesson-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingLessonFile() throws Exception {
        // Get the lessonFile
        restLessonFileMockMvc.perform(get("/api/lesson-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLessonFile() throws Exception {
        // Initialize the database
        lessonFileRepository.saveAndFlush(lessonFile);

        int databaseSizeBeforeUpdate = lessonFileRepository.findAll().size();

        // Update the lessonFile
        LessonFile updatedLessonFile = lessonFileRepository.findById(lessonFile.getId()).get();
        // Disconnect from session so that the updates on updatedLessonFile are not directly saved in db
        em.detach(updatedLessonFile);
        updatedLessonFile
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);
        LessonFileDTO lessonFileDTO = lessonFileMapper.toDto(updatedLessonFile);

        restLessonFileMockMvc.perform(put("/api/lesson-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonFileDTO)))
            .andExpect(status().isOk());

        // Validate the LessonFile in the database
        List<LessonFile> lessonFileList = lessonFileRepository.findAll();
        assertThat(lessonFileList).hasSize(databaseSizeBeforeUpdate);
        LessonFile testLessonFile = lessonFileList.get(lessonFileList.size() - 1);
        assertThat(testLessonFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLessonFile.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testLessonFile.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingLessonFile() throws Exception {
        int databaseSizeBeforeUpdate = lessonFileRepository.findAll().size();

        // Create the LessonFile
        LessonFileDTO lessonFileDTO = lessonFileMapper.toDto(lessonFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restLessonFileMockMvc.perform(put("/api/lesson-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lessonFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LessonFile in the database
        List<LessonFile> lessonFileList = lessonFileRepository.findAll();
        assertThat(lessonFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLessonFile() throws Exception {
        // Initialize the database
        lessonFileRepository.saveAndFlush(lessonFile);

        int databaseSizeBeforeDelete = lessonFileRepository.findAll().size();

        // Get the lessonFile
        restLessonFileMockMvc.perform(delete("/api/lesson-files/{id}", lessonFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LessonFile> lessonFileList = lessonFileRepository.findAll();
        assertThat(lessonFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonFile.class);
        LessonFile lessonFile1 = new LessonFile();
        lessonFile1.setId(1L);
        LessonFile lessonFile2 = new LessonFile();
        lessonFile2.setId(lessonFile1.getId());
        assertThat(lessonFile1).isEqualTo(lessonFile2);
        lessonFile2.setId(2L);
        assertThat(lessonFile1).isNotEqualTo(lessonFile2);
        lessonFile1.setId(null);
        assertThat(lessonFile1).isNotEqualTo(lessonFile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonFileDTO.class);
        LessonFileDTO lessonFileDTO1 = new LessonFileDTO();
        lessonFileDTO1.setId(1L);
        LessonFileDTO lessonFileDTO2 = new LessonFileDTO();
        assertThat(lessonFileDTO1).isNotEqualTo(lessonFileDTO2);
        lessonFileDTO2.setId(lessonFileDTO1.getId());
        assertThat(lessonFileDTO1).isEqualTo(lessonFileDTO2);
        lessonFileDTO2.setId(2L);
        assertThat(lessonFileDTO1).isNotEqualTo(lessonFileDTO2);
        lessonFileDTO1.setId(null);
        assertThat(lessonFileDTO1).isNotEqualTo(lessonFileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(lessonFileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(lessonFileMapper.fromId(null)).isNull();
    }
}
