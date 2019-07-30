package com.cmp.adminportal.repository

import com.cmp.adminportal.domain.StarGate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [StarGate] entity.
 */
@Suppress("unused")
@Repository
interface StarGateRepository : JpaRepository<StarGate, Long>
