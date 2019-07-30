package com.cmp.adminportal.web.rest

import com.cmp.adminportal.domain.CostManagementPlatform
import com.cmp.adminportal.domain.HonestBuilding
import com.cmp.adminportal.repository.CostManagementPlatformRepository
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
 * REST controller for managing [com.cmp.adminportal.domain.CostManagementPlatform].
 */
@RestController
@RequestMapping("/api")
class CostManagementPlatformResource(
    private val costManagementPlatformRepository: CostManagementPlatformRepository,
    private val honestBuildingRepository: HonestBuildingRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /cost-management-platforms` : Create a new costManagementPlatform.
     *
     * @param costManagementPlatform the costManagementPlatform to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new costManagementPlatform, or with status `400 (Bad Request)` if the costManagementPlatform has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cost-management-platforms")
    fun createCostManagementPlatform(@RequestBody costManagementPlatform: CostManagementPlatform): ResponseEntity<CostManagementPlatform> {
        log.debug("REST request to save CostManagementPlatform : {}", costManagementPlatform)
        if (costManagementPlatform.id != null) {
            throw BadRequestAlertException("A new costManagementPlatform cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = costManagementPlatformRepository.save(costManagementPlatform)

        // Create a HB object with role and access from CMP
        val honestBuilding = HonestBuilding(
            role = result.role,
            access = result.access
        )
        // Initialize an instance of HB Resource
        val hbResource = HonestBuildingResource(honestBuildingRepository)
        // Create a new data with HB object in HB
        var hbResult = hbResource.createHonestBuilding(honestBuilding)

        // Create a new HB object including HB ID
        var newhb = HonestBuilding(
            id = hbResult.body.id
        )
        costManagementPlatform.honestbuilding = newhb

        // Update the CPM data with hbId
        updateCostManagementPlatform(costManagementPlatform)

        return ResponseEntity.created(URI("/api/cost-management-platforms/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /cost-management-platforms` : Updates an existing costManagementPlatform.
     *
     * @param costManagementPlatform the costManagementPlatform to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated costManagementPlatform,
     * or with status `400 (Bad Request)` if the costManagementPlatform is not valid,
     * or with status `500 (Internal Server Error)` if the costManagementPlatform couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cost-management-platforms")
    fun updateCostManagementPlatform(@RequestBody costManagementPlatform: CostManagementPlatform): ResponseEntity<CostManagementPlatform> {
        log.debug("REST request to update CostManagementPlatform : {}", costManagementPlatform)
        if (costManagementPlatform.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = costManagementPlatformRepository.save(costManagementPlatform)

        // Create a HB object with role and access from CMP
        val honestBuilding = HonestBuilding(
            role = result.role,
            access = result.access
        )
        // Initialize an instance of HB Resource
        val hbResource = HonestBuildingResource(honestBuildingRepository)
        // Create a new data with HB object in HB
        var hbResult = hbResource.getHonestBuilding(id = honestBuilding.body.id)

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, costManagementPlatform.id.toString()))
            .body(result)
    }

    /**
     * `GET  /cost-management-platforms` : get all the costManagementPlatforms.
     *
     * @param pageable the pagination information.
     * @param queryParams a [MultiValueMap] query parameters.
     * @param uriBuilder a [UriComponentsBuilder] URI builder.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of costManagementPlatforms in body.
     */
    @GetMapping("/cost-management-platforms")
    fun getAllCostManagementPlatforms(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<CostManagementPlatform>> {
        log.debug("REST request to get a page of CostManagementPlatforms")
        val page = costManagementPlatformRepository.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /cost-management-platforms/:id` : get the "id" costManagementPlatform.
     *
     * @param id the id of the costManagementPlatform to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the costManagementPlatform, or with status `404 (Not Found)`.
     */
    @GetMapping("/cost-management-platforms/{id}")
    fun getCostManagementPlatform(@PathVariable id: Long): ResponseEntity<CostManagementPlatform> {
        log.debug("REST request to get CostManagementPlatform : {}", id)
        val costManagementPlatform = costManagementPlatformRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(costManagementPlatform)
    }

    /**
     * `DELETE  /cost-management-platforms/:id` : delete the "id" costManagementPlatform.
     *
     * @param id the id of the costManagementPlatform to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/cost-management-platforms/{id}")
    fun deleteCostManagementPlatform(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete CostManagementPlatform : {}", id)

        costManagementPlatformRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    companion object {
        private const val ENTITY_NAME = "costManagementPlatform"
    }
}
