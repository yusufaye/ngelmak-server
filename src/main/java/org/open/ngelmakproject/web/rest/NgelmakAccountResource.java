package org.open.ngelmakproject.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.open.ngelmakproject.domain.NgelmakAccount;
import org.open.ngelmakproject.repository.NgelmakAccountRepository;
import org.open.ngelmakproject.service.NgelmakAccountService;
import org.open.ngelmakproject.service.dto.NgelmakAccountDTO;
import org.open.ngelmakproject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.open.ngelmakproject.domain.NgelmakAccount}.
 */
@RestController
@RequestMapping("/api/ngelmak-accounts")
public class NgelmakAccountResource {

    private static final Logger log = LoggerFactory.getLogger(NgelmakAccountResource.class);

    private static final String ENTITY_NAME = "ngelmakAccount";

    @Value("${ngelmak.clientApp.name}")
    private String applicationName;

    private final NgelmakAccountService ngelmakAccountService;

    private final NgelmakAccountRepository ngelmakAccountRepository;

    public NgelmakAccountResource(NgelmakAccountService ngelmakAccountService, NgelmakAccountRepository ngelmakAccountRepository) {
        this.ngelmakAccountService = ngelmakAccountService;
        this.ngelmakAccountRepository = ngelmakAccountRepository;
    }

    /**
     * {@code POST  /ngelmak-accounts} : Create a new ngelmakAccount.
     *
     * @param ngelmakAccount the ngelmakAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ngelmakAccount, or with status {@code 400 (Bad Request)} if the ngelmakAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NgelmakAccount> createNgelmakAccount(@Valid @RequestBody NgelmakAccountDTO ngelmakAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save NgelmakAccount : {}", ngelmakAccountDTO);
        if (ngelmakAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new ngelmakAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NgelmakAccount ngelmakAccount = ngelmakAccountService.save(ngelmakAccountDTO);
        return ResponseEntity.created(new URI("/api/ngelmak-accounts/" + ngelmakAccount.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ngelmakAccount.getId().toString()))
            .body(ngelmakAccount);
    }

    /**
     * {@code PUT  /ngelmak-accounts/:id} : Updates an existing ngelmakAccount.
     *
     * @param id the id of the ngelmakAccount to save.
     * @param ngelmakAccount the ngelmakAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ngelmakAccount,
     * or with status {@code 400 (Bad Request)} if the ngelmakAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ngelmakAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<NgelmakAccount> updateNgelmakAccount(
        @Valid @RequestBody NgelmakAccount ngelmakAccount
    ) throws URISyntaxException {
        log.debug("REST request to update NgelmakAccount : {}", ngelmakAccount);
        if (ngelmakAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ngelmakAccount = ngelmakAccountService.update(ngelmakAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ngelmakAccount.getId().toString()))
            .body(ngelmakAccount);
    }

    /**
     * {@code PATCH  /ngelmak-accounts/:id} : Partial updates given fields of an existing ngelmakAccount, field will ignore if it is null
     *
     * @param id the id of the ngelmakAccount to save.
     * @param ngelmakAccount the ngelmakAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ngelmakAccount,
     * or with status {@code 400 (Bad Request)} if the ngelmakAccount is not valid,
     * or with status {@code 404 (Not Found)} if the ngelmakAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the ngelmakAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NgelmakAccount> partialUpdateNgelmakAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NgelmakAccount ngelmakAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update NgelmakAccount partially : {}, {}", id, ngelmakAccount);
        if (ngelmakAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ngelmakAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ngelmakAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NgelmakAccount> result = ngelmakAccountService.partialUpdate(ngelmakAccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ngelmakAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /ngelmak-accounts} : get all the ngelmakAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ngelmakAccounts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NgelmakAccount>> getAllNgelmakAccounts(@ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of NgelmakAccounts");
        Page<NgelmakAccount> page = ngelmakAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ngelmak-accounts/:id} : get the "id" ngelmakAccount.
     *
     * @param id the id of the ngelmakAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ngelmakAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NgelmakAccount> getNgelmakAccount(@PathVariable("id") Long id) {
        log.debug("REST request to get NgelmakAccount : {}", id);
        Optional<NgelmakAccount> ngelmakAccount = ngelmakAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ngelmakAccount);
    }

    @GetMapping("/current-user")
    public ResponseEntity<NgelmakAccount> findByCurrentUser() {
        log.debug("REST request to get NgelmakAccount by current user");
        Optional<NgelmakAccount> ngelmakAccount = ngelmakAccountService.findOneByCurrentUser();
        return ResponseUtil.wrapOrNotFound(ngelmakAccount);
    }

    /**
     * {@code DELETE  /ngelmak-accounts/:id} : delete the "id" ngelmakAccount.
     *
     * @param id the id of the ngelmakAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNgelmakAccount(@PathVariable("id") Long id) {
        log.debug("REST request to delete NgelmakAccount : {}", id);
        ngelmakAccountService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
