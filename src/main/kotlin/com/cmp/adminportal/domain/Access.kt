package com.cmp.adminportal.domain

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

import java.io.Serializable

/**
 * An Access.
 */
@Entity
@Table(name = "access")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class Access(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "access")
    var access: String? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Access) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Access{" +
        "id=$id" +
        ", access = '$access'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
