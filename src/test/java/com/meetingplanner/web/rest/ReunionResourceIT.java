package com.meetingplanner.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.meetingplanner.IntegrationTest;
import com.meetingplanner.domain.Reunion;
import com.meetingplanner.domain.enumeration.Creneau;
import com.meetingplanner.domain.enumeration.TypeReunion;
import com.meetingplanner.repository.ReunionRepository;
import com.meetingplanner.service.dto.ReunionDTO;
import com.meetingplanner.service.mapper.ReunionMapper;
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
 * Integration tests for the {@link ReunionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReunionResourceIT {

    private static final TypeReunion DEFAULT_TYPE = TypeReunion.VC;
    private static final TypeReunion UPDATED_TYPE = TypeReunion.SPEC;

    private static final Creneau DEFAULT_CRENEAU = Creneau.C1;
    private static final Creneau UPDATED_CRENEAU = Creneau.C2;

    private static final String ENTITY_API_URL = "/api/reunions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReunionRepository reunionRepository;

    @Autowired
    private ReunionMapper reunionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReunionMockMvc;

    private Reunion reunion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reunion createEntity(EntityManager em) {
        Reunion reunion = new Reunion().type(DEFAULT_TYPE).creneau(DEFAULT_CRENEAU);
        return reunion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reunion createUpdatedEntity(EntityManager em) {
        Reunion reunion = new Reunion().type(UPDATED_TYPE).creneau(UPDATED_CRENEAU);
        return reunion;
    }

    @BeforeEach
    public void initTest() {
        reunion = createEntity(em);
    }

    @Test
    @Transactional
    void createReunion() throws Exception {
        int databaseSizeBeforeCreate = reunionRepository.findAll().size();
        // Create the Reunion
        ReunionDTO reunionDTO = reunionMapper.toDto(reunion);
        restReunionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reunionDTO)))
            .andExpect(status().isCreated());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeCreate + 1);
        Reunion testReunion = reunionList.get(reunionList.size() - 1);
        assertThat(testReunion.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testReunion.getCreneau()).isEqualTo(DEFAULT_CRENEAU);
    }

    @Test
    @Transactional
    void createReunionWithExistingId() throws Exception {
        // Create the Reunion with an existing ID
        reunion.setId(1L);
        ReunionDTO reunionDTO = reunionMapper.toDto(reunion);

        int databaseSizeBeforeCreate = reunionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReunionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reunionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReunions() throws Exception {
        // Initialize the database
        reunionRepository.saveAndFlush(reunion);

        // Get all the reunionList
        restReunionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reunion.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].creneau").value(hasItem(DEFAULT_CRENEAU.toString())));
    }

    @Test
    @Transactional
    void getReunion() throws Exception {
        // Initialize the database
        reunionRepository.saveAndFlush(reunion);

        // Get the reunion
        restReunionMockMvc
            .perform(get(ENTITY_API_URL_ID, reunion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reunion.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.creneau").value(DEFAULT_CRENEAU.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReunion() throws Exception {
        // Get the reunion
        restReunionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReunion() throws Exception {
        // Initialize the database
        reunionRepository.saveAndFlush(reunion);

        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();

        // Update the reunion
        Reunion updatedReunion = reunionRepository.findById(reunion.getId()).get();
        // Disconnect from session so that the updates on updatedReunion are not directly saved in db
        em.detach(updatedReunion);
        updatedReunion.type(UPDATED_TYPE).creneau(UPDATED_CRENEAU);
        ReunionDTO reunionDTO = reunionMapper.toDto(updatedReunion);

        restReunionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reunionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reunionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
        Reunion testReunion = reunionList.get(reunionList.size() - 1);
        assertThat(testReunion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testReunion.getCreneau()).isEqualTo(UPDATED_CRENEAU);
    }

    @Test
    @Transactional
    void putNonExistingReunion() throws Exception {
        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();
        reunion.setId(count.incrementAndGet());

        // Create the Reunion
        ReunionDTO reunionDTO = reunionMapper.toDto(reunion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReunionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reunionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reunionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReunion() throws Exception {
        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();
        reunion.setId(count.incrementAndGet());

        // Create the Reunion
        ReunionDTO reunionDTO = reunionMapper.toDto(reunion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReunionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reunionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReunion() throws Exception {
        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();
        reunion.setId(count.incrementAndGet());

        // Create the Reunion
        ReunionDTO reunionDTO = reunionMapper.toDto(reunion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReunionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reunionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReunionWithPatch() throws Exception {
        // Initialize the database
        reunionRepository.saveAndFlush(reunion);

        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();

        // Update the reunion using partial update
        Reunion partialUpdatedReunion = new Reunion();
        partialUpdatedReunion.setId(reunion.getId());

        partialUpdatedReunion.type(UPDATED_TYPE);

        restReunionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReunion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReunion))
            )
            .andExpect(status().isOk());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
        Reunion testReunion = reunionList.get(reunionList.size() - 1);
        assertThat(testReunion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testReunion.getCreneau()).isEqualTo(DEFAULT_CRENEAU);
    }

    @Test
    @Transactional
    void fullUpdateReunionWithPatch() throws Exception {
        // Initialize the database
        reunionRepository.saveAndFlush(reunion);

        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();

        // Update the reunion using partial update
        Reunion partialUpdatedReunion = new Reunion();
        partialUpdatedReunion.setId(reunion.getId());

        partialUpdatedReunion.type(UPDATED_TYPE).creneau(UPDATED_CRENEAU);

        restReunionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReunion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReunion))
            )
            .andExpect(status().isOk());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
        Reunion testReunion = reunionList.get(reunionList.size() - 1);
        assertThat(testReunion.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testReunion.getCreneau()).isEqualTo(UPDATED_CRENEAU);
    }

    @Test
    @Transactional
    void patchNonExistingReunion() throws Exception {
        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();
        reunion.setId(count.incrementAndGet());

        // Create the Reunion
        ReunionDTO reunionDTO = reunionMapper.toDto(reunion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReunionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reunionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reunionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReunion() throws Exception {
        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();
        reunion.setId(count.incrementAndGet());

        // Create the Reunion
        ReunionDTO reunionDTO = reunionMapper.toDto(reunion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReunionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reunionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReunion() throws Exception {
        int databaseSizeBeforeUpdate = reunionRepository.findAll().size();
        reunion.setId(count.incrementAndGet());

        // Create the Reunion
        ReunionDTO reunionDTO = reunionMapper.toDto(reunion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReunionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reunionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reunion in the database
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReunion() throws Exception {
        // Initialize the database
        reunionRepository.saveAndFlush(reunion);

        int databaseSizeBeforeDelete = reunionRepository.findAll().size();

        // Delete the reunion
        restReunionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reunion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reunion> reunionList = reunionRepository.findAll();
        assertThat(reunionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
