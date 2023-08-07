package com.abc.onboarding.security.services;

import com.abc.onboarding.customer.entity.Customer;
import com.abc.onboarding.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    CustomerRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer user = userRepository.findByUsername(username)
                                      .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username:" +
                                                                                               " " + username));

        return UserDetailsImpl.build(user);
    }

}
