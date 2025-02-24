package com.ncu.imagesystem.controller;

import com.ncu.imagesystem.entity.DoctorEntity;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.DoctorService;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("医生信息表controller")
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @ApiOperation("新增一条医生信息接口")
    @PostMapping("/addOne")
    public Result addOne(@RequestBody DoctorEntity doctor){
        boolean save = doctorService.save(doctor);
        if(save){
            return Result.success("医生信息添加成功！");
        }else {
            return Result.failed("医生信息添加失败！");
        }
    }

    @ApiOperation("根据doctorId查询医生信息接口")
    @GetMapping("/getDoctorById")
    public Result getDoctorById(Integer doctorId){
        DoctorEntity doctor = doctorService.getById(doctorId);
        if(doctor != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), doctor);
        }else {
            return Result.failed("医生信息获取失败！");
        }
    }

    @ApiOperation("根据doctorId更新医生信息接口")
    @PostMapping("/updateDoctorById")
    public Result updateDoctorById(@RequestBody DoctorEntity doctor){
        boolean b = doctorService.updateById(doctor);
        if(b){
            return Result.success("医生信息更新成功！");
        }else {
            return Result.failed("医生信息更新失败！");
        }
    }
}
