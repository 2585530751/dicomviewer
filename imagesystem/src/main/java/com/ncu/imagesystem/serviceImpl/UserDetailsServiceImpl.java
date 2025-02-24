package com.ncu.imagesystem.serviceImpl;

import com.ncu.imagesystem.entity.SysGroupEntity;
import com.ncu.imagesystem.entity.SysPermissionEntity;
import com.ncu.imagesystem.entity.SysUserEntity;
import com.ncu.imagesystem.mapper.SysGroupMapper;
import com.ncu.imagesystem.service.SysPermissionService;
import com.ncu.imagesystem.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserEntity user = sysUserService.getUserByAccount(username);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        if(user == null) {
            userDetails.setEnabled(false);
            throw new UsernameNotFoundException("用户不存在！");
        }
        // 查询用户权限
        List<SysPermissionEntity> userAllPermissionsByUserId = sysPermissionService.getUserAllPermissionsByUserId(user.getUserId());
        if(!userAllPermissionsByUserId.isEmpty()){
            List<String> list = userAllPermissionsByUserId.stream().map(SysPermissionEntity::getPermissionName).distinct().collect(Collectors.toList());
            List<SimpleGrantedAuthority> authorities = list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            userDetails.setAuthorities(authorities);
        }
        return userDetails;
    }
}


