package com.cmp.adminportal.web.rest

import com.cmp.adminportal.domain.Role
import com.cmp.adminportal.service.RoleService
import com.cmp.adminportal.web.rest.errors.BadRequestAlertException

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.net.URI
import java.net.URISyntaxException

/**
 * REST controller for managing [com.cmp.adminportal.domain.Role].
 */
@RestController
@RequestMapping("/api")
class RoleResource(
    private val roleService: RoleService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /roles` : Create a new role.
     *
     * @param role the role to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new role, or with status `400 (Bad Request)` if the role has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roles")
    fun createRole(@RequestBody role: Role): ResponseEntity<Role> {
        log.debug("REST request to save Role : {}", role)
        if (role.id != null) {
            throw BadRequestAlertException("A new role cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = roleService.save(role)
        return ResponseEntity.created(URI("/api/roles/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /roles` : Updates an existing role.
     *
     * @param role the role to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated role,
     * or with status `400 (Bad Request)` if the role is not valid,
     * or with status `500 (Internal Server Error)` if the role couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/roles")
    fun updateRole(@RequestBody role: Role): ResponseEntity<Role> {
        log.debug("REST request to update Role : {}", role)
        if (role.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = roleService.save(role)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, role.id.toString()))
            .body(result)
    }

    /**
     * `GET  /roles` : get all the roles.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of roles in body.
     */
    @GetMapping("/roles")
    fun getAllRoles(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<Role>> {
        log.debug("REST request to get a page of Roles")
        val page = roleService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /roles/:id` : get the "id" role.
     *
     * @param id the id of the role to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the role, or with status `404 (Not Found)`.
     */
    @GetMapping("/roles/{id}")
    fun getRole(@PathVariable id: Long): ResponseEntity<Role> {
        log.debug("REST request to get Role : {}", id)
        val role = roleService.findOne(id)
        return ResponseUtil.wrapOrNotFound(role)
    }

    /**
     * `DELETE  /roles/:id` : delete the "id" role.
     *
     * @param id the id of the role to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/roles/{id}")
    fun deleteRole(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Role : {}", id)
        roleService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "role"
    }
}
