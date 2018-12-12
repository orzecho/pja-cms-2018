package pl.edu.pja.nyan.web.rest;

import pl.edu.pja.nyan.NyanApp;

import pl.edu.pja.nyan.domain.GapItem;
import pl.edu.pja.nyan.domain.FillingGapsTestItem;
import pl.edu.pja.nyan.repository.GapItemRepository;
import pl.edu.pja.nyan.service.GapItemService;
import pl.edu.pja.nyan.service.dto.GapItemDTO;
import pl.edu.pja.nyan.service.mapper.GapItemMapper;
import pl.edu.pja.nyan.web.rest.errors.ExceptionTranslator;
import pl.edu.pja.nyan.service.dto.GapItemCriteria;
import pl.edu.pja.nyan.service.GapItemQueryService;

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
 * Test class for the GapItemResource REST controller.
 *
 * @see GapItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NyanApp.class)
public class GapItemResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private GapItemRepository gapItemRepository;


    @Autowired
    private GapItemMapper gapItemMapper;
    

    @Autowired
    private GapItemService gapItemService;

    @Autowired
    private GapItemQueryService gapItemQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGapItemMockMvc;

    private GapItem gapItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GapItemResource gapItemResource = new GapItemResource(gapItemService, gapItemQueryService);
        this.restGapItemMockMvc = MockMvcBuilders.standaloneSetup(gapItemResource)
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
    public static GapItem createEntity(EntityManager em) {
        GapItem gapItem = new GapItem()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return gapItem;
    }

    @Before
    public void initTest() {
        gapItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createGapItem() throws Exception {
        int databaseSizeBeforeCreate = gapItemRepository.findAll().size();

        // Create the GapItem
        GapItemDTO gapItemDTO = gapItemMapper.toDto(gapItem);
        restGapItemMockMvc.perform(post("/api/gap-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gapItemDTO)))
            .andExpect(status().isCreated());

        // Validate the GapItem in the database
        List<GapItem> gapItemList = gapItemRepository.findAll();
        assertThat(gapItemList).hasSize(databaseSizeBeforeCreate + 1);
        GapItem testGapItem = gapItemList.get(gapItemList.size() - 1);
        assertThat(testGapItem.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testGapItem.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createGapItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gapItemRepository.findAll().size();

        // Create the GapItem with an existing ID
        gapItem.setId(1L);
        GapItemDTO gapItemDTO = gapItemMapper.toDto(gapItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGapItemMockMvc.perform(post("/api/gap-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gapItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GapItem in the database
        List<GapItem> gapItemList = gapItemRepository.findAll();
        assertThat(gapItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = gapItemRepository.findAll().size();
        // set the field null
        gapItem.setKey(null);

        // Create the GapItem, which fails.
        GapItemDTO gapItemDTO = gapItemMapper.toDto(gapItem);

        restGapItemMockMvc.perform(post("/api/gap-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gapItemDTO)))
            .andExpect(status().isBadRequest());

        List<GapItem> gapItemList = gapItemRepository.findAll();
        assertThat(gapItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = gapItemRepository.findAll().size();
        // set the field null
        gapItem.setValue(null);

        // Create the GapItem, which fails.
        GapItemDTO gapItemDTO = gapItemMapper.toDto(gapItem);

        restGapItemMockMvc.perform(post("/api/gap-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gapItemDTO)))
            .andExpect(status().isBadRequest());

        List<GapItem> gapItemList = gapItemRepository.findAll();
        assertThat(gapItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGapItems() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        // Get all the gapItemList
        restGapItemMockMvc.perform(get("/api/gap-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gapItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }
    

    @Test
    @Transactional
    public void getGapItem() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        // Get the gapItem
        restGapItemMockMvc.perform(get("/api/gap-items/{id}", gapItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gapItem.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAllGapItemsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        // Get all the gapItemList where key equals to DEFAULT_KEY
        defaultGapItemShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the gapItemList where key equals to UPDATED_KEY
        defaultGapItemShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllGapItemsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        // Get all the gapItemList where key in DEFAULT_KEY or UPDATED_KEY
        defaultGapItemShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the gapItemList where key equals to UPDATED_KEY
        defaultGapItemShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllGapItemsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        // Get all the gapItemList where key is not null
        defaultGapItemShouldBeFound("key.specified=true");

        // Get all the gapItemList where key is null
        defaultGapItemShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    public void getAllGapItemsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        // Get all the gapItemList where value equals to DEFAULT_VALUE
        defaultGapItemShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the gapItemList where value equals to UPDATED_VALUE
        defaultGapItemShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllGapItemsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        // Get all the gapItemList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultGapItemShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the gapItemList where value equals to UPDATED_VALUE
        defaultGapItemShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllGapItemsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        // Get all the gapItemList where value is not null
        defaultGapItemShouldBeFound("value.specified=true");

        // Get all the gapItemList where value is null
        defaultGapItemShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllGapItemsByTestItemIsEqualToSomething() throws Exception {
        // Initialize the database
        FillingGapsTestItem testItem = FillingGapsTestItemResourceIntTest.createEntity(em);
        em.persist(testItem);
        em.flush();
        gapItem.setTestItem(testItem);
        gapItemRepository.saveAndFlush(gapItem);
        Long testItemId = testItem.getId();

        // Get all the gapItemList where testItem equals to testItemId
        defaultGapItemShouldBeFound("testItemId.equals=" + testItemId);

        // Get all the gapItemList where testItem equals to testItemId + 1
        defaultGapItemShouldNotBeFound("testItemId.equals=" + (testItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGapItemShouldBeFound(String filter) throws Exception {
        restGapItemMockMvc.perform(get("/api/gap-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gapItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGapItemShouldNotBeFound(String filter) throws Exception {
        restGapItemMockMvc.perform(get("/api/gap-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingGapItem() throws Exception {
        // Get the gapItem
        restGapItemMockMvc.perform(get("/api/gap-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGapItem() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        int databaseSizeBeforeUpdate = gapItemRepository.findAll().size();

        // Update the gapItem
        GapItem updatedGapItem = gapItemRepository.findById(gapItem.getId()).get();
        // Disconnect from session so that the updates on updatedGapItem are not directly saved in db
        em.detach(updatedGapItem);
        updatedGapItem
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);
        GapItemDTO gapItemDTO = gapItemMapper.toDto(updatedGapItem);

        restGapItemMockMvc.perform(put("/api/gap-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gapItemDTO)))
            .andExpect(status().isOk());

        // Validate the GapItem in the database
        List<GapItem> gapItemList = gapItemRepository.findAll();
        assertThat(gapItemList).hasSize(databaseSizeBeforeUpdate);
        GapItem testGapItem = gapItemList.get(gapItemList.size() - 1);
        assertThat(testGapItem.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testGapItem.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingGapItem() throws Exception {
        int databaseSizeBeforeUpdate = gapItemRepository.findAll().size();

        // Create the GapItem
        GapItemDTO gapItemDTO = gapItemMapper.toDto(gapItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restGapItemMockMvc.perform(put("/api/gap-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gapItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GapItem in the database
        List<GapItem> gapItemList = gapItemRepository.findAll();
        assertThat(gapItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGapItem() throws Exception {
        // Initialize the database
        gapItemRepository.saveAndFlush(gapItem);

        int databaseSizeBeforeDelete = gapItemRepository.findAll().size();

        // Get the gapItem
        restGapItemMockMvc.perform(delete("/api/gap-items/{id}", gapItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GapItem> gapItemList = gapItemRepository.findAll();
        assertThat(gapItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GapItem.class);
        GapItem gapItem1 = new GapItem();
        gapItem1.setId(1L);
        GapItem gapItem2 = new GapItem();
        gapItem2.setId(gapItem1.getId());
        assertThat(gapItem1).isEqualTo(gapItem2);
        gapItem2.setId(2L);
        assertThat(gapItem1).isNotEqualTo(gapItem2);
        gapItem1.setId(null);
        assertThat(gapItem1).isNotEqualTo(gapItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GapItemDTO.class);
        GapItemDTO gapItemDTO1 = new GapItemDTO();
        gapItemDTO1.setId(1L);
        GapItemDTO gapItemDTO2 = new GapItemDTO();
        assertThat(gapItemDTO1).isNotEqualTo(gapItemDTO2);
        gapItemDTO2.setId(gapItemDTO1.getId());
        assertThat(gapItemDTO1).isEqualTo(gapItemDTO2);
        gapItemDTO2.setId(2L);
        assertThat(gapItemDTO1).isNotEqualTo(gapItemDTO2);
        gapItemDTO1.setId(null);
        assertThat(gapItemDTO1).isNotEqualTo(gapItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(gapItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(gapItemMapper.fromId(null)).isNull();
    }
}
