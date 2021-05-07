package com.chpok.logiweb.security;

import com.chpok.logiweb.dao.UserDao;
import com.chpok.logiweb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> user = userDao.findUserByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsImpl(user.get());
    }
}
