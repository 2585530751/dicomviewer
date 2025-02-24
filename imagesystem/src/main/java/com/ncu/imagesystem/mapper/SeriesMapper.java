package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.dto.ImagePageQuery;
import com.ncu.imagesystem.dto.ImageSetDTO;
import com.ncu.imagesystem.entity.SeriesEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SeriesMapper extends BaseMapper<SeriesEntity> {
    List<ImageSetDTO> getImageSetPageByQuery(ImagePageQuery query);
    Long count(ImagePageQuery query);
}
