package org.open.ngelmakproject.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.open.ngelmakproject.domain.Config;
import org.open.ngelmakproject.repository.ConfigRepository;
import org.open.ngelmakproject.service.ConfigService;
import org.open.ngelmakproject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.open.ngelmakproject.domain.Config}.
 */
@RestController
@RequestMapping("/api/configs")
public class ConfigResource {

    private static final Logger log = LoggerFactory.getLogger(ConfigResource.class);

    private static final String ENTITY_NAME = "config";

    @Value("${ngelmak.clientApp.name}")
    private String applicationName;

    private final ConfigService configService;

    private final ConfigRepository configRepository;

    public ConfigResource(ConfigService configService, ConfigRepository configRepository) {
        this.configService = configService;
        this.configRepository = configRepository;
    }

    /**
     * {@code POST  /configs} : Create a new config.
     *
     * @param config the config to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new config, or with status {@code 400 (Bad Request)} if the config has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Config> createConfig(@RequestBody Config config) throws URISyntaxException {
        log.debug("REST request to save Config : {}", config);
        if (config.getId() != null) {
            throw new BadRequestAlertException("A new config cannot already have an ID", ENTITY_NAME, "idexists");
        }
        config = configService.save(config);
        return ResponseEntity.created(new URI("/api/configs/" + config.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, config.getId().toString()))
            .body(config);
    }

    /**
     * {@code PUT  /configs/:id} : Updates an existing config.
     *
     * @param id the id of the config to save.
     * @param config the config to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated config,
     * or with status {@code 400 (Bad Request)} if the config is not valid,
     * or with status {@code 500 (Internal Server Error)} if the config couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Config> updateConfig(@PathVariable(value = "id", required = false) final Long id, @RequestBody Config config)
        throws URISyntaxException {
        log.debug("REST request to update Config : {}, {}", id, config);
        if (config.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, config.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        config = configService.update(config);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, config.getId().toString()))
            .body(config);
    }

    /**
     * {@code PATCH  /configs/:id} : Partial updates given fields of an existing config, field will ignore if it is null
     *
     * @param id the id of the config to save.
     * @param config the config to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated config,
     * or with status {@code 400 (Bad Request)} if the config is not valid,
     * or with status {@code 404 (Not Found)} if the config is not found,
     * or with status {@code 500 (Internal Server Error)} if the config couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Config> partialUpdateConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Config config
    ) throws URISyntaxException {
        log.debug("REST request to partial update Config partially : {}, {}", id, config);
        if (config.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, config.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Config> result = configService.partialUpdate(config);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, config.getId().toString())
        );
    }

    /**
     * {@code GET  /configs} : get all the configs.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Config>> getAllConfigs(
        @ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("ngelmakaccount-is-null".equals(filter)) {
            log.debug("REST request to get all Configs where ngelmakAccount is null");
            return new ResponseEntity<>(configService.findAllWhereNgelmakAccountIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Configs");
        Page<Config> page = configService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configs/:id} : get the "id" config.
     *
     * @param id the id of the config to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the config, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Config> getConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get Config : {}", id);
        Optional<Config> config = configService.findOne(id);
        return ResponseUtil.wrapOrNotFound(config);
    }

    /**
     * {@code DELETE  /configs/:id} : delete the "id" config.
     *
     * @param id the id of the config to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete Config : {}", id);
        configService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
