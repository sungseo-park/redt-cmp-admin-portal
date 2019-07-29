package com.cmp.adminportal.web.rest

import com.cmp.adminportal.CmpAdminPortalApp
import com.cmp.adminportal.config.TestSecurityConfiguration
import com.cmp.adminportal.domain.HB
import com.cmp.adminportal.repository.HBRepository
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
 * Test class for the HBResource REST controller.
 *
 * @see HBResource
 */
@SpringBootTest(classes = [CmpAdminPortalApp::class, TestSecurityConfiguration::class])
class HBResourceIT {

    @Autowired
    private lateinit var hBRepository: HBRepository

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

    private lateinit var restHBMockMvc: MockMvc

    private lateinit var hB: HB

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val hBResource = HBResource(hBRepository)
        this.restHBMockMvc = MockMvcBuilders.standaloneSetup(hBResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        hB = createEntity(em)
    }

    @Test
    @Transactional
    fun createHB() {
        val databaseSizeBeforeCreate = hBRepository.findAll().size

        // Create the HB
        restHBMockMvc.perform(
            post("/api/hbs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(hB))
        ).andExpect(status().isCreated)

        // Validate the HB in the database
        val hBList = hBRepository.findAll()
        assertThat(hBList).hasSize(databaseSizeBeforeCreate + 1)
        val testHB = hBList[hBList.size - 1]
        assertThat(testHB.role).isEqualTo(DEFAULT_ROLE)
        assertThat(testHB.access).isEqualTo(DEFAULT_ACCESS)
    }

    @Test
    @Transactional
    fun createHBWithExistingId() {
        val databaseSizeBeforeCreate = hBRepository.findAll().size

        // Create the HB with an existing ID
        hB.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restHBMockMvc.perform(
            post("/api/hbs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(hB))
        ).andExpect(status().isBadRequest)

        // Validate the HB in the database
        val hBList = hBRepository.findAll()
        assertThat(hBList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    fun getAllHBS() {
        // Initialize the database
        hBRepository.saveAndFlush(hB)

        // Get all the hBList
        restHBMockMvc.perform(get("/api/hbs?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hB.id?.toInt())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].access").value(hasItem(DEFAULT_ACCESS.toString())))
    }

    @Test
    @Transactional
    fun getHB() {
        // Initialize the database
        hBRepository.saveAndFlush(hB)

        val id = hB.id
        assertNotNull(id)

        // Get the hB
        restHBMockMvc.perform(get("/api/hbs/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.access").value(DEFAULT_ACCESS.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingHB() {
        // Get the hB
        restHBMockMvc.perform(get("/api/hbs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateHB() {
        // Initialize the database
        hBRepository.saveAndFlush(hB)

        val databaseSizeBeforeUpdate = hBRepository.findAll().size

        // Update the hB
        val id = hB.id
        assertNotNull(id)
        val updatedHB = hBRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedHB are not directly saved in db
        em.detach(updatedHB)
        updatedHB.role = UPDATED_ROLE
        updatedHB.access = UPDATED_ACCESS

        restHBMockMvc.perform(
            put("/api/hbs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedHB))
        ).andExpect(status().isOk)

        // Validate the HB in the database
        val hBList = hBRepository.findAll()
        assertThat(hBList).hasSize(databaseSizeBeforeUpdate)
        val testHB = hBList[hBList.size - 1]
        assertThat(testHB.role).isEqualTo(UPDATED_ROLE)
        assertThat(testHB.access).isEqualTo(UPDATED_ACCESS)
    }

    @Test
    @Transactional
    fun updateNonExistingHB() {
        val databaseSizeBeforeUpdate = hBRepository.findAll().size

        // Create the HB

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHBMockMvc.perform(
            put("/api/hbs")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(hB))
        ).andExpect(status().isBadRequest)

        // Validate the HB in the database
        val hBList = hBRepository.findAll()
        assertThat(hBList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteHB() {
        // Initialize the database
        hBRepository.saveAndFlush(hB)

        val databaseSizeBeforeDelete = hBRepository.findAll().size

        val id = hB.id
        assertNotNull(id)

        // Delete the hB
        restHBMockMvc.perform(
            delete("/api/hbs/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val hBList = hBRepository.findAll()
        assertThat(hBList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(HB::class)
        val hB1 = HB()
        hB1.id = 1L
        val hB2 = HB()
        hB2.id = hB1.id
        assertThat(hB1).isEqualTo(hB2)
        hB2.id = 2L
        assertThat(hB1).isNotEqualTo(hB2)
        hB1.id = null
        assertThat(hB1).isNotEqualTo(hB2)
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
        fun createEntity(em: EntityManager): HB {
            val hB = HB(
                role = DEFAULT_ROLE,
                access = DEFAULT_ACCESS
            )

            return hB
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): HB {
            val hB = HB(
                role = UPDATED_ROLE,
                access = UPDATED_ACCESS
            )

            return hB
        }
    }
}
