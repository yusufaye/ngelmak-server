package org.open.ngelmakproject.service;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.open.ngelmakproject.domain.Config;
import org.open.ngelmakproject.domain.NgelmakAccount;
import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.domain.enumeration.Accessibility;
import org.open.ngelmakproject.domain.enumeration.Visibility;
import org.open.ngelmakproject.repository.NgelmakAccountRepository;
import org.open.ngelmakproject.service.dto.NgelmakAccountDTO;
import org.open.ngelmakproject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.open.ngelmakproject.domain.NgelmakAccount}.
 */
@Service
@Transactional
public class NgelmakAccountService {

    private static final String ENTITY_NAME = "ngelmak-account";

    private static final Logger log = LoggerFactory.getLogger(NgelmakAccountService.class);

    private final NgelmakAccountRepository ngelmakAccountRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ConfigService configService;

    public NgelmakAccountService(NgelmakAccountRepository ngelmakAccountRepository) {
        this.ngelmakAccountRepository = ngelmakAccountRepository;
    }

    /**
     * Save a ngelmakAccount.
     *
     * @param ngelmakAccount the entity to save.
     * @return the persisted entity.
     */
    public NgelmakAccount save(NgelmakAccountDTO ngelmakAccountDTO) {
        log.info("Request to save NgelmakAccount : {}", ngelmakAccountDTO);

        Optional<User> optional = userService.getUserWithAuthorities();
        if (optional.isEmpty()) {
            throw new BadRequestAlertException("A new should always be attach to a user", ENTITY_NAME, "userNotFound");
        }

        NgelmakAccount ngelmakAccount = new NgelmakAccount();
        ngelmakAccount.setCreatedAt(ZonedDateTime.now());
        ngelmakAccount.setName(ngelmakAccountDTO.getName());
        ngelmakAccount.setVisibility(ngelmakAccountDTO.getVisibility());
        ngelmakAccount.setUser(optional.get());
        Config defaultConfig = new Config();
        defaultConfig.lastUpdate(ZonedDateTime.now());
        defaultConfig.defaultAccessibility(Accessibility.DEFAULT);
        defaultConfig.defaultVisibility(Visibility.PRIVATE);
        defaultConfig = configService.save(defaultConfig);
        ngelmakAccount.setConfiguration(defaultConfig);
        return ngelmakAccountRepository.save(ngelmakAccount);
    }

    /**
     * Update a ngelmakAccount.
     *
     * @param ngelmakAccount the entity to save.
     * @return the persisted entity.
     */
    public NgelmakAccount update(NgelmakAccount ngelmakAccount) {
        log.debug("Request to update NgelmakAccount : {}", ngelmakAccount);
        if (!ngelmakAccountRepository.existsById(ngelmakAccount.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        return ngelmakAccountRepository.save(ngelmakAccount);
    }

    /**
     * Partially update a ngelmakAccount.
     *
     * @param ngelmakAccount the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NgelmakAccount> partialUpdate(NgelmakAccount ngelmakAccount) {
        log.debug("Request to partially update NgelmakAccount : {}", ngelmakAccount);

        return ngelmakAccountRepository
            .findById(ngelmakAccount.getId())
            .map(existingNgelmakAccount -> {
                if (ngelmakAccount.getName() != null) {
                    existingNgelmakAccount.setName(ngelmakAccount.getName());
                }
                if (ngelmakAccount.getForegroundPicture() != null) {
                    existingNgelmakAccount.setForegroundPicture(ngelmakAccount.getForegroundPicture());
                }
                if (ngelmakAccount.getBackgroundPicture() != null) {
                    existingNgelmakAccount.setBackgroundPicture(ngelmakAccount.getBackgroundPicture());
                }
                if (ngelmakAccount.getVisibility() != null) {
                    existingNgelmakAccount.setVisibility(ngelmakAccount.getVisibility());
                }
                if (ngelmakAccount.getCreatedAt() != null) {
                    existingNgelmakAccount.setCreatedAt(ngelmakAccount.getCreatedAt());
                }
                if (ngelmakAccount.getDescription() != null) {
                    existingNgelmakAccount.setDescription(ngelmakAccount.getDescription());
                }

                return existingNgelmakAccount;
            })
            .map(ngelmakAccountRepository::save);
    }

    /**
     * Get all the ngelmakAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NgelmakAccount> findAll(Pageable pageable) {
        log.debug("Request to get all NgelmakAccounts");
        return ngelmakAccountRepository.findAll(pageable);
    }

    /**
     * Get one ngelmakAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NgelmakAccount> findOne(Long id) {
        log.debug("Request to get NgelmakAccount : {}", id);
        return ngelmakAccountRepository.findById(id);
    }

    /**
     * Get one ngelmakAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NgelmakAccount> findOneByCurrentUser() {
        Optional<User> optional = userService.getUserWithAuthorities();
        if (optional.isEmpty()) {
            throw new BadRequestAlertException("A new should always be attach to a user", ENTITY_NAME, "userNotFound");
        }
        log.debug("Request to get NgelmakAccount for the connected user {}", optional.get());
        return ngelmakAccountRepository.findByUser(optional.get());
    }

    /**
     * Delete the ngelmakAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NgelmakAccount : {}", id);
        ngelmakAccountRepository.deleteById(id);
    }
}
