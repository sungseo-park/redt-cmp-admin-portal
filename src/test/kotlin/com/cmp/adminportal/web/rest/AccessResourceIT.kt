package com.cmp.adminportal.web.rest

import com.cmp.adminportal.CmpAdminPortalApp
import com.cmp.adminportal.config.TestSecurityConfiguration
import com.cmp.adminportal.domain.Access
import com.cmp.adminportal.repository.AccessRepository
import com.cmp.adminportal.service.AccessService
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
 * Test class for the AccessResource REST controller.
 *
 * @see AccessResource
 */
@SpringBootTest(classes = [CmpAdminPortalApp::class, TestSecurityConfiguration::class])
class AccessResourceIT {

    @Autowired
    private lateinit var accessRepository: AccessRepository

    @Autowired
    private lateinit var accessService: AccessService

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

    private lateinit var restAccessMockMvc: MockMvc

    private lateinit var access: Access

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val accessResource = AccessResource(accessService)
        this.restAccessMockMvc = MockMvcBuilders.standaloneSetup(accessResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        access = createEntity(em)
    }

    @Test
    @Transactional
    fun createAccess() {
        val databaseSizeBeforeCreate = accessRepository.findAll().size

        // Create the Access
        restAccessMockMvc.perform(
            post("/api/accesses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(access))
        ).andExpect(status().isCreated)

        // Validate the Access in the database
        val accessList = accessRepository.findAll()
        assertThat(accessList).hasSize(databaseSizeBeforeCreate + 1)
        val testAccess = accessList[accessList.size - 1]
        assertThat(testAccess.accessLevel).isEqualTo(DEFAULT_ACCESS_LEVEL)
    }

    @Test
    @Transactional
    fun createAccessWithExistingId() {
        val databaseSizeBeforeCreate = accessRepository.findAll().size

        // Create the Access with an existing ID
        access.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccessMockMvc.perform(
            post("/api/accesses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(access))
        ).andExpect(status().isBadRequest)

        // Validate the Access in the database
        val accessList = accessRepository.findAll()
        assertThat(accessList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    fun getAllAccesses() {
        // Initialize the database
        accessRepository.saveAndFlush(access)

        // Get all the accessList
        restAccessMockMvc.perform(get("/api/accesses?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(access.id?.toInt())))
            .andExpect(jsonPath("$.[*].accessLevel").value(hasItem(DEFAULT_ACCESS_LEVEL)))
    }

    @Test
    @Transactional
    fun getAccess() {
        // Initialize the database
        accessRepository.saveAndFlush(access)

        val id = access.id
        assertNotNull(id)

        // Get the access
        restAccessMockMvc.perform(get("/api/accesses/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.accessLevel").value(DEFAULT_ACCESS_LEVEL))
    }

    @Test
    @Transactional
    fun getNonExistingAccess() {
        // Get the access
        restAccessMockMvc.perform(get("/api/accesses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateAccess() {
        // Initialize the database
        accessService.save(access)

        val databaseSizeBeforeUpdate = accessRepository.findAll().size

        // Update the access
        val id = access.id
        assertNotNull(id)
        val updatedAccess = accessRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedAccess are not directly saved in db
        em.detach(updatedAccess)
        updatedAccess.accessLevel = UPDATED_ACCESS_LEVEL

        restAccessMockMvc.perform(
            put("/api/accesses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedAccess))
        ).andExpect(status().isOk)

        // Validate the Access in the database
        val accessList = accessRepository.findAll()
        assertThat(accessList).hasSize(databaseSizeBeforeUpdate)
        val testAccess = accessList[accessList.size - 1]
        assertThat(testAccess.accessLevel).isEqualTo(UPDATED_ACCESS_LEVEL)
    }

    @Test
    @Transactional
    fun updateNonExistingAccess() {
        val databaseSizeBeforeUpdate = accessRepository.findAll().size

        // Create the Access

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessMockMvc.perform(
            put("/api/accesses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(access))
        ).andExpect(status().isBadRequest)

        // Validate the Access in the database
        val accessList = accessRepository.findAll()
        assertThat(accessList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteAccess() {
        // Initialize the database
        accessService.save(access)

        val databaseSizeBeforeDelete = accessRepository.findAll().size

        val id = access.id
        assertNotNull(id)

        // Delete the access
        restAccessMockMvc.perform(
            delete("/api/accesses/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val accessList = accessRepository.findAll()
        assertThat(accessList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(Access::class)
        val access1 = Access()
        access1.id = 1L
        val access2 = Access()
        access2.id = access1.id
        assertThat(access1).isEqualTo(access2)
        access2.id = 2L
        assertThat(access1).isNotEqualTo(access2)
        access1.id = null
        assertThat(access1).isNotEqualTo(access2)
    }

    companion object {

        private const val DEFAULT_ACCESS_LEVEL: String = "AAAAAAAAAA"
        private const val UPDATED_ACCESS_LEVEL = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Access {
            val access = Access(
                accessLevel = DEFAULT_ACCESS_LEVEL
            )

            return access
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Access {
            val access = Access(
                accessLevel = UPDATED_ACCESS_LEVEL
            )

            return access
        }
    }
}
