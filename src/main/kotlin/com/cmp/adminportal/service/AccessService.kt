package com.cmp.adminportal.service

import com.cmp.adminportal.domain.Access

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.util.Optional

/**
 * Service Interface for managing [Access].
 */
interface AccessService {

    /**
     * Save a access.
     *
     * @param access the entity to save.
     * @return the persisted entity.
     */
    fun save(access: Access): Access

    /**
     * Get all the accesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<Access>

    /**
     * Get the "id" access.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<Access>

    /**
     * Delete the "id" access.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
