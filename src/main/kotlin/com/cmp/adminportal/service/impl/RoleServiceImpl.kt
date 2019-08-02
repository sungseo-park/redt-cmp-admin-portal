package com.cmp.adminportal.service.impl

import com.cmp.adminportal.service.RoleService
import com.cmp.adminportal.domain.Role
import com.cmp.adminportal.repository.RoleRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [Role].
 */
@Service
@Transactional
class RoleServiceImpl(
    private val roleRepository: RoleRepository
) : RoleService {

    private val log = LoggerFactory.getLogger(RoleServiceImpl::class.java)

    /**
     * Save a role.
     *
     * @param role the entity to save.
     * @return the persisted entity.
     */
    override fun save(role: Role): Role {
        log.debug("Request to save Role : {}", role)
        return roleRepository.save(role)
    }

    /**
     * Get all the roles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<Role> {
        log.debug("Request to get all Roles")
        return roleRepository.findAll(pageable)
    }

    /**
     * Get one role by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<Role> {
        log.debug("Request to get Role : {}", id)
        return roleRepository.findById(id)
    }

    /**
     * Delete the role by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Role : {}", id)

        roleRepository.deleteById(id)
    }
}
