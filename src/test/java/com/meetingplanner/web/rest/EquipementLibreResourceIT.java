package com.meetingplanner.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meetingplanner.IntegrationTest;
import com.meetingplanner.domain.EquipementLibre;
import com.meetingplanner.domain.enumeration.TypeEquipement;
import com.meetingplanner.repository.EquipementLibreRepository;
import com.meetingplanner.service.dto.EquipementLibreDTO;
import com.meetingplanner.service.mapper.EquipementLibreMapper;
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
 * Integration tests for the {@link EquipementLibreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquipementLibreResourceIT {

    private static final TypeEquipement DEFAULT_TYPE = TypeEquipement.PIEUVRE;
    private static final TypeEquipement UPDATED_TYPE = TypeEquipement.ECRAN;

    private static final Boolean DEFAULT_RESERVE = false;
    private static final Boolean UPDATED_RESERVE = true;

    private static final String ENTITY_API_URL = "/api/equipement-libres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquipementLibreRepository equipementLibreRepository;

    @Autowired
    private EquipementLibreMapper equipementLibreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipementLibreMockMvc;

    private EquipementLibre equipementLibre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipementLibre createEntity(EntityManager em) {
        EquipementLibre equipementLibre = new EquipementLibre().type(DEFAULT_TYPE).reserve(DEFAULT_RESERVE);
        return equipementLibre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipementLibre createUpdatedEntity(EntityManager em) {
        EquipementLibre equipementLibre = new EquipementLibre().type(UPDATED_TYPE).reserve(UPDATED_RESERVE);
        return equipementLibre;
    }

    @BeforeEach
    public void initTest() {
        equipementLibre = createEntity(em);
    }

    @Test
    @Transactional
    void createEquipementLibre() throws Exception {
        int databaseSizeBeforeCreate = equipementLibreRepository.findAll().size();
        // Create the EquipementLibre
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(equipementLibre);
        restEquipementLibreMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeCreate + 1);
        EquipementLibre testEquipementLibre = equipementLibreList.get(equipementLibreList.size() - 1);
        assertThat(testEquipementLibre.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEquipementLibre.getReserve()).isEqualTo(DEFAULT_RESERVE);
    }

    @Test
    @Transactional
    void createEquipementLibreWithExistingId() throws Exception {
        // Create the EquipementLibre with an existing ID
        equipementLibre.setId(1L);
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(equipementLibre);

        int databaseSizeBeforeCreate = equipementLibreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipementLibreMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEquipementLibres() throws Exception {
        // Initialize the database
        equipementLibreRepository.saveAndFlush(equipementLibre);

        // Get all the equipementLibreList
        restEquipementLibreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipementLibre.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].reserve").value(hasItem(DEFAULT_RESERVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEquipementLibre() throws Exception {
        // Initialize the database
        equipementLibreRepository.saveAndFlush(equipementLibre);

        // Get the equipementLibre
        restEquipementLibreMockMvc
            .perform(get(ENTITY_API_URL_ID, equipementLibre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipementLibre.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.reserve").value(DEFAULT_RESERVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEquipementLibre() throws Exception {
        // Get the equipementLibre
        restEquipementLibreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEquipementLibre() throws Exception {
        // Initialize the database
        equipementLibreRepository.saveAndFlush(equipementLibre);

        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();

        // Update the equipementLibre
        EquipementLibre updatedEquipementLibre = equipementLibreRepository.findById(equipementLibre.getId()).get();
        // Disconnect from session so that the updates on updatedEquipementLibre are not directly saved in db
        em.detach(updatedEquipementLibre);
        updatedEquipementLibre.type(UPDATED_TYPE).reserve(UPDATED_RESERVE);
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(updatedEquipementLibre);

        restEquipementLibreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipementLibreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isOk());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
        EquipementLibre testEquipementLibre = equipementLibreList.get(equipementLibreList.size() - 1);
        assertThat(testEquipementLibre.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEquipementLibre.getReserve()).isEqualTo(UPDATED_RESERVE);
    }

    @Test
    @Transactional
    void putNonExistingEquipementLibre() throws Exception {
        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();
        equipementLibre.setId(count.incrementAndGet());

        // Create the EquipementLibre
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(equipementLibre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementLibreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipementLibreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipementLibre() throws Exception {
        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();
        equipementLibre.setId(count.incrementAndGet());

        // Create the EquipementLibre
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(equipementLibre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementLibreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipementLibre() throws Exception {
        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();
        equipementLibre.setId(count.incrementAndGet());

        // Create the EquipementLibre
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(equipementLibre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementLibreMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipementLibreWithPatch() throws Exception {
        // Initialize the database
        equipementLibreRepository.saveAndFlush(equipementLibre);

        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();

        // Update the equipementLibre using partial update
        EquipementLibre partialUpdatedEquipementLibre = new EquipementLibre();
        partialUpdatedEquipementLibre.setId(equipementLibre.getId());

        partialUpdatedEquipementLibre.type(UPDATED_TYPE);

        restEquipementLibreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipementLibre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipementLibre))
            )
            .andExpect(status().isOk());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
        EquipementLibre testEquipementLibre = equipementLibreList.get(equipementLibreList.size() - 1);
        assertThat(testEquipementLibre.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEquipementLibre.getReserve()).isEqualTo(DEFAULT_RESERVE);
    }

    @Test
    @Transactional
    void fullUpdateEquipementLibreWithPatch() throws Exception {
        // Initialize the database
        equipementLibreRepository.saveAndFlush(equipementLibre);

        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();

        // Update the equipementLibre using partial update
        EquipementLibre partialUpdatedEquipementLibre = new EquipementLibre();
        partialUpdatedEquipementLibre.setId(equipementLibre.getId());

        partialUpdatedEquipementLibre.type(UPDATED_TYPE).reserve(UPDATED_RESERVE);

        restEquipementLibreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipementLibre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipementLibre))
            )
            .andExpect(status().isOk());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
        EquipementLibre testEquipementLibre = equipementLibreList.get(equipementLibreList.size() - 1);
        assertThat(testEquipementLibre.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEquipementLibre.getReserve()).isEqualTo(UPDATED_RESERVE);
    }

    @Test
    @Transactional
    void patchNonExistingEquipementLibre() throws Exception {
        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();
        equipementLibre.setId(count.incrementAndGet());

        // Create the EquipementLibre
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(equipementLibre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementLibreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipementLibreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipementLibre() throws Exception {
        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();
        equipementLibre.setId(count.incrementAndGet());

        // Create the EquipementLibre
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(equipementLibre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementLibreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipementLibre() throws Exception {
        int databaseSizeBeforeUpdate = equipementLibreRepository.findAll().size();
        equipementLibre.setId(count.incrementAndGet());

        // Create the EquipementLibre
        EquipementLibreDTO equipementLibreDTO = equipementLibreMapper.toDto(equipementLibre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementLibreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipementLibreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EquipementLibre in the database
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipementLibre() throws Exception {
        // Initialize the database
        equipementLibreRepository.saveAndFlush(equipementLibre);

        int databaseSizeBeforeDelete = equipementLibreRepository.findAll().size();

        // Delete the equipementLibre
        restEquipementLibreMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipementLibre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipementLibre> equipementLibreList = equipementLibreRepository.findAll();
        assertThat(equipementLibreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
