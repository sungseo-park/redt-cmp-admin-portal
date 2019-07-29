package com.cmp.adminportal.repository

import com.cmp.adminportal.domain.CostManagementPlatform
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [CostManagementPlatform] entity.
 */
@Suppress("unused")
@Repository
interface CostManagementPlatformRepository : JpaRepository<CostManagementPlatform, Long>
