package com.ncu.imagesystem.tools;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ncu.imagesystem.entity.SysGroupEntity;
import com.ncu.imagesystem.entity.SysPermissionEntity;
import com.ncu.imagesystem.service.SysGroupService;
import com.ncu.imagesystem.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PermissionDataLoader implements CommandLineRunner {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysGroupService sysGroupService;

    @Override
    public void run(String... args) throws Exception {
        loadPermissionData();
    }

    private void loadPermissionData(){
        List<SysPermissionEntity> list = sysPermissionService.list();
        List<SysGroupEntity> groupList = sysGroupService.list();
        if(CollectionUtil.isNotEmpty(list)){
            for(SysPermissionEntity permission : list){
                String permissionName = permission.getPermissionName();
                if(StringUtils.isNotBlank(permissionName)) redisTemplate.opsForValue().set(permissionName, permission);
            }
        }
        if(CollectionUtil.isNotEmpty(groupList)){
            Map<String, SysGroupEntity> map = groupList.stream().collect(Collectors.toMap(SysGroupEntity::getGroupName, g -> g));
            redisTemplate.opsForHash().putAll("sysGroups", map);
        }
    }
}
