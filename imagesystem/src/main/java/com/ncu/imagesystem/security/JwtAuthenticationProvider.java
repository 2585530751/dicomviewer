package com.ncu.imagesystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        UserDetails userDetails = userDetailsService.loadUserByUsername(account);
//        if(passwordEncoder.matches(password,userDetails.getPassword())){
//            return new UsernamePasswordAuthenticationToken(account, password, userDetails.getAuthorities());
//        }
        if(!userDetails.isEnabled()){
            throw new AuthenticationServiceException("用户查找失败！");
        }
        if(bCryptPasswordEncoder.matches(password, userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(account, userDetails.getPassword(), userDetails.getAuthorities());
        }
        throw new BadCredentialsException("Error!!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
