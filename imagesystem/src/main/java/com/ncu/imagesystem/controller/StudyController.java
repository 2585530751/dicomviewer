package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ncu.imagesystem.dto.*;
import com.ncu.imagesystem.entity.*;
import com.ncu.imagesystem.enums.Modality2DepartmentName;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.mapper.ImageDepartmentRelationMapper;
import com.ncu.imagesystem.mapper.StudyDepartmentRelationMapper;
import com.ncu.imagesystem.service.*;
import com.ncu.imagesystem.tools.Result;
import com.ncu.imagesystem.tools.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api("检查信息表controller")
@RestController
@RequestMapping("/study")
public class StudyController {

    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ImageDoctorRelationService imageDoctorRelationService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private StudyService studyService;
    @Autowired
    private Environment environment;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SysUserPermRelationService sysUserPermRelationService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private StudyDepartmentRelationMapper studyDepartmentRelationMapper;
    @Autowired
    private ImageDepartmentRelationMapper seriesDepartmentRelationMapper;

    @ApiOperation("上传检查文件接口")
    @PreAuthorize("@vps.hasAnyPermission('admin,doctor')")
    @PostMapping(value = "/uploadSeries", consumes ="multipart/form-data")
    public Result uploadStudy(@RequestPart("files") MultipartFile[] files, @RequestPart(value = "seriesInfo", required = false)String info){
        if(files == null || files[0] == null){
            return Result.failed("检查文件为空！");
        }

        // 获取额外的科室信息
        JSONObject jsonObject = JSONUtil.parseObj(info);
        String departmentNameList = jsonObject.get("departmentNameList", String.class);
        List<SysPermissionEntity> permissionList = new ArrayList<>();
        if(StringUtils.isNotBlank(departmentNameList)){
            List<String> list = JSONUtil.toList(departmentNameList, String.class);
            for(String departmentName : list){
                SysPermissionEntity permission = (SysPermissionEntity) redisTemplate.opsForValue().get(departmentName);
                permissionList.add(permission);
            }
        }
        setPermissionListByFiles(permissionList, files);

        JSONObject userJson = UserUtils.getUserJsonObject();
        Integer patientId = studyService.checkAndSavePatientData(files, permissionList, userJson);
        if(patientId < 0) return Result.failed("检查文件上传失败！");
        PatientEntity patient = patientService.getById(patientId);
        Integer studyId = studyService.saveStudyData(files, patient, permissionList, userJson);
        if(studyId < 0) return Result.failed("检查文件上传失败！");
        boolean seriesData = studyService.saveSeriesData(files, patient, userJson, studyId, permissionList);

        if(seriesData){
            return Result.success("检查文件上传成功！");
        }else {
            return Result.failed("检查文件上传失败！");
        }
    }

    private void setPermissionListByFiles(List<SysPermissionEntity> permissionList, MultipartFile[] files){
        Set<String> modalitySet = new HashSet<>();
        for(MultipartFile file : files){
            String modality = getModality(file);
            modalitySet.add(modality);
        }
        for(String modality : modalitySet){
            String departmentName = Modality2DepartmentName.getDepartmentByModality(modality);
            if(departmentName != null){
                SysPermissionEntity permission = (SysPermissionEntity)redisTemplate.opsForValue().get(departmentName);
                permissionList.add(permission);
            }
        }
    }

    private String getModality(MultipartFile file){
        InputStream inputStream = null;
        DicomInputStream dis = null;
        String modality = null;
        try{
            inputStream = file.getInputStream();
            dis = new DicomInputStream(inputStream);
            Attributes attrs = dis.readDataset(-1, -1);
            modality = attrs.getString(Tag.Modality);
        } catch (IOException e){
            System.out.println("File open failed!");
        }finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    System.out.println("DicomInputStream close failed!");
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("InputStream close failed!");
                }
            }
        }
        return modality;
    }

    private List<Integer> getDepartmentIdList(){
        JSONObject userJson = UserUtils.getUserJsonObject();
        Integer userId = userJson.getInt("userId");
        LambdaQueryWrapper<SysUserPermRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserPermRelationEntity::getUserId, userId);
        List<SysUserPermRelationEntity> list = sysUserPermRelationService.list(wrapper);
        return list.stream().map(SysUserPermRelationEntity::getPermissionId).collect(Collectors.toList());
    }

    @ApiOperation("查询病人和检查信息分页接口")
    @PostMapping(value = "/getPatientStudyPage")
    public Result getPatientStudyPage(@RequestBody PatientStudyQuery query){
        List<Integer> departmentIdList = getDepartmentIdList();
        query.setDepartmentIdList(departmentIdList);
        CommonPage<StudyDTO> patientStudyDTOCommonPage = studyService.getPatientStudyPageByDepartmentId(query);
        return Result.success(ResultCode.SUCCESS.getMsg(), patientStudyDTOCommonPage);
    }

    @ApiOperation("查询检查和序列信息分页接口")
    @PostMapping(value = "/getStudySeriesPage")
    public Result getStudySeriesPage(@RequestBody StudySeriesQuery query){
        List<Integer> departmentIdList = getDepartmentIdList();
        query.setDepartmentIdList(departmentIdList);
        CommonPage<StudySeriesDTO> studySeriesDTOCommonPage = studyService.getStudySeriesPageByDepartmentId(query);
        return Result.success(ResultCode.SUCCESS.getMsg(), studySeriesDTOCommonPage);
    }

    @ApiOperation("查询序列和图像信息分页接口")
    @PostMapping(value = "/getSeriesImagePage")
    public Result getSeriesImagePage(@RequestBody SeriesImageQuery query){
        List<Integer> departmentIdList = getDepartmentIdList();
        query.setDepartmentIdList(departmentIdList);
        CommonPage<SeriesImageDTO> seriesImageDTOCommonPage = studyService.getSeriesImagePageByDepartmentId(query);
        return Result.success(ResultCode.SUCCESS.getMsg(), seriesImageDTOCommonPage);
    }

    @ApiOperation("新增一个检查接口")
    @PostMapping(value = "/addOneStudy")
    public Result addOneStudy(@RequestBody OneStudyReq oneStudyReq){
        boolean b = studyService.saveOneStudy(oneStudyReq.getStudy(), oneStudyReq.getDepartmentIdList());
        if(b){
            return Result.success("检查新增成功！");
        }else {
            return Result.failed("检查新增失败！");
        }
    }

    @ApiOperation("删除单个检查接口")
    @GetMapping("/deleteOneStudy")
    public Result deleteOneStudy(Integer studyId){
        LambdaQueryWrapper<SeriesEntity> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SeriesEntity::getStudyId, studyId);
        List<SeriesEntity> list = seriesService.list(wrapper1);
        if(list != null && !list.isEmpty()) {
            List<Integer> seriesIdList = list.stream()
                    .map(SeriesEntity::getSeriesId).collect(Collectors.toList());
            LambdaQueryWrapper<ImageEntity> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.in(ImageEntity::getSeriesId, seriesIdList);
            imageService.removeList(wrapper2);
            seriesService.removeListByIds(seriesIdList);
            LambdaQueryWrapper<ImageDepartmentRelationEntity> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.in(ImageDepartmentRelationEntity::getSeriesId, seriesIdList);
            seriesDepartmentRelationMapper.delete(wrapper3);
        }
        LambdaQueryWrapper<StudyDepartmentRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyDepartmentRelationEntity::getStudyId, studyId);
        studyDepartmentRelationMapper.delete(wrapper);
        boolean b = studyService.removeById(studyId);
        if(b){
            return Result.success("检查删除成功！");
        }else {
            return Result.failed("检查删除失败！");
        }
    }

    @ApiOperation("删除多个检查接口")
    @PostMapping("/deleteStudies")
    public Result deleteStudies(@RequestBody List<Integer> ids){
        LambdaQueryWrapper<SeriesEntity> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(SeriesEntity::getStudyId, ids);
        List<SeriesEntity> list = seriesService.list(wrapper1);
        if(list != null && !list.isEmpty()) {
            List<Integer> seriesIdList = list.stream()
                    .map(SeriesEntity::getSeriesId).collect(Collectors.toList());
            LambdaQueryWrapper<ImageEntity> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.in(ImageEntity::getSeriesId, seriesIdList);
            imageService.removeList(wrapper2);
            seriesService.removeListByIds(seriesIdList);
            LambdaQueryWrapper<ImageDepartmentRelationEntity> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.in(ImageDepartmentRelationEntity::getSeriesId, seriesIdList);
            seriesDepartmentRelationMapper.delete(wrapper3);
        }
        LambdaQueryWrapper<StudyDepartmentRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StudyDepartmentRelationEntity::getStudyId, ids);
        studyDepartmentRelationMapper.delete(wrapper);
        boolean b = studyService.removeByIds(ids);
        if(b){
            return Result.success("检查删除成功！");
        }else {
            return Result.failed("检查删除失败！");
        }
    }

    @ApiOperation("更新检查接口")
    @PostMapping("/updateOneStudy")
    public Result updateOneStudy(@RequestBody StudyEntity study){
        boolean b = studyService.updateById(study);
        if(b){
            return Result.success("检查更新成功！");
        }else {
            return Result.failed("检查更新失败！");
        }
    }
}
