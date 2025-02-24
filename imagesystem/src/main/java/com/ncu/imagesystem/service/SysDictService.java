package com.ncu.imagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.dto.SysDictItemDTO;
import com.ncu.imagesystem.entity.SysDictEntity;

import java.util.List;

public interface SysDictService extends IService<SysDictEntity> {

    List<SysDictItemDTO> getDictById(Integer dictId);

    List<SysDictItemDTO> getDictByCode(String dictCode);

    List<SysDictItemDTO> getAllDict();

}
