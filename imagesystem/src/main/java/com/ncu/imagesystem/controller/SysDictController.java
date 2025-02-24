package com.ncu.imagesystem.controller;

import com.ncu.imagesystem.dto.SysDictItemDTO;
import com.ncu.imagesystem.entity.SysDictEntity;
import com.ncu.imagesystem.entity.SysDictItemEntity;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.service.SysDictItemService;
import com.ncu.imagesystem.service.SysDictService;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("字典信息表controller")
@RestController
@RequestMapping("/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private SysDictItemService sysDictItemService;

    @ApiOperation("根据dictId查询字典信息接口")
    @GetMapping("/getDictById")
    public Result getDictById(Integer dictId){
        List<SysDictItemDTO> dict = sysDictService.getDictById(dictId);
        if(dict != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), dict);
        }else {
            return Result.failed("字典信息获取失败！");
        }
    }

    @ApiOperation("根据dictCode查询字典信息接口")
    @GetMapping("/getDictByCode")
    public Result getDictByCode(String dictCode){
        List<SysDictItemDTO> dict = sysDictService.getDictByCode(dictCode);
        if(dict != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), dict);
        }else {
            return Result.failed("字典信息获取失败！");
        }
    }

    @ApiOperation("获取所有字典信息接口")
    @GetMapping("/getAllDict")
    public Result getAllDict(){
        List<SysDictItemDTO> dict = sysDictService.getAllDict();
        if(dict != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), dict);
        }else {
            return Result.failed("字典信息获取失败！");
        }
    }

    @ApiOperation("新增字典信息接口")
    @PostMapping("/addDict")
    public Result addDict(@RequestBody SysDictEntity dict){
        dict.setIsDeleted(0);
        boolean save = sysDictService.save(dict);
        if(save){
            return Result.success(ResultCode.SUCCESS.getMsg(), dict);
        }else {
            return Result.failed("字典信息新增失败！");
        }
    }

    @ApiOperation("新增字典项信息接口")
    @PostMapping("/addDictItems")
    public Result addDictItems(@RequestBody List<SysDictItemEntity> list){
        list.forEach(o->o.setStatus(1));
        boolean save = sysDictItemService.saveBatch(list);
        if(save){
            return Result.success(ResultCode.SUCCESS.getMsg(), list);
        }else {
            return Result.failed("字典项信息新增失败！");
        }
    }
}
