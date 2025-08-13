package org.open.ngelmakproject.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.open.ngelmakproject.domain.NkPrivilege;
import org.open.ngelmakproject.domain.UserPrivilege;
import org.open.ngelmakproject.repository.PrivilegeRepository;
import org.open.ngelmakproject.repository.UserPrivilegeRepository;
import org.open.ngelmakproject.service.PrivilegeService;
import org.open.ngelmakproject.service.dto.UserPrivilegeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link org.open.ngelmakproject.domain.NkPrivilege}.
 */
@RestController
@RequestMapping("/api/privileges")
@Transactional
public class PrivilegeResource {

    private static final Logger log = LoggerFactory.getLogger(PrivilegeResource.class);

    private static final String ENTITY_NAME = "adminPrivilege";

    @Value("${ngelmak.clientApp.name}")
    private String applicationName;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private UserPrivilegeRepository userPrivilegeRepository;

    /**
     * {@code POST  /privileges} : Create a new privilege.
     *
     * @param privilege the privilege to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new privilege, or with status {@code 400 (Bad Request)} if the privilege has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyPrivilege('ROLE_ADMIN')")
    public ResponseEntity<UserPrivilege> grantPrivilege(@Valid @RequestBody UserPrivilegeDTO userPrivilegeDTO) throws URISyntaxException {
        log.debug("REST request to grant Privilege : {}", userPrivilegeDTO);
        UserPrivilege userPrivilege = privilegeService.grand(userPrivilegeDTO);
        return ResponseEntity.created(new URI("/api/userPrivileges/" + userPrivilege.getId().toString()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userPrivilege.getId().toString()))
            .body(userPrivilege);
    }

    /**
     * {@code GET  /privileges} : get all the privileges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of privileges in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NkPrivilege>> getAllPrivileges() {
        log.debug("REST request to get a page of Privileges");
        List<NkPrivilege> privileges = privilegeRepository.findAll();
        return ResponseEntity.ok().body(privileges);
    }

    /**
     * {@code GET  /privileges/:login} : get the "login" privilege.
     *
     * @param login the login of the privilege to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the privilege, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{login}")
    @PreAuthorize("hasAnyPrivilege('ROLE_ADMIN')")
    public ResponseEntity<List<UserPrivilege>> getPrivilege(@PathVariable("login") String login) {
        log.debug("REST request to get Privilege : {}", login);
        List<UserPrivilege> userPrivileges = userPrivilegeRepository.findByGrantedToLogin(login);
        return ResponseEntity.ok().body(userPrivileges);
    }

    /**
     * {@code REVOKE  /privileges/revoke/:id} : revoke the "id" privilege.
     *
     * @param id the id of the privilege to revoke.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/revoke/{id}")
    @PreAuthorize("hasAnyPrivilege('ROLE_ADMIN')")
    public ResponseEntity<Void> revokePrivilege(@PathVariable("id") Long id) {
        log.debug("REST request to revoke Privilege : {}", id);
        privilegeService.revoke(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code ASSIGN  /privileges/assign/:id} : revoke the "id" privilege.
     *
     * @param id the id of the privilege to revoke.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PutMapping("/assign/{id}")
    @PreAuthorize("hasAnyPrivilege('ROLE_ADMIN')")
    public ResponseEntity<Void> assignPrivilege(@PathVariable("id") Long id) {
        log.debug("REST request to assign Privilege : {}", id);
        privilegeService.assign(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code DELETE  /privileges/:id} : delete the "id" privilege.
     *
     * @param id the id of the privilege to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyPrivilege('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePrivilege(@PathVariable("id") String id) {
        log.debug("REST request to delete Privilege : {}", id);
        privilegeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
