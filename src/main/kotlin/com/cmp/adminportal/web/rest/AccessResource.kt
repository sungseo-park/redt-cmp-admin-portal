package com.cmp.adminportal.web.rest

import com.cmp.adminportal.domain.Access
import com.cmp.adminportal.service.AccessService
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
 * REST controller for managing [com.cmp.adminportal.domain.Access].
 */
@RestController
@RequestMapping("/api")
class AccessResource(
    private val accessService: AccessService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /accesses` : Create a new access.
     *
     * @param access the access to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new access, or with status `400 (Bad Request)` if the access has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/accesses")
    fun createAccess(@RequestBody access: Access): ResponseEntity<Access> {
        log.debug("REST request to save Access : {}", access)
        if (access.id != null) {
            throw BadRequestAlertException("A new access cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = accessService.save(access)
        return ResponseEntity.created(URI("/api/accesses/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /accesses` : Updates an existing access.
     *
     * @param access the access to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated access,
     * or with status `400 (Bad Request)` if the access is not valid,
     * or with status `500 (Internal Server Error)` if the access couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/accesses")
    fun updateAccess(@RequestBody access: Access): ResponseEntity<Access> {
        log.debug("REST request to update Access : {}", access)
        if (access.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = accessService.save(access)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, access.id.toString()))
            .body(result)
    }

    /**
     * `GET  /accesses` : get all the accesses.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of accesses in body.
     */
    @GetMapping("/accesses")
    fun getAllAccesses(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<Access>> {
        log.debug("REST request to get a page of Accesses")
        val page = accessService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /accesses/:id` : get the "id" access.
     *
     * @param id the id of the access to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the access, or with status `404 (Not Found)`.
     */
    @GetMapping("/accesses/{id}")
    fun getAccess(@PathVariable id: Long): ResponseEntity<Access> {
        log.debug("REST request to get Access : {}", id)
        val access = accessService.findOne(id)
        return ResponseUtil.wrapOrNotFound(access)
    }

    /**
     * `DELETE  /accesses/:id` : delete the "id" access.
     *
     * @param id the id of the access to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/accesses/{id}")
    fun deleteAccess(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Access : {}", id)
        accessService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "access"
    }
}
