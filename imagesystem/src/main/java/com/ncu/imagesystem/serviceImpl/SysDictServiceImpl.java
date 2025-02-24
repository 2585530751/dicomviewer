package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.dto.SysDictItemDTO;
import com.ncu.imagesystem.entity.SysDictEntity;
import com.ncu.imagesystem.mapper.SysDictMapper;
import com.ncu.imagesystem.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDictEntity> implements SysDictService {
    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public List<SysDictItemDTO> getDictById(Integer dictId) {
        return sysDictMapper.getDictByDictId(dictId);
    }

    @Override
    public List<SysDictItemDTO> getDictByCode(String dictCode) {
        return sysDictMapper.getDictByDictCode(dictCode);
    }

    @Override
    public List<SysDictItemDTO> getAllDict() {
        return sysDictMapper.getAllDict();
    }
}
