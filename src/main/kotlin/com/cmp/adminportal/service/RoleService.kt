package com.cmp.adminportal.service

import com.cmp.adminportal.domain.Role

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [Role].
 */
interface RoleService {

    /**
     * Save a role.
     *
     * @param role the entity to save.
     * @return the persisted entity.
     */
    fun save(role: Role): Role

    /**
     * Get all the roles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<Role>

    /**
     * Get the "id" role.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<Role>

    /**
     * Delete the "id" role.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
