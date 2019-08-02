package com.cmp.adminportal.repository

import com.cmp.adminportal.domain.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Role] entity.
 */
@Suppress("unused")
@Repository
interface RoleRepository : JpaRepository<Role, Long>
