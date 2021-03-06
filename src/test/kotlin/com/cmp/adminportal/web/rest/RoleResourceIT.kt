package com.cmp.adminportal.web.rest

import com.cmp.adminportal.CmpAdminPortalApp
import com.cmp.adminportal.config.TestSecurityConfiguration
import com.cmp.adminportal.domain.Role
import com.cmp.adminportal.repository.RoleRepository
import com.cmp.adminportal.service.RoleService
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
 * Test class for the RoleResource REST controller.
 *
 * @see RoleResource
 */
@SpringBootTest(classes = [CmpAdminPortalApp::class, TestSecurityConfiguration::class])
class RoleResourceIT {

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var roleService: RoleService

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

    private lateinit var restRoleMockMvc: MockMvc

    private lateinit var role: Role

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val roleResource = RoleResource(roleService)
        this.restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        role = createEntity(em)
    }

    @Test
    @Transactional
    fun createRole() {
        val databaseSizeBeforeCreate = roleRepository.findAll().size

        // Create the Role
        restRoleMockMvc.perform(
            post("/api/roles")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(role))
        ).andExpect(status().isCreated)

        // Validate the Role in the database
        val roleList = roleRepository.findAll()
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1)
        val testRole = roleList[roleList.size - 1]
        assertThat(testRole.role).isEqualTo(DEFAULT_ROLE)
        assertThat(testRole.access).isEqualTo(DEFAULT_ACCESS)
    }

    @Test
    @Transactional
    fun createRoleWithExistingId() {
        val databaseSizeBeforeCreate = roleRepository.findAll().size

        // Create the Role with an existing ID
        role.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc.perform(
            post("/api/roles")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(role))
        ).andExpect(status().isBadRequest)

        // Validate the Role in the database
        val roleList = roleRepository.findAll()
        assertThat(roleList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    fun getAllRoles() {
        // Initialize the database
        roleRepository.saveAndFlush(role)

        // Get all the roleList
        restRoleMockMvc.perform(get("/api/roles?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.id?.toInt())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].access").value(hasItem(DEFAULT_ACCESS.toString())))
    }

    @Test
    @Transactional
    fun getRole() {
        // Initialize the database
        roleRepository.saveAndFlush(role)

        val id = role.id
        assertNotNull(id)

        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.access").value(DEFAULT_ACCESS.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingRole() {
        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateRole() {
        // Initialize the database
        roleService.save(role)

        val databaseSizeBeforeUpdate = roleRepository.findAll().size

        // Update the role
        val id = role.id
        assertNotNull(id)
        val updatedRole = roleRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole)
        updatedRole.role = UPDATED_ROLE
        updatedRole.access = UPDATED_ACCESS

        restRoleMockMvc.perform(
            put("/api/roles")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedRole))
        ).andExpect(status().isOk)

        // Validate the Role in the database
        val roleList = roleRepository.findAll()
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate)
        val testRole = roleList[roleList.size - 1]
        assertThat(testRole.role).isEqualTo(UPDATED_ROLE)
        assertThat(testRole.access).isEqualTo(UPDATED_ACCESS)
    }

    @Test
    @Transactional
    fun updateNonExistingRole() {
        val databaseSizeBeforeUpdate = roleRepository.findAll().size

        // Create the Role

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc.perform(
            put("/api/roles")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(role))
        ).andExpect(status().isBadRequest)

        // Validate the Role in the database
        val roleList = roleRepository.findAll()
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    fun deleteRole() {
        // Initialize the database
        roleService.save(role)

        val databaseSizeBeforeDelete = roleRepository.findAll().size

        val id = role.id
        assertNotNull(id)

        // Delete the role
        restRoleMockMvc.perform(
            delete("/api/roles/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val roleList = roleRepository.findAll()
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        equalsVerifier(Role::class)
        val role1 = Role()
        role1.id = 1L
        val role2 = Role()
        role2.id = role1.id
        assertThat(role1).isEqualTo(role2)
        role2.id = 2L
        assertThat(role1).isNotEqualTo(role2)
        role1.id = null
        assertThat(role1).isNotEqualTo(role2)
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
        fun createEntity(em: EntityManager): Role {
            val role = Role(
                role = DEFAULT_ROLE,
                access = DEFAULT_ACCESS
            )

            return role
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Role {
            val role = Role(
                role = UPDATED_ROLE,
                access = UPDATED_ACCESS
            )

            return role
        }
    }
}
