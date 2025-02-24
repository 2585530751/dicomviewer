package com.ncu.imagesystem.controller;

import com.ncu.imagesystem.entity.SysGroupEntity;
import com.ncu.imagesystem.entity.SysUserGroupRelationEntity;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.SysGroupService;
import com.ncu.imagesystem.service.SysUserGroupRelationService;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api("系统组信息controller")
@RestController
@RequestMapping("/group")
public class SysGroupController {
    @Autowired
    private SysGroupService sysGroupService;
    @Autowired
    private SysUserGroupRelationService sysUserGroupRelationService;

    @ApiOperation("新增一条系统组信息接口")
    @PostMapping("/addOne")
    public Result addOne(@RequestBody SysGroupEntity sysGroupEntity){
        sysGroupEntity.setCreateTime(new Date());
        boolean save = sysGroupService.save(sysGroupEntity);
        if(save){
            return Result.success("系统组信息添加成功！");
        }else {
            return Result.failed("系统组信息添加失败！");
        }
    }

    @ApiOperation("根据groupId查询系统组信息接口")
    @GetMapping("/getGroupById")
    public Result getGroupById(Integer groupId){
        SysGroupEntity sysGroupEntity = sysGroupService.getById(groupId);
        if(sysGroupEntity != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), sysGroupEntity);
        }else {
            return Result.failed("系统组信息获取失败！");
        }
    }

    @ApiOperation("根据groupId更新系统组信息接口")
    @PostMapping("/updateGroupById")
    public Result updateGroupById(@RequestBody SysGroupEntity sysGroupEntity){
        boolean b = sysGroupService.updateById(sysGroupEntity);
        if(b){
            return Result.success("系统组信息更新成功！");
        }else {
            return Result.failed("系统组信息更新失败！");
        }
    }

    @ApiOperation("给系统组增加用户信息接口")
    @PostMapping("/addUserToGroup")
    public Result addUserToGroup(@RequestBody SysUserGroupRelationEntity sysUserGroupRelation){
        boolean save = sysUserGroupRelationService.save(sysUserGroupRelation);
        if(save){
            return Result.success("系统组用户信息添加成功！");
        }else {
            return Result.failed("系统组用户信息添加失败！");
        }
    }
}
