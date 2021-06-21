package com.meetingplanner.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meetingplanner.IntegrationTest;
import com.meetingplanner.domain.EquipementSalle;
import com.meetingplanner.domain.enumeration.TypeEquipement;
import com.meetingplanner.repository.EquipementSalleRepository;
import com.meetingplanner.service.dto.EquipementSalleDTO;
import com.meetingplanner.service.mapper.EquipementSalleMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EquipementSalleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquipementSalleResourceIT {

    private static final TypeEquipement DEFAULT_TYPE = TypeEquipement.PIEUVRE;
    private static final TypeEquipement UPDATED_TYPE = TypeEquipement.ECRAN;

    private static final String ENTITY_API_URL = "/api/equipement-salles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquipementSalleRepository equipementSalleRepository;

    @Autowired
    private EquipementSalleMapper equipementSalleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipementSalleMockMvc;

    private EquipementSalle equipementSalle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipementSalle createEntity(EntityManager em) {
        EquipementSalle equipementSalle = new EquipementSalle().type(DEFAULT_TYPE);
        return equipementSalle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipementSalle createUpdatedEntity(EntityManager em) {
        EquipementSalle equipementSalle = new EquipementSalle().type(UPDATED_TYPE);
        return equipementSalle;
    }

    @BeforeEach
    public void initTest() {
        equipementSalle = createEntity(em);
    }

    @Test
    @Transactional
    void createEquipementSalle() throws Exception {
        int databaseSizeBeforeCreate = equipementSalleRepository.findAll().size();
        // Create the EquipementSalle
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(equipementSalle);
        restEquipementSalleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeCreate + 1);
        EquipementSalle testEquipementSalle = equipementSalleList.get(equipementSalleList.size() - 1);
        assertThat(testEquipementSalle.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createEquipementSalleWithExistingId() throws Exception {
        // Create the EquipementSalle with an existing ID
        equipementSalle.setId(1L);
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(equipementSalle);

        int databaseSizeBeforeCreate = equipementSalleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipementSalleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEquipementSalles() throws Exception {
        // Initialize the database
        equipementSalleRepository.saveAndFlush(equipementSalle);

        // Get all the equipementSalleList
        restEquipementSalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipementSalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getEquipementSalle() throws Exception {
        // Initialize the database
        equipementSalleRepository.saveAndFlush(equipementSalle);

        // Get the equipementSalle
        restEquipementSalleMockMvc
            .perform(get(ENTITY_API_URL_ID, equipementSalle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipementSalle.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEquipementSalle() throws Exception {
        // Get the equipementSalle
        restEquipementSalleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEquipementSalle() throws Exception {
        // Initialize the database
        equipementSalleRepository.saveAndFlush(equipementSalle);

        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();

        // Update the equipementSalle
        EquipementSalle updatedEquipementSalle = equipementSalleRepository.findById(equipementSalle.getId()).get();
        // Disconnect from session so that the updates on updatedEquipementSalle are not directly saved in db
        em.detach(updatedEquipementSalle);
        updatedEquipementSalle.type(UPDATED_TYPE);
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(updatedEquipementSalle);

        restEquipementSalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipementSalleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isOk());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
        EquipementSalle testEquipementSalle = equipementSalleList.get(equipementSalleList.size() - 1);
        assertThat(testEquipementSalle.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingEquipementSalle() throws Exception {
        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();
        equipementSalle.setId(count.incrementAndGet());

        // Create the EquipementSalle
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(equipementSalle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementSalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipementSalleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipementSalle() throws Exception {
        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();
        equipementSalle.setId(count.incrementAndGet());

        // Create the EquipementSalle
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(equipementSalle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementSalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipementSalle() throws Exception {
        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();
        equipementSalle.setId(count.incrementAndGet());

        // Create the EquipementSalle
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(equipementSalle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementSalleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipementSalleWithPatch() throws Exception {
        // Initialize the database
        equipementSalleRepository.saveAndFlush(equipementSalle);

        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();

        // Update the equipementSalle using partial update
        EquipementSalle partialUpdatedEquipementSalle = new EquipementSalle();
        partialUpdatedEquipementSalle.setId(equipementSalle.getId());

        restEquipementSalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipementSalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipementSalle))
            )
            .andExpect(status().isOk());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
        EquipementSalle testEquipementSalle = equipementSalleList.get(equipementSalleList.size() - 1);
        assertThat(testEquipementSalle.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateEquipementSalleWithPatch() throws Exception {
        // Initialize the database
        equipementSalleRepository.saveAndFlush(equipementSalle);

        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();

        // Update the equipementSalle using partial update
        EquipementSalle partialUpdatedEquipementSalle = new EquipementSalle();
        partialUpdatedEquipementSalle.setId(equipementSalle.getId());

        partialUpdatedEquipementSalle.type(UPDATED_TYPE);

        restEquipementSalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipementSalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipementSalle))
            )
            .andExpect(status().isOk());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
        EquipementSalle testEquipementSalle = equipementSalleList.get(equipementSalleList.size() - 1);
        assertThat(testEquipementSalle.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingEquipementSalle() throws Exception {
        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();
        equipementSalle.setId(count.incrementAndGet());

        // Create the EquipementSalle
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(equipementSalle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementSalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipementSalleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipementSalle() throws Exception {
        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();
        equipementSalle.setId(count.incrementAndGet());

        // Create the EquipementSalle
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(equipementSalle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementSalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipementSalle() throws Exception {
        int databaseSizeBeforeUpdate = equipementSalleRepository.findAll().size();
        equipementSalle.setId(count.incrementAndGet());

        // Create the EquipementSalle
        EquipementSalleDTO equipementSalleDTO = equipementSalleMapper.toDto(equipementSalle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementSalleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipementSalleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EquipementSalle in the database
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipementSalle() throws Exception {
        // Initialize the database
        equipementSalleRepository.saveAndFlush(equipementSalle);

        int databaseSizeBeforeDelete = equipementSalleRepository.findAll().size();

        // Delete the equipementSalle
        restEquipementSalleMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipementSalle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipementSalle> equipementSalleList = equipementSalleRepository.findAll();
        assertThat(equipementSalleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
