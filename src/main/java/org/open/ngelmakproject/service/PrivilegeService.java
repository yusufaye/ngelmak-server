package org.open.ngelmakproject.service;

import java.time.Instant;
import java.util.Optional;

import org.open.ngelmakproject.domain.User;
import org.open.ngelmakproject.domain.UserPrivilege;
import org.open.ngelmakproject.domain.enumeration.GrantStatus;
import org.open.ngelmakproject.repository.UserPrivilegeRepository;
import org.open.ngelmakproject.repository.UserRepository;
import org.open.ngelmakproject.service.dto.UserPrivilegeDTO;
import org.open.ngelmakproject.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link org.open.ngelmakproject.domain.NkPrivilege}.
 */
@Service
@Transactional
public class PrivilegeService {

    private static final String ENTITY_NAME = "privilege";

    private static final Logger log = LoggerFactory.getLogger(PrivilegeService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPrivilegeRepository userPrivilegeRepository;

    /**
     * Save a membership.
     *
     * @param membership the entity to grand.
     * @return the persisted entity.
     */
    public UserPrivilege grand(UserPrivilegeDTO userPrivilegeDTO) {
        log.debug("Request to grand Privilege : {}", userPrivilegeDTO);
        Optional<User> optional = userService.getUserWithAuthorities();
        if (optional.isEmpty()) {
            throw new BadRequestAlertException("Invalid privilege assigner", ENTITY_NAME, "privilegeAssignerNotFound");
        }
        Optional<User> grantTo = userRepository.findOneByLogin(userPrivilegeDTO.getLogin());
        if (grantTo.isEmpty()) {
            throw new BadRequestAlertException("Invalid privilege owner", ENTITY_NAME, "privilegeOwnerNotFound");
        }
        if (userPrivilegeRepository.existsByPrivilegeAndGrantedTo(userPrivilegeDTO.getPrivilege(), grantTo.get())) {
            throw new BadRequestAlertException("privilege already exists", ENTITY_NAME, "idexists");
        }
        UserPrivilege userPrivilege = new UserPrivilege();
        userPrivilege.setPrivilege(userPrivilegeDTO.getPrivilege());
        userPrivilege.setLastUpdatedBy(optional.get());
        userPrivilege.setLastUpdatedDate(Instant.now());
        userPrivilege.setDate(Instant.now());
        userPrivilege.setComment(userPrivilegeDTO.getComment());
        userPrivilege.setGrantedTo(grantTo.get());
        userPrivilege.setGrantStatus(GrantStatus.GRANTED);
        return userPrivilegeRepository.save(userPrivilege);
    }

    public void revoke(Long id) {
        Optional<User> optional = userService.getUserWithAuthorities();
        if (optional.isEmpty()) {
            throw new BadRequestAlertException("Invalid privilege assigner", ENTITY_NAME, "privilegeAssignerNotFound");
        }
        userPrivilegeRepository.findById(id)
        .map(existingPost -> {
            existingPost.setGrantStatus(GrantStatus.REVOKED);
            existingPost.setLastUpdatedBy(optional.get());
            existingPost.setLastUpdatedDate(Instant.now());
            return existingPost;
        })
        .map(userPrivilegeRepository::save);
    }

    public void assign(Long id) {
        Optional<User> optional = userService.getUserWithAuthorities();
        if (optional.isEmpty()) {
            throw new BadRequestAlertException("Invalid privilege assigner", ENTITY_NAME, "privilegeAssignerNotFound");
        }
        userPrivilegeRepository.findById(id)
        .map(existingPost -> {
            existingPost.setGrantStatus(GrantStatus.GRANTED);
            existingPost.setLastUpdatedBy(optional.get());
            existingPost.setLastUpdatedDate(Instant.now());
            return existingPost;
        })
        .map(userPrivilegeRepository::save);
    }
}
