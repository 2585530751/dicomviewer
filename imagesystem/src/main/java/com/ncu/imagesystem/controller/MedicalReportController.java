package com.ncu.imagesystem.controller;

import com.ncu.imagesystem.entity.MedicalReportEntity;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.MedicalReportService;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("医学报告信息表controller")
@RestController
@RequestMapping("/report")
public class MedicalReportController {

    @Autowired
    private MedicalReportService medicalReportService;

    @ApiOperation("新增一条医学报告信息接口")
    @PostMapping("/addOne")
    public Result addOne(@RequestBody MedicalReportEntity report){
        boolean save = medicalReportService.save(report);
        if(save){
            return Result.success("医学报告信息添加成功！");
        }else {
            return Result.failed("医学报告信息添加失败！");
        }
    }

    @ApiOperation("根据reportId查询医学报告信息接口")
    @GetMapping("/getReportById")
    public Result getReportById(Integer reportId){
        MedicalReportEntity report = medicalReportService.getById(reportId);
        if(report != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), report);
        }else {
            return Result.failed("医学报告信息获取失败！");
        }
    }

    @ApiOperation("根据reportId更新医学报告信息接口")
    @PostMapping("/updateReportById")
    public Result updaterReportById(@RequestBody MedicalReportEntity report){
        boolean b = medicalReportService.updateById(report);
        if(b){
            return Result.success("医学报告信息更新成功！");
        }else {
            return Result.failed("医学报告信息更新失败！");
        }
    }

    @ApiOperation("导出医学报告文件接口")
    @PostMapping("/exportReportFile")
    public Result exportReportFile(@RequestBody MedicalReportEntity report){
        return null;
    }
}
