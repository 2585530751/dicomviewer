package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.entity.SysGroupEntity;
import com.ncu.imagesystem.entity.SysPermissionEntity;
import com.ncu.imagesystem.mapper.SysGroupMapper;
import com.ncu.imagesystem.mapper.SysPermissionMapper;
import com.ncu.imagesystem.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermissionEntity> implements SysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysGroupMapper sysGroupMapper;

    @Override
    public List<SysPermissionEntity> getPermissionsByUserId(Integer userId) {
        return sysPermissionMapper.getPermissionsByUserId(userId);
    }

    @Override
    public List<SysPermissionEntity> getPermissionsByGroupId(List<Integer> groupId) {
        return sysPermissionMapper.getPermissionsByGroupId(groupId);
    }

    @Override
    public List<SysPermissionEntity> getUserAllPermissionsByUserId(Integer userId) {
        List<SysPermissionEntity> permissionsByUserId = getPermissionsByUserId(userId);
        List<SysGroupEntity> groupByUserId = sysGroupMapper.getGroupByUserId(userId);
        List<SysPermissionEntity> permissionsByGroupId = new ArrayList<>();
        if(groupByUserId != null && !groupByUserId.isEmpty()){
            List<Integer> list = groupByUserId.stream().map(SysGroupEntity::getGroupId).collect(Collectors.toList());
            permissionsByGroupId = getPermissionsByGroupId(list);
        }
        if(permissionsByUserId != null && !permissionsByUserId.isEmpty()) {
            permissionsByGroupId.addAll(permissionsByUserId);
        }
        Set<SysPermissionEntity> set = new TreeSet<>(Comparator.comparing(SysPermissionEntity::getPermissionId));
        set.addAll(permissionsByGroupId);
        return new ArrayList<>(set);
    }

    @Override
    public SysPermissionEntity getPermissionByName(String name) {
        LambdaQueryWrapper<SysPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPermissionEntity::getPermissionName, name);
        return sysPermissionMapper.selectOne(wrapper);
    }
}
