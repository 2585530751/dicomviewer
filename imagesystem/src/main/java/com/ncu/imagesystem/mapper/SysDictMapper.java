package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.dto.SysDictItemDTO;
import com.ncu.imagesystem.entity.SysDictEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysDictMapper extends BaseMapper<SysDictEntity> {

    List<SysDictItemDTO> getDictByDictId(Integer dictId);

    List<SysDictItemDTO> getDictByDictCode(String dictCode);

    List<SysDictItemDTO> getAllDict();
}
