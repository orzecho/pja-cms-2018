package pl.edu.pja.nyan.web.rest;

import pl.edu.pja.nyan.NyanApp;

import pl.edu.pja.nyan.domain.FillingGapsTestItem;
import pl.edu.pja.nyan.domain.GapItem;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.repository.FillingGapsTestItemRepository;
import pl.edu.pja.nyan.service.FillingGapsTestItemService;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemDTO;
import pl.edu.pja.nyan.service.mapper.FillingGapsTestItemMapper;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemCriteria;
import pl.edu.pja.nyan.service.FillingGapsTestItemQueryService;

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

import javax.persistence.EntityManager;
import java.util.List;


import static pl.edu.pja.nyan.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FillingGapsTestItemResource REST controller.
 *
 * @see FillingGapsTestItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class FillingGapsTestItemResourceIntTest {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    @Autowired
    private FillingGapsTestItemRepository fillingGapsTestItemRepository;


    @Autowired
    private FillingGapsTestItemMapper fillingGapsTestItemMapper;


    @Autowired
    private FillingGapsTestItemService fillingGapsTestItemService;

    @Autowired
    private FillingGapsTestItemQueryService fillingGapsTestItemQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFillingGapsTestItemMockMvc;

    private FillingGapsTestItem fillingGapsTestItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FillingGapsTestItemResource fillingGapsTestItemResource = new FillingGapsTestItemResource(fillingGapsTestItemService, fillingGapsTestItemQueryService);
        this.restFillingGapsTestItemMockMvc = MockMvcBuilders.standaloneSetup(fillingGapsTestItemResource)
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
    public static FillingGapsTestItem createEntity(EntityManager em) {
        FillingGapsTestItem fillingGapsTestItem = new FillingGapsTestItem()
            .question(DEFAULT_QUESTION);
        return fillingGapsTestItem;
    }

    @Before
    public void initTest() {
        fillingGapsTestItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createFillingGapsTestItem() throws Exception {
        int databaseSizeBeforeCreate = fillingGapsTestItemRepository.findAll().size();

        // Create the FillingGapsTestItem
        FillingGapsTestItemDTO fillingGapsTestItemDTO = fillingGapsTestItemMapper.toDto(fillingGapsTestItem);
        restFillingGapsTestItemMockMvc.perform(post("/api/filling-gaps-test-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fillingGapsTestItemDTO)))
            .andExpect(status().isCreated());

        // Validate the FillingGapsTestItem in the database
        List<FillingGapsTestItem> fillingGapsTestItemList = fillingGapsTestItemRepository.findAll();
        assertThat(fillingGapsTestItemList).hasSize(databaseSizeBeforeCreate + 1);
        FillingGapsTestItem testFillingGapsTestItem = fillingGapsTestItemList.get(fillingGapsTestItemList.size() - 1);
        assertThat(testFillingGapsTestItem.getQuestion()).isEqualTo(DEFAULT_QUESTION);
    }

    @Test
    @Transactional
    public void createFillingGapsTestItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fillingGapsTestItemRepository.findAll().size();

        // Create the FillingGapsTestItem with an existing ID
        fillingGapsTestItem.setId(1L);
        FillingGapsTestItemDTO fillingGapsTestItemDTO = fillingGapsTestItemMapper.toDto(fillingGapsTestItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFillingGapsTestItemMockMvc.perform(post("/api/filling-gaps-test-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fillingGapsTestItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FillingGapsTestItem in the database
        List<FillingGapsTestItem> fillingGapsTestItemList = fillingGapsTestItemRepository.findAll();
        assertThat(fillingGapsTestItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fillingGapsTestItemRepository.findAll().size();
        // set the field null
        fillingGapsTestItem.setQuestion(null);

        // Create the FillingGapsTestItem, which fails.
        FillingGapsTestItemDTO fillingGapsTestItemDTO = fillingGapsTestItemMapper.toDto(fillingGapsTestItem);

        restFillingGapsTestItemMockMvc.perform(post("/api/filling-gaps-test-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fillingGapsTestItemDTO)))
            .andExpect(status().isBadRequest());

        List<FillingGapsTestItem> fillingGapsTestItemList = fillingGapsTestItemRepository.findAll();
        assertThat(fillingGapsTestItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFillingGapsTestItems() throws Exception {
        // Initialize the database
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);

        // Get all the fillingGapsTestItemList
        restFillingGapsTestItemMockMvc.perform(get("/api/filling-gaps-test-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fillingGapsTestItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())));
    }


    @Test
    @Transactional
    public void getFillingGapsTestItem() throws Exception {
        // Initialize the database
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);

        // Get the fillingGapsTestItem
        restFillingGapsTestItemMockMvc.perform(get("/api/filling-gaps-test-items/{id}", fillingGapsTestItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fillingGapsTestItem.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()));
    }

    @Test
    @Transactional
    public void getAllFillingGapsTestItemsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);

        // Get all the fillingGapsTestItemList where question equals to DEFAULT_QUESTION
        defaultFillingGapsTestItemShouldBeFound("question.equals=" + DEFAULT_QUESTION);

        // Get all the fillingGapsTestItemList where question equals to UPDATED_QUESTION
        defaultFillingGapsTestItemShouldNotBeFound("question.equals=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    public void getAllFillingGapsTestItemsByQuestionIsInShouldWork() throws Exception {
        // Initialize the database
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);

        // Get all the fillingGapsTestItemList where question in DEFAULT_QUESTION or UPDATED_QUESTION
        defaultFillingGapsTestItemShouldBeFound("question.in=" + DEFAULT_QUESTION + "," + UPDATED_QUESTION);

        // Get all the fillingGapsTestItemList where question equals to UPDATED_QUESTION
        defaultFillingGapsTestItemShouldNotBeFound("question.in=" + UPDATED_QUESTION);
    }

    @Test
    @Transactional
    public void getAllFillingGapsTestItemsByQuestionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);

        // Get all the fillingGapsTestItemList where question is not null
        defaultFillingGapsTestItemShouldBeFound("question.specified=true");

        // Get all the fillingGapsTestItemList where question is null
        defaultFillingGapsTestItemShouldNotBeFound("question.specified=false");
    }

    @Test
    @Transactional
    public void getAllFillingGapsTestItemsByGapItemIsEqualToSomething() throws Exception {
        // Initialize the database
        GapItem gapItem = GapItemResourceIntTest.createEntity(em);
        em.persist(gapItem);
        em.flush();
        fillingGapsTestItem.addGapItem(gapItem);
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);
        Long gapItemId = gapItem.getId();

        // Get all the fillingGapsTestItemList where gapItem equals to gapItemId
        defaultFillingGapsTestItemShouldBeFound("gapItemId.equals=" + gapItemId);

        // Get all the fillingGapsTestItemList where gapItem equals to gapItemId + 1
        defaultFillingGapsTestItemShouldNotBeFound("gapItemId.equals=" + (gapItemId + 1));
    }


    @Test
    @Transactional
    public void getAllFillingGapsTestItemsByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        Tag tag = TagResourceIntTest.createEntity(em);
        em.persist(tag);
        em.flush();
        fillingGapsTestItem.addTag(tag);
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);
        Long tagId = tag.getId();

        // Get all the fillingGapsTestItemList where tag equals to tagId
        defaultFillingGapsTestItemShouldBeFound("tagId.equals=" + tagId);

        // Get all the fillingGapsTestItemList where tag equals to tagId + 1
        defaultFillingGapsTestItemShouldNotBeFound("tagId.equals=" + (tagId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFillingGapsTestItemShouldBeFound(String filter) throws Exception {
        restFillingGapsTestItemMockMvc.perform(get("/api/filling-gaps-test-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fillingGapsTestItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFillingGapsTestItemShouldNotBeFound(String filter) throws Exception {
        restFillingGapsTestItemMockMvc.perform(get("/api/filling-gaps-test-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingFillingGapsTestItem() throws Exception {
        // Get the fillingGapsTestItem
        restFillingGapsTestItemMockMvc.perform(get("/api/filling-gaps-test-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFillingGapsTestItem() throws Exception {
        // Initialize the database
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);

        int databaseSizeBeforeUpdate = fillingGapsTestItemRepository.findAll().size();

        // Update the fillingGapsTestItem
        FillingGapsTestItem updatedFillingGapsTestItem = fillingGapsTestItemRepository.findById(fillingGapsTestItem.getId()).get();
        // Disconnect from session so that the updates on updatedFillingGapsTestItem are not directly saved in db
        em.detach(updatedFillingGapsTestItem);
        updatedFillingGapsTestItem
            .question(UPDATED_QUESTION);
        FillingGapsTestItemDTO fillingGapsTestItemDTO = fillingGapsTestItemMapper.toDto(updatedFillingGapsTestItem);

        restFillingGapsTestItemMockMvc.perform(put("/api/filling-gaps-test-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fillingGapsTestItemDTO)))
            .andExpect(status().isOk());

        // Validate the FillingGapsTestItem in the database
        List<FillingGapsTestItem> fillingGapsTestItemList = fillingGapsTestItemRepository.findAll();
        assertThat(fillingGapsTestItemList).hasSize(databaseSizeBeforeUpdate);
        FillingGapsTestItem testFillingGapsTestItem = fillingGapsTestItemList.get(fillingGapsTestItemList.size() - 1);
        assertThat(testFillingGapsTestItem.getQuestion()).isEqualTo(UPDATED_QUESTION);
    }

    @Test
    @Transactional
    public void updateNonExistingFillingGapsTestItem() throws Exception {
        int databaseSizeBeforeUpdate = fillingGapsTestItemRepository.findAll().size();

        // Create the FillingGapsTestItem
        FillingGapsTestItemDTO fillingGapsTestItemDTO = fillingGapsTestItemMapper.toDto(fillingGapsTestItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFillingGapsTestItemMockMvc.perform(put("/api/filling-gaps-test-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fillingGapsTestItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FillingGapsTestItem in the database
        List<FillingGapsTestItem> fillingGapsTestItemList = fillingGapsTestItemRepository.findAll();
        assertThat(fillingGapsTestItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFillingGapsTestItem() throws Exception {
        // Initialize the database
        fillingGapsTestItemRepository.saveAndFlush(fillingGapsTestItem);

        int databaseSizeBeforeDelete = fillingGapsTestItemRepository.findAll().size();

        // Get the fillingGapsTestItem
        restFillingGapsTestItemMockMvc.perform(delete("/api/filling-gaps-test-items/{id}", fillingGapsTestItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FillingGapsTestItem> fillingGapsTestItemList = fillingGapsTestItemRepository.findAll();
        assertThat(fillingGapsTestItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FillingGapsTestItem.class);
        FillingGapsTestItem fillingGapsTestItem1 = new FillingGapsTestItem();
        fillingGapsTestItem1.setId(1L);
        FillingGapsTestItem fillingGapsTestItem2 = new FillingGapsTestItem();
        fillingGapsTestItem2.setId(fillingGapsTestItem1.getId());
        assertThat(fillingGapsTestItem1).isEqualTo(fillingGapsTestItem2);
        fillingGapsTestItem2.setId(2L);
        assertThat(fillingGapsTestItem1).isNotEqualTo(fillingGapsTestItem2);
        fillingGapsTestItem1.setId(null);
        assertThat(fillingGapsTestItem1).isNotEqualTo(fillingGapsTestItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FillingGapsTestItemDTO.class);
        FillingGapsTestItemDTO fillingGapsTestItemDTO1 = new FillingGapsTestItemDTO();
        fillingGapsTestItemDTO1.setId(1L);
        FillingGapsTestItemDTO fillingGapsTestItemDTO2 = new FillingGapsTestItemDTO();
        assertThat(fillingGapsTestItemDTO1).isNotEqualTo(fillingGapsTestItemDTO2);
        fillingGapsTestItemDTO2.setId(fillingGapsTestItemDTO1.getId());
        assertThat(fillingGapsTestItemDTO1).isEqualTo(fillingGapsTestItemDTO2);
        fillingGapsTestItemDTO2.setId(2L);
        assertThat(fillingGapsTestItemDTO1).isNotEqualTo(fillingGapsTestItemDTO2);
        fillingGapsTestItemDTO1.setId(null);
        assertThat(fillingGapsTestItemDTO1).isNotEqualTo(fillingGapsTestItemDTO2);
    }
}
