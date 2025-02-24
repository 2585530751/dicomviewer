package com.ncu.imagesystem.controller;

import com.ncu.imagesystem.entity.OrganizationEntity;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.OrganizationService;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("组织信息表controller")
@RestController
@RequestMapping("/org")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @ApiOperation("新增一条组织信息接口")
    @PostMapping("/addOne")
    public Result addOne(@RequestBody OrganizationEntity organization){
        boolean save = organizationService.save(organization);
        if(save){
            return Result.success("组织信息添加成功！");
        }else {
            return Result.failed("组织信息添加失败！");
        }
    }

    @ApiOperation("根据orgId查询组织信息接口")
    @GetMapping("/getOrganizationById")
    public Result getOrganizationById(Integer orgId){
        OrganizationEntity organization = organizationService.getById(orgId);
        if(organization != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), organization);
        }else {
            return Result.failed("组织信息获取失败！");
        }
    }

    @ApiOperation("根据orgId更新组织信息接口")
    @PostMapping("/updateOrganizationById")
    public Result updateOrganizationById(@RequestBody OrganizationEntity organization){
        boolean b = organizationService.updateById(organization);
        if(b){
            return Result.success("组织信息更新成功！");
        }else {
            return Result.failed("组织信息更新失败！");
        }
    }
}
