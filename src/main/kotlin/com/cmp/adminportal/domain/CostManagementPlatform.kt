package com.cmp.adminportal.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

import java.io.Serializable

import com.cmp.adminportal.domain.enumeration.Access

/**
 * A CostManagementPlatform.
 */
@Entity
@Table(name = "cost_management_platform")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class CostManagementPlatform(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "role")
    var role: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "access")
    var access: Access? = null,

    @OneToOne
    @JoinColumn(unique = true)
    var hb: HB? = null,

    @OneToOne
    @JoinColumn(unique = true)
    var stargate: StarGate? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CostManagementPlatform) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "CostManagementPlatform{" +
        "id=$id" +
        ", role='$role'" +
        ", access='$access'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
