package com.ncu.imagesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncu.imagesystem.dto.CommonPage;
import com.ncu.imagesystem.dto.ImagePageQuery;
import com.ncu.imagesystem.dto.ImageSetDTO;
import com.ncu.imagesystem.entity.SeriesEntity;

import java.util.List;

public interface SeriesService extends IService<SeriesEntity> {
    CommonPage<SeriesEntity> getImagePageByQuery(ImagePageQuery query);
    CommonPage<ImageSetDTO> getImageSetPageByQuery(ImagePageQuery query);
    SeriesEntity getById(Integer seriesId);
    boolean deleteById(Integer seriesId);

    boolean removeOneById(Integer id);
    boolean removeListByIds(List<Integer> ids);
}
