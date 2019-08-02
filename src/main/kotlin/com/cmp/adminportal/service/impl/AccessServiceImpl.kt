package com.cmp.adminportal.service.impl

import com.cmp.adminportal.service.AccessService
import com.cmp.adminportal.domain.Access
import com.cmp.adminportal.repository.AccessRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

/**
 * Service Implementation for managing [Access].
 */
@Service
@Transactional
class AccessServiceImpl(
    private val accessRepository: AccessRepository
) : AccessService {

    private val log = LoggerFactory.getLogger(AccessServiceImpl::class.java)

    /**
     * Save a access.
     *
     * @param access the entity to save.
     * @return the persisted entity.
     */
    override fun save(access: Access): Access {
        log.debug("Request to save Access : {}", access)
        return accessRepository.save(access)
    }

    /**
     * Get all the accesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<Access> {
        log.debug("Request to get all Accesses")
        return accessRepository.findAll(pageable)
    }

    /**
     * Get one access by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<Access> {
        log.debug("Request to get Access : {}", id)
        return accessRepository.findById(id)
    }

    /**
     * Delete the access by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Access : {}", id)

        accessRepository.deleteById(id)
    }
}
