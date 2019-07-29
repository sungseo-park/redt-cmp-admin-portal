package com.cmp.adminportal.repository

import com.cmp.adminportal.domain.HonestBuilding
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [HonestBuilding] entity.
 */
@Suppress("unused")
@Repository
interface HonestBuildingRepository : JpaRepository<HonestBuilding, Long>
