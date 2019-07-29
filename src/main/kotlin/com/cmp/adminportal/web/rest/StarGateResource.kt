package com.cmp.adminportal.web.rest

import com.cmp.adminportal.domain.StarGate
import com.cmp.adminportal.repository.StarGateRepository
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
 * REST controller for managing [com.cmp.adminportal.domain.StarGate].
 */
@RestController
@RequestMapping("/api")
class StarGateResource(
    private val starGateRepository: StarGateRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /star-gates` : Create a new starGate.
     *
     * @param starGate the starGate to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new starGate, or with status `400 (Bad Request)` if the starGate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/star-gates")
    fun createStarGate(@RequestBody starGate: StarGate): ResponseEntity<StarGate> {
        log.debug("REST request to save StarGate : {}", starGate)
        if (starGate.id != null) {
            throw BadRequestAlertException("A new starGate cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = starGateRepository.save(starGate)
        return ResponseEntity.created(URI("/api/star-gates/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /star-gates` : Updates an existing starGate.
     *
     * @param starGate the starGate to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated starGate,
     * or with status `400 (Bad Request)` if the starGate is not valid,
     * or with status `500 (Internal Server Error)` if the starGate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/star-gates")
    fun updateStarGate(@RequestBody starGate: StarGate): ResponseEntity<StarGate> {
        log.debug("REST request to update StarGate : {}", starGate)
        if (starGate.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = starGateRepository.save(starGate)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, starGate.id.toString()))
            .body(result)
    }

    /**
     * `GET  /star-gates` : get all the starGates.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of starGates in body.
     */
    @GetMapping("/star-gates")
    fun getAllStarGates(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<StarGate>> {
        log.debug("REST request to get a page of StarGates")
        val page = starGateRepository.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /star-gates/:id` : get the "id" starGate.
     *
     * @param id the id of the starGate to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the starGate, or with status `404 (Not Found)`.
     */
    @GetMapping("/star-gates/{id}")
    fun getStarGate(@PathVariable id: Long): ResponseEntity<StarGate> {
        log.debug("REST request to get StarGate : {}", id)
        val starGate = starGateRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(starGate)
    }

    /**
     * `DELETE  /star-gates/:id` : delete the "id" starGate.
     *
     * @param id the id of the starGate to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/star-gates/{id}")
    fun deleteStarGate(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete StarGate : {}", id)

        starGateRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "starGate"
    }
}
