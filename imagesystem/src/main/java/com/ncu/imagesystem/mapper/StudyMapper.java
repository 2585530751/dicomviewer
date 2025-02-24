package com.ncu.imagesystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncu.imagesystem.dto.*;
import com.ncu.imagesystem.entity.StudyEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudyMapper extends BaseMapper<StudyEntity> {
    Long countPatient(PatientStudyQuery query);
    List<StudyDTO> getPatientStudyPageByDepartmentId(PatientStudyQuery query);

    Long countStudy(StudySeriesQuery query);

    List<StudySeriesDTO> getStudySeriesPageByDepartmentId(StudySeriesQuery query);

    Long countSeries(SeriesImageQuery query);

    List<SeriesImageDTO> getSeriesImagePageByDepartmentId(SeriesImageQuery query);
}
