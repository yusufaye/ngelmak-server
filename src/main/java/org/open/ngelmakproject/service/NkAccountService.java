package org.open.ngelmakproject.service;

import java.time.Instant;
import java.util.Optional;

import org.open.ngelmakproject.domain.Config;
import org.open.ngelmakproject.domain.NkAccount;
import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.domain.enumeration.Accessibility;
import org.open.ngelmakproject.domain.enumeration.Visibility;
import org.open.ngelmakproject.repository.NkAccountRepository;
import org.open.ngelmakproject.service.dto.NkAccountDTO;
import org.open.ngelmakproject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link org.open.ngelmakproject.domain.NkAccount}.
 */
@Service
@Transactional
public class NkAccountService {

    private static final String ENTITY_NAME = "ngelmak-account";

    private static final Logger log = LoggerFactory.getLogger(NkAccountService.class);

    private final NkAccountRepository ngelmakAccountRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ConfigService configService;

    public NkAccountService(NkAccountRepository ngelmakAccountRepository) {
        this.ngelmakAccountRepository = ngelmakAccountRepository;
    }

    /**
     * Save a ngelmakAccount.
     *
     * @param ngelmakAccount the entity to save.
     * @return the persisted entity.
     */
    public NkAccount save(NkAccountDTO ngelmakAccountDTO) {
        log.info("Request to save NkAccount : {}", ngelmakAccountDTO);

        Optional<User> optional = userService.getUserWithAuthorities();
        if (optional.isEmpty()) {
            throw new BadRequestAlertException("A new should always be attach to a user", ENTITY_NAME, "userNotFound");
        }

        NkAccount ngelmakAccount = new NkAccount();
        ngelmakAccount.setCreatedAt(Instant.now());
        ngelmakAccount.setName(ngelmakAccountDTO.getName());
        ngelmakAccount.setVisibility(ngelmakAccountDTO.getVisibility());
        ngelmakAccount.setUser(optional.get());
        Config defaultConfig = new Config();
        defaultConfig.lastUpdate(Instant.now());
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
    public NkAccount update(NkAccount ngelmakAccount) {
        log.debug("Request to update NkAccount : {}", ngelmakAccount);
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
    public Optional<NkAccount> partialUpdate(NkAccount ngelmakAccount) {
        log.debug("Request to partially update NkAccount : {}", ngelmakAccount);

        return ngelmakAccountRepository
                .findById(ngelmakAccount.getId())
                .map(existingNkAccount -> {
                    if (ngelmakAccount.getName() != null) {
                        existingNkAccount.setName(ngelmakAccount.getName());
                    }
                    if (ngelmakAccount.getForegroundPicture() != null) {
                        existingNkAccount.setForegroundPicture(ngelmakAccount.getForegroundPicture());
                    }
                    if (ngelmakAccount.getBackgroundPicture() != null) {
                        existingNkAccount.setBackgroundPicture(ngelmakAccount.getBackgroundPicture());
                    }
                    if (ngelmakAccount.getVisibility() != null) {
                        existingNkAccount.setVisibility(ngelmakAccount.getVisibility());
                    }
                    if (ngelmakAccount.getCreatedAt() != null) {
                        existingNkAccount.setCreatedAt(ngelmakAccount.getCreatedAt());
                    }
                    if (ngelmakAccount.getDescription() != null) {
                        existingNkAccount.setDescription(ngelmakAccount.getDescription());
                    }

                    return existingNkAccount;
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
    public Page<NkAccount> findAll(Pageable pageable) {
        log.debug("Request to get all NkAccounts");
        return ngelmakAccountRepository.findAll(pageable);
    }

    /**
     * Get one ngelmakAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NkAccount> findOne(Long id) {
        log.debug("Request to get NkAccount : {}", id);
        return ngelmakAccountRepository.findById(id);
    }

    /**
     * Get one ngelmakAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NkAccount> findOneByCurrentUser() {
        Optional<User> optional = userService.getUserWithAuthorities();
        if (optional.isEmpty()) {
            throw new BadRequestAlertException("A new should always be attach to a user", ENTITY_NAME, "userNotFound");
        }
        log.debug("Request to get NkAccount for the connected user {}", optional.get());
        return ngelmakAccountRepository.findOneByUser(optional.get());
    }

    /**
     * Get one ngelmakAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public NkAccount findByCurrentUser() {
        Optional<User> optional = userService.getUserWithAuthorities();
        if (optional.isEmpty()) {
            throw new BadRequestAlertException("A new should always be attach to a user", ENTITY_NAME, "userNotFound");
        }
        Optional<NkAccount> optional2 = ngelmakAccountRepository.findOneByUser(optional.get());
        if (optional2.isEmpty()) {
            throw new BadRequestAlertException("No account found for the given user", ENTITY_NAME, "nkAccountNotFound");
        }
        return optional2.get();
    }

    /**
     * Delete the ngelmakAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NkAccount : {}", id);
        ngelmakAccountRepository.deleteById(id);
    }
}
