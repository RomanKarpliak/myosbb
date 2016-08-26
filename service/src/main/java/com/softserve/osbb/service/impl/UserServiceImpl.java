package com.softserve.osbb.service.impl;

import com.softserve.osbb.model.Role;
import com.softserve.osbb.model.User;
import com.softserve.osbb.repository.RoleRepository;
import com.softserve.osbb.repository.UserRepository;
import com.softserve.osbb.service.UserService;
import com.softserve.osbb.service.utils.UserModelUserDetails;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by cavayman on 11.07.2016.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository userRoleRepository;


    private Logger log;

    @Override
    public User save(User user) {

        return userRepository.save(user);
    }


    @Override
    public User findOne(Integer integer) {

        return userRepository.findOne(integer);
    }

    @Override
    public User findOne(String id) {
        try {
            return userRepository.findOne(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean exists(Integer integer) {

        return userRepository.exists(integer);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findAll(Iterable<Integer> iterable) {
        return userRepository.findAll(iterable);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void delete(Integer integer) {
        if (exists(integer))
            userRepository.delete(integer);


    }

    @Override
    public void delete(User user) {
        if (exists(user.getUserId()))
            userRepository.delete(user.getUserId());

    }

    @Override
    public void delete(Iterable<? extends User> iterable) {
        userRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public void flush() {
        userRepository.flush();
    }

    @Override
    public void deleteInBatch(Iterable<User> iterable) {
        userRepository.deleteInBatch(iterable);
    }

    @Override
    public void deleteAllInBatch() {
        userRepository.deleteAllInBatch();
    }

    @Override
    public User getOne(Integer integer) {
        return userRepository.getOne(integer);
    }

    @Override
    public User saveAndFlush(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> save(Iterable<User> iterable) {
        return userRepository.save(iterable);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;

        try {
            // Get the user record from the DataSource
            log.debug("Retrieving user data for {}", username);
            user = this.userRepository.findUserByEmail(username);
        } catch (Exception e) {
            log.error("Failed to locate user with username of '" + username + "'", e);
        }

        if ( user == null ) {
            log.debug("Username not found:  {}", username);
            throw new UsernameNotFoundException("Invalid username");
        }

        log.debug("Found user data for {}", username);

        Collection<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
        try {
            // Get the user's roles from the DataSource
            Collection<Role> roles = this.userRoleRepository.findByUsers(user);

            // Add the user's roles to the collection of authorities
            for (Role role : roles) {
                log.debug("Adding role {} to user {}", role.getName(), username);
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        } catch (Exception e) {
            log.error("Failed to load user's roles", e);
        }
        UserModelUserDetails userDetails=new UserModelUserDetails(user);
        userDetails.setRoles(authorities);

        // Return the user details object required by Spring Security, containing the user's roles/authorities
        return userDetails;
    }


}
