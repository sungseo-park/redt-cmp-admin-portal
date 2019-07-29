package com.cmp.adminportal.web.rest

import com.cmp.adminportal.CmpAdminPortalApp
import com.cmp.adminportal.config.TestSecurityConfiguration
import com.cmp.adminportal.domain.HonestBuilding
import com.cmp.adminportal.repository.HonestBuildingRepository
import com.cmp.adminportal.web.rest.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.cmp.adminportal.domain.enumeration.Access

/**
 * Test class for the HonestBuildingResource REST controller.
 *
 * @see HonestBuildingResource
 */
@SpringBootTest(classes = [CmpAdminPortalApp::class, TestSecurityConfiguration::class])
class HonestBuildingResourceIT {

    @Autowired
    private lateinit var honestBuildingRepository: HonestBuildingRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restHonestBuildingMockMvc: MockMvc

    private lateinit var honestBuilding: HonestBuilding

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val honestBuildingResource = HonestBuildingResource(honestBuildingRepository)
        this.restHonestBuildingMockMvc = MockMvcBuilders.standaloneSetup(honestBuildingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        honestBuilding = createEntity(em)
    }

    @Test
    @Transactional
    fun createHonestBuilding() {
        val databaseSizeBeforeCreate = honestBuildingRepository.findAll().size

        // Create the HonestBuilding
        restHonestBuildingMockMvc.perform(
            post("/api/honest-buildings")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(honestBuilding))
        ).andExpect(status().isCreated)

        // Validate the HonestBuilding in the database
        val honestBuildingList = honestBuildingRepository.findAll()
        assertThat(honestBuildingList).hasSize(databaseSizeBeforeCreate + 1)
        val testHonestBuilding = honestBuildingList[honestBuildingList.size - 1]
        assertThat(testHonestBuilding.role).isEqualTo(DEFAULT_ROLE)
        assertThat(testHonestBuilding.access).isEqualTo(DEFAULT_ACCESS)
    }

    @Test
    @Transactional
    fun createHonestBuildingWithExistingId() {
        val databaseSizeBeforeCreate = honestBuildingRepository.findAll().size

        // Create the HonestBuilding with an existing ID
        honestBuilding.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restHonestBuildingMockMvc.perform(
            post("/api/honest-buildings")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(honestBuilding))
        ).andExpect(status().isBadRequest)

        // Validate the HonestBuilding in the database
        val honestBuildingList = honestBuildingRepository.findAll()
        assertThat(honestBuildingList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    fun getAllHonestBuildings() {
        // Initialize the database
        honestBuildingRepository.saveAndFlush(honestBuilding)

        // Get all the honestBuildingList
        restHonestBuildingMockMvc.perform(get("/api/honest-buildings?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(honestBuilding.id?.toInt())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].access").value(hasItem(DEFAULT_ACCESS.toString())))
    }

    @Test
    @Transactional
    fun getHonestBuilding() {
        // Initialize the database
        honestBuildingRepository.saveAndFlush(honestBuilding)

        val id = honestBuilding.id
        assertNotNull(id)

        // Get the honestBuilding
        restHonestBuildingMockMvc.perform(get("/api/honest-buildings/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.access").value(DEFAULT_ACCESS.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingHonestBuilding() {
        // Get the honestBuilding
        restHonestBuildingMockMvc.perform(get("/api/honest-buildings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateHonestBuilding() {
        // Initialize the database
        honestBuildingRepository.saveAndFlush(honestBuilding)

        val databaseSizeBeforeUpdate = honestBuildingRepository.findAll().size

        // Update the honestBuilding
        val id = honestBuilding.id
        assertNotNull(id)
        val updatedHonestBuilding = honestBuildingRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedHonestBuilding are not directly saved in db
        em.detach(updatedHonestBuilding)
        updatedHonestBuilding.role = UPDATED_ROLE
        updatedHonestBuilding.access = UPDATED_ACCESS

        restHonestBuildingMockMvc.perform(
            put("/api/honest-buildings")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedHonestBuilding))
        ).andExpect(status().isOk)

        // Validate the HonestBuilding in the database
        val honestBuildingList = honestBuildingRepository.findAll()
        assertThat(honestBuildingList).hasSize(databaseSizeBeforeUpdate)
        val testHonestBuilding = honestBuildingList[honestBuildingList.size - 1]
        assertThat(testHonestBuilding.role).isEqualTo(UPDATED_ROLE)
        assertThat(testHonestBuilding.access).isEqualTo(UPDATED_ACCESS)
    }

    @Test
    @Transactional
    fun updateNonExistingHonestBuilding() {
        val databaseSizeBeforeUpdate = honestBuildingRepository.findAll().size

        // Create the HonestBuilding

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHonestBuildingMockMvc.perform(
            put("/api/honest-buildings")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(honestBuilding))
        ).andExpect(status().isBadRequest)

        // Validate the HonestBuilding in the database
        val honestBuildingList = honestBuildingRepository.findAll()
        assertThat(honestBuildingList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteHonestBuilding() {
        // Initialize the database
        honestBuildingRepository.saveAndFlush(honestBuilding)

        val databaseSizeBeforeDelete = honestBuildingRepository.findAll().size

        val id = honestBuilding.id
        assertNotNull(id)

        // Delete the honestBuilding
        restHonestBuildingMockMvc.perform(
            delete("/api/honest-buildings/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val honestBuildingList = honestBuildingRepository.findAll()
        assertThat(honestBuildingList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(HonestBuilding::class)
        val honestBuilding1 = HonestBuilding()
        honestBuilding1.id = 1L
        val honestBuilding2 = HonestBuilding()
        honestBuilding2.id = honestBuilding1.id
        assertThat(honestBuilding1).isEqualTo(honestBuilding2)
        honestBuilding2.id = 2L
        assertThat(honestBuilding1).isNotEqualTo(honestBuilding2)
        honestBuilding1.id = null
        assertThat(honestBuilding1).isNotEqualTo(honestBuilding2)
    }

    companion object {

        private const val DEFAULT_ROLE: String = "AAAAAAAAAA"
        private const val UPDATED_ROLE = "BBBBBBBBBB"

        private val DEFAULT_ACCESS: Access = Access.FULL
        private val UPDATED_ACCESS: Access = Access.LIMITED

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): HonestBuilding {
            val honestBuilding = HonestBuilding(
                role = DEFAULT_ROLE,
                access = DEFAULT_ACCESS
            )

            return honestBuilding
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): HonestBuilding {
            val honestBuilding = HonestBuilding(
                role = UPDATED_ROLE,
                access = UPDATED_ACCESS
            )

            return honestBuilding
        }
    }
}
