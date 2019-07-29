package com.cmp.adminportal.web.rest

import com.cmp.adminportal.CmpAdminPortalApp
import com.cmp.adminportal.config.TestSecurityConfiguration
import com.cmp.adminportal.domain.CostManagementPlatform
import com.cmp.adminportal.repository.CostManagementPlatformRepository
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
 * Test class for the CostManagementPlatformResource REST controller.
 *
 * @see CostManagementPlatformResource
 */
@SpringBootTest(classes = [CmpAdminPortalApp::class, TestSecurityConfiguration::class])
class CostManagementPlatformResourceIT {

    @Autowired
    private lateinit var costManagementPlatformRepository: CostManagementPlatformRepository

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

    private lateinit var restCostManagementPlatformMockMvc: MockMvc

    private lateinit var costManagementPlatform: CostManagementPlatform

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val costManagementPlatformResource = CostManagementPlatformResource(costManagementPlatformRepository)
        this.restCostManagementPlatformMockMvc = MockMvcBuilders.standaloneSetup(costManagementPlatformResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        costManagementPlatform = createEntity(em)
    }

    @Test
    @Transactional
    fun createCostManagementPlatform() {
        val databaseSizeBeforeCreate = costManagementPlatformRepository.findAll().size

        // Create the CostManagementPlatform
        restCostManagementPlatformMockMvc.perform(
            post("/api/cost-management-platforms")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(costManagementPlatform))
        ).andExpect(status().isCreated)

        // Validate the CostManagementPlatform in the database
        val costManagementPlatformList = costManagementPlatformRepository.findAll()
        assertThat(costManagementPlatformList).hasSize(databaseSizeBeforeCreate + 1)
        val testCostManagementPlatform = costManagementPlatformList[costManagementPlatformList.size - 1]
        assertThat(testCostManagementPlatform.role).isEqualTo(DEFAULT_ROLE)
        assertThat(testCostManagementPlatform.access).isEqualTo(DEFAULT_ACCESS)
    }

    @Test
    @Transactional
    fun createCostManagementPlatformWithExistingId() {
        val databaseSizeBeforeCreate = costManagementPlatformRepository.findAll().size

        // Create the CostManagementPlatform with an existing ID
        costManagementPlatform.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostManagementPlatformMockMvc.perform(
            post("/api/cost-management-platforms")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(costManagementPlatform))
        ).andExpect(status().isBadRequest)

        // Validate the CostManagementPlatform in the database
        val costManagementPlatformList = costManagementPlatformRepository.findAll()
        assertThat(costManagementPlatformList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    fun getAllCostManagementPlatforms() {
        // Initialize the database
        costManagementPlatformRepository.saveAndFlush(costManagementPlatform)

        // Get all the costManagementPlatformList
        restCostManagementPlatformMockMvc.perform(get("/api/cost-management-platforms?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costManagementPlatform.id?.toInt())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].access").value(hasItem(DEFAULT_ACCESS.toString())))
    }

    @Test
    @Transactional
    fun getCostManagementPlatform() {
        // Initialize the database
        costManagementPlatformRepository.saveAndFlush(costManagementPlatform)

        val id = costManagementPlatform.id
        assertNotNull(id)

        // Get the costManagementPlatform
        restCostManagementPlatformMockMvc.perform(get("/api/cost-management-platforms/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.access").value(DEFAULT_ACCESS.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingCostManagementPlatform() {
        // Get the costManagementPlatform
        restCostManagementPlatformMockMvc.perform(get("/api/cost-management-platforms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateCostManagementPlatform() {
        // Initialize the database
        costManagementPlatformRepository.saveAndFlush(costManagementPlatform)

        val databaseSizeBeforeUpdate = costManagementPlatformRepository.findAll().size

        // Update the costManagementPlatform
        val id = costManagementPlatform.id
        assertNotNull(id)
        val updatedCostManagementPlatform = costManagementPlatformRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedCostManagementPlatform are not directly saved in db
        em.detach(updatedCostManagementPlatform)
        updatedCostManagementPlatform.role = UPDATED_ROLE
        updatedCostManagementPlatform.access = UPDATED_ACCESS

        restCostManagementPlatformMockMvc.perform(
            put("/api/cost-management-platforms")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedCostManagementPlatform))
        ).andExpect(status().isOk)

        // Validate the CostManagementPlatform in the database
        val costManagementPlatformList = costManagementPlatformRepository.findAll()
        assertThat(costManagementPlatformList).hasSize(databaseSizeBeforeUpdate)
        val testCostManagementPlatform = costManagementPlatformList[costManagementPlatformList.size - 1]
        assertThat(testCostManagementPlatform.role).isEqualTo(UPDATED_ROLE)
        assertThat(testCostManagementPlatform.access).isEqualTo(UPDATED_ACCESS)
    }

    @Test
    @Transactional
    fun updateNonExistingCostManagementPlatform() {
        val databaseSizeBeforeUpdate = costManagementPlatformRepository.findAll().size

        // Create the CostManagementPlatform

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostManagementPlatformMockMvc.perform(
            put("/api/cost-management-platforms")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(costManagementPlatform))
        ).andExpect(status().isBadRequest)

        // Validate the CostManagementPlatform in the database
        val costManagementPlatformList = costManagementPlatformRepository.findAll()
        assertThat(costManagementPlatformList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteCostManagementPlatform() {
        // Initialize the database
        costManagementPlatformRepository.saveAndFlush(costManagementPlatform)

        val databaseSizeBeforeDelete = costManagementPlatformRepository.findAll().size

        val id = costManagementPlatform.id
        assertNotNull(id)

        // Delete the costManagementPlatform
        restCostManagementPlatformMockMvc.perform(
            delete("/api/cost-management-platforms/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val costManagementPlatformList = costManagementPlatformRepository.findAll()
        assertThat(costManagementPlatformList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(CostManagementPlatform::class)
        val costManagementPlatform1 = CostManagementPlatform()
        costManagementPlatform1.id = 1L
        val costManagementPlatform2 = CostManagementPlatform()
        costManagementPlatform2.id = costManagementPlatform1.id
        assertThat(costManagementPlatform1).isEqualTo(costManagementPlatform2)
        costManagementPlatform2.id = 2L
        assertThat(costManagementPlatform1).isNotEqualTo(costManagementPlatform2)
        costManagementPlatform1.id = null
        assertThat(costManagementPlatform1).isNotEqualTo(costManagementPlatform2)
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
        fun createEntity(em: EntityManager): CostManagementPlatform {
            val costManagementPlatform = CostManagementPlatform(
                role = DEFAULT_ROLE,
                access = DEFAULT_ACCESS
            )

            return costManagementPlatform
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): CostManagementPlatform {
            val costManagementPlatform = CostManagementPlatform(
                role = UPDATED_ROLE,
                access = UPDATED_ACCESS
            )

            return costManagementPlatform
        }
    }
}
