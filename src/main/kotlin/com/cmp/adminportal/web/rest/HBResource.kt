package com.cmp.adminportal.web.rest

import com.cmp.adminportal.domain.HB
import com.cmp.adminportal.repository.HBRepository
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
 * REST controller for managing [com.cmp.adminportal.domain.HB].
 */
@RestController
@RequestMapping("/api")
class HBResource(
    private val hBRepository: HBRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /hbs` : Create a new hB.
     *
     * @param hB the hB to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new hB, or with status `400 (Bad Request)` if the hB has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hbs")
    fun createHB(@RequestBody hB: HB): ResponseEntity<HB> {
        log.debug("REST request to save HB : {}", hB)
        if (hB.id != null) {
            throw BadRequestAlertException("A new hB cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = hBRepository.save(hB)
        return ResponseEntity.created(URI("/api/hbs/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /hbs` : Updates an existing hB.
     *
     * @param hB the hB to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated hB,
     * or with status `400 (Bad Request)` if the hB is not valid,
     * or with status `500 (Internal Server Error)` if the hB couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hbs")
    fun updateHB(@RequestBody hB: HB): ResponseEntity<HB> {
        log.debug("REST request to update HB : {}", hB)
        if (hB.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = hBRepository.save(hB)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hB.id.toString()))
            .body(result)
    }

    /**
     * `GET  /hbs` : get all the hBS.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of hBS in body.
     */
    @GetMapping("/hbs")
    fun getAllHBS(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<HB>> {
        log.debug("REST request to get a page of HBS")
        val page = hBRepository.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /hbs/:id` : get the "id" hB.
     *
     * @param id the id of the hB to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the hB, or with status `404 (Not Found)`.
     */
    @GetMapping("/hbs/{id}")
    fun getHB(@PathVariable id: Long): ResponseEntity<HB> {
        log.debug("REST request to get HB : {}", id)
        val hB = hBRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(hB)
    }

    /**
     * `DELETE  /hbs/:id` : delete the "id" hB.
     *
     * @param id the id of the hB to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/hbs/{id}")
    fun deleteHB(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete HB : {}", id)

        hBRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "hB"
    }
}
