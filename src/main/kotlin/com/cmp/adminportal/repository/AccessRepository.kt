package com.cmp.adminportal.repository

import com.cmp.adminportal.domain.Access
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Access] entity.
 */
@Suppress("unused")
@Repository
interface AccessRepository : JpaRepository<Access, Long>
