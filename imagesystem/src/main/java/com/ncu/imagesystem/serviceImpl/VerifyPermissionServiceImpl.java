package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service("vps")
public class VerifyPermissionServiceImpl{

    private static final String PERMISSION_SEP = ",";

    /**
     * 判断当前用户是否具有permission权限
     * @param permission permissionName
     * @return boolean
     */
    public boolean hasPermission(String permission) {
        if (StringUtils.isBlank(permission)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(authorities.isEmpty()) return false;
        Set<String> set = new HashSet<>();
        for (GrantedAuthority ga : authorities) {
            set.add(ga.getAuthority());
        }
        return hasPermissions(set, permission);
    }

    /**
     * 判断当前用户是否具有permissions中的至少一个权限
     * @param permissions permissionName以,分隔
     * @return boolean
     */
    public boolean hasAnyPermission(String permissions) {
        if (StringUtils.isBlank(permissions)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(authorities.isEmpty()) return false;
        Set<String> set = new HashSet<>();
        for (GrantedAuthority ga : authorities) {
            set.add(ga.getAuthority());
        }
        for(String permission : permissions.split(PERMISSION_SEP)){
            if(permission != null && hasPermissions(set, permission)){
                return true;
            }
        }
        return false;
    }

    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(permission.trim());
    }
}
