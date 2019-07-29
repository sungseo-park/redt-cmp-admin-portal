package com.cmp.adminportal.web.rest

import com.cmp.adminportal.CmpAdminPortalApp
import com.cmp.adminportal.config.TestSecurityConfiguration
import com.cmp.adminportal.domain.StarGate
import com.cmp.adminportal.repository.StarGateRepository
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

/**
 * Test class for the StarGateResource REST controller.
 *
 * @see StarGateResource
 */
@SpringBootTest(classes = [CmpAdminPortalApp::class, TestSecurityConfiguration::class])
class StarGateResourceIT {

    @Autowired
    private lateinit var starGateRepository: StarGateRepository

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

    private lateinit var restStarGateMockMvc: MockMvc

    private lateinit var starGate: StarGate

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val starGateResource = StarGateResource(starGateRepository)
        this.restStarGateMockMvc = MockMvcBuilders.standaloneSetup(starGateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        starGate = createEntity(em)
    }

    @Test
    @Transactional
    fun createStarGate() {
        val databaseSizeBeforeCreate = starGateRepository.findAll().size

        // Create the StarGate
        restStarGateMockMvc.perform(
            post("/api/star-gates")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(starGate))
        ).andExpect(status().isCreated)

        // Validate the StarGate in the database
        val starGateList = starGateRepository.findAll()
        assertThat(starGateList).hasSize(databaseSizeBeforeCreate + 1)
        val testStarGate = starGateList[starGateList.size - 1]
        assertThat(testStarGate.role).isEqualTo(DEFAULT_ROLE)
    }

    @Test
    @Transactional
    fun createStarGateWithExistingId() {
        val databaseSizeBeforeCreate = starGateRepository.findAll().size

        // Create the StarGate with an existing ID
        starGate.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restStarGateMockMvc.perform(
            post("/api/star-gates")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(starGate))
        ).andExpect(status().isBadRequest)

        // Validate the StarGate in the database
        val starGateList = starGateRepository.findAll()
        assertThat(starGateList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    fun getAllStarGates() {
        // Initialize the database
        starGateRepository.saveAndFlush(starGate)

        // Get all the starGateList
        restStarGateMockMvc.perform(get("/api/star-gates?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(starGate.id?.toInt())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
    }

    @Test
    @Transactional
    fun getStarGate() {
        // Initialize the database
        starGateRepository.saveAndFlush(starGate)

        val id = starGate.id
        assertNotNull(id)

        // Get the starGate
        restStarGateMockMvc.perform(get("/api/star-gates/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
    }

    @Test
    @Transactional
    fun getNonExistingStarGate() {
        // Get the starGate
        restStarGateMockMvc.perform(get("/api/star-gates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateStarGate() {
        // Initialize the database
        starGateRepository.saveAndFlush(starGate)

        val databaseSizeBeforeUpdate = starGateRepository.findAll().size

        // Update the starGate
        val id = starGate.id
        assertNotNull(id)
        val updatedStarGate = starGateRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedStarGate are not directly saved in db
        em.detach(updatedStarGate)
        updatedStarGate.role = UPDATED_ROLE

        restStarGateMockMvc.perform(
            put("/api/star-gates")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedStarGate))
        ).andExpect(status().isOk)

        // Validate the StarGate in the database
        val starGateList = starGateRepository.findAll()
        assertThat(starGateList).hasSize(databaseSizeBeforeUpdate)
        val testStarGate = starGateList[starGateList.size - 1]
        assertThat(testStarGate.role).isEqualTo(UPDATED_ROLE)
    }

    @Test
    @Transactional
    fun updateNonExistingStarGate() {
        val databaseSizeBeforeUpdate = starGateRepository.findAll().size

        // Create the StarGate

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStarGateMockMvc.perform(
            put("/api/star-gates")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(starGate))
        ).andExpect(status().isBadRequest)

        // Validate the StarGate in the database
        val starGateList = starGateRepository.findAll()
        assertThat(starGateList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteStarGate() {
        // Initialize the database
        starGateRepository.saveAndFlush(starGate)

        val databaseSizeBeforeDelete = starGateRepository.findAll().size

        val id = starGate.id
        assertNotNull(id)

        // Delete the starGate
        restStarGateMockMvc.perform(
            delete("/api/star-gates/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val starGateList = starGateRepository.findAll()
        assertThat(starGateList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(StarGate::class)
        val starGate1 = StarGate()
        starGate1.id = 1L
        val starGate2 = StarGate()
        starGate2.id = starGate1.id
        assertThat(starGate1).isEqualTo(starGate2)
        starGate2.id = 2L
        assertThat(starGate1).isNotEqualTo(starGate2)
        starGate1.id = null
        assertThat(starGate1).isNotEqualTo(starGate2)
    }

    companion object {

        private const val DEFAULT_ROLE: String = "AAAAAAAAAA"
        private const val UPDATED_ROLE = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): StarGate {
            val starGate = StarGate(
                role = DEFAULT_ROLE
            )

            return starGate
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): StarGate {
            val starGate = StarGate(
                role = UPDATED_ROLE
            )

            return starGate
        }
    }
}
