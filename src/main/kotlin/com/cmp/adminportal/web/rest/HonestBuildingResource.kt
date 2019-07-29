package com.cmp.adminportal.web.rest

import com.cmp.adminportal.domain.HonestBuilding
import com.cmp.adminportal.repository.HonestBuildingRepository
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
 * REST controller for managing [com.cmp.adminportal.domain.HonestBuilding].
 */
@RestController
@RequestMapping("/api")
class HonestBuildingResource(
    private val honestBuildingRepository: HonestBuildingRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /honest-buildings` : Create a new honestBuilding.
     *
     * @param honestBuilding the honestBuilding to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new honestBuilding, or with status `400 (Bad Request)` if the honestBuilding has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/honest-buildings")
    fun createHonestBuilding(@RequestBody honestBuilding: HonestBuilding): ResponseEntity<HonestBuilding> {
        log.debug("REST request to save HonestBuilding : {}", honestBuilding)
        if (honestBuilding.id != null) {
            throw BadRequestAlertException("A new honestBuilding cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = honestBuildingRepository.save(honestBuilding)
        return ResponseEntity.created(URI("/api/honest-buildings/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /honest-buildings` : Updates an existing honestBuilding.
     *
     * @param honestBuilding the honestBuilding to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated honestBuilding,
     * or with status `400 (Bad Request)` if the honestBuilding is not valid,
     * or with status `500 (Internal Server Error)` if the honestBuilding couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/honest-buildings")
    fun updateHonestBuilding(@RequestBody honestBuilding: HonestBuilding): ResponseEntity<HonestBuilding> {
        log.debug("REST request to update HonestBuilding : {}", honestBuilding)
        if (honestBuilding.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = honestBuildingRepository.save(honestBuilding)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, honestBuilding.id.toString()))
            .body(result)
    }

    /**
     * `GET  /honest-buildings` : get all the honestBuildings.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of honestBuildings in body.
     */
    @GetMapping("/honest-buildings")
    fun getAllHonestBuildings(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<HonestBuilding>> {
        log.debug("REST request to get a page of HonestBuildings")
        val page = honestBuildingRepository.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /honest-buildings/:id` : get the "id" honestBuilding.
     *
     * @param id the id of the honestBuilding to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the honestBuilding, or with status `404 (Not Found)`.
     */
    @GetMapping("/honest-buildings/{id}")
    fun getHonestBuilding(@PathVariable id: Long): ResponseEntity<HonestBuilding> {
        log.debug("REST request to get HonestBuilding : {}", id)
        val honestBuilding = honestBuildingRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(honestBuilding)
    }

    /**
     * `DELETE  /honest-buildings/:id` : delete the "id" honestBuilding.
     *
     * @param id the id of the honestBuilding to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/honest-buildings/{id}")
    fun deleteHonestBuilding(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete HonestBuilding : {}", id)

        honestBuildingRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "honestBuilding"
    }
}
