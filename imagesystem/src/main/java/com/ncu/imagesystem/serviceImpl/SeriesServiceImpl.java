package com.ncu.imagesystem.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.dto.CommonPage;
import com.ncu.imagesystem.dto.ImagePageQuery;
import com.ncu.imagesystem.dto.ImageSetDTO;
import com.ncu.imagesystem.entity.ImageEntity;
import com.ncu.imagesystem.entity.SeriesEntity;
import com.ncu.imagesystem.mapper.SeriesMapper;
import com.ncu.imagesystem.mapper.ImageMapper;
import com.ncu.imagesystem.service.ModelResultService;
import com.ncu.imagesystem.service.SeriesService;
import com.ncu.imagesystem.tools.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeriesServiceImpl extends ServiceImpl<SeriesMapper, SeriesEntity> implements SeriesService {
    @Autowired
    private SeriesMapper seriesMapper;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private Environment environment;
    @Autowired
    private ModelResultService modelResultService;

    @Override
    public CommonPage<SeriesEntity> getImagePageByQuery(ImagePageQuery query) {
        LambdaQueryWrapper<SeriesEntity> wrapper = new LambdaQueryWrapper<>();
        // 分页查询条件
        if(query.getPatientName() != null){
            wrapper.like(SeriesEntity::getPatientName, query.getPatientName());
        }
        wrapper.eq(SeriesEntity::getIsDeleted, 0);
        // 设置分页信息
        Page<SeriesEntity> page = new Page<>(query.getCurrent(), query.getPageSize());
        // 执行分页查询
        Page<SeriesEntity> patientPage = this.page(page, wrapper);
        // 获取分页查询结果
        if(patientPage == null) return null;
        return new CommonPage<>(patientPage.getRecords(),
                patientPage.getTotal(),
                patientPage.getSize(),
                patientPage.getCurrent(),
                patientPage.getPages());
    }

    @Override
    public CommonPage<ImageSetDTO> getImageSetPageByQuery(ImagePageQuery query) {
        Long total = seriesMapper.count(query);
        Long current = query.getCurrent();
        Long pageSize = query.getPageSize();
        Long totalPage = total % pageSize == 0 ?  total / pageSize : (total / pageSize) + 1;
        Long start = (current-1) * pageSize;
        query.setCurrent(start);
        List<ImageSetDTO> imageSetPageByQuery = seriesMapper.getImageSetPageByQuery(query);
        return new CommonPage<>(imageSetPageByQuery, total, pageSize, current, totalPage);
    }

    @Override
    public SeriesEntity getById(Integer seriesId) {
        LambdaQueryWrapper<SeriesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SeriesEntity::getSeriesId, seriesId);
        return this.getOne(wrapper);
    }

    @Override
    public boolean deleteById(Integer seriesId) {
        SeriesEntity series = new SeriesEntity();
        series.setSeriesId(seriesId);
        series.setIsDeleted(1);
        LambdaQueryWrapper<ImageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImageEntity::getSeriesId, seriesId);
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setIsDeleted(1);
        imageMapper.update(imageEntity, wrapper);
        return this.updateById(series);
    }

    @Override
    public boolean removeOneById(Integer id) {
        SeriesEntity series = this.getById(id);
        String seriesPath = series.getSeriesPath();
        if(StringUtils.isNotBlank(seriesPath)) {
            String filePath = environment.getProperty("web.upload-path") + File.separator + seriesPath;
            File file = new File(filePath);
            FileUtil.deleteFileOrDir(file);
        }
        ArrayList<Integer> seriesIds = new ArrayList<>();
        seriesIds.add(id);
        modelResultService.removeListBySeriesIds(seriesIds);
        return this.removeById(id);
    }

    @Override
    public boolean removeListByIds(List<Integer> ids) {
        List<SeriesEntity> seriesList = this.listByIds(ids);
        String property = environment.getProperty("web.upload-path");
        for(SeriesEntity series : seriesList){
            String seriesPath = series.getSeriesPath();
            if(StringUtils.isNotBlank(seriesPath)) {
                String filePath = property + File.separator + seriesPath;
                File file = new File(filePath);
                FileUtil.deleteFileOrDir(file);
            }
        }
        modelResultService.removeListBySeriesIds(ids);
        return this.removeByIds(ids);
    }
}
