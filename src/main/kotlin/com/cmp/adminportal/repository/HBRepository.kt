package com.cmp.adminportal.repository

import com.cmp.adminportal.domain.HB
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [HB] entity.
 */
@Suppress("unused")
@Repository
interface HBRepository : JpaRepository<HB, Long>
