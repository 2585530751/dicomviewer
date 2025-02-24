package com.ncu.imagesystem.serviceImpl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncu.imagesystem.dto.*;
import com.ncu.imagesystem.entity.*;
import com.ncu.imagesystem.enums.Modality2DepartmentName;
import com.ncu.imagesystem.mapper.ImageDepartmentRelationMapper;
import com.ncu.imagesystem.mapper.PatientDepartmentRelationMapper;
import com.ncu.imagesystem.mapper.StudyMapper;
import com.ncu.imagesystem.mapper.StudyDepartmentRelationMapper;
import com.ncu.imagesystem.service.*;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.MyConstant;
import com.ncu.imagesystem.tools.UserUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class StudyServiceImpl extends ServiceImpl<StudyMapper, StudyEntity> implements StudyService {
    @Autowired
    private PatientService patientService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Environment environment;
    @Autowired
    private StudyMapper studyMapper;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private PatientDepartmentRelationMapper patientDepartmentRelationMapper;
    @Autowired
    private StudyDepartmentRelationMapper studyDepartmentRelationMapper;
    @Autowired
    private ImageDepartmentRelationMapper imageDepartmentRelationMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer checkAndSavePatientData(MultipartFile[] files, List<SysPermissionEntity> permissionList, JSONObject userJson){
        Integer ret;
        DicomInputStream dis = null;
        InputStream inputStream = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hhmmss");
        try {
            inputStream = files[0].getInputStream();
            dis = new DicomInputStream(inputStream);
            Attributes attrs = dis.readDataset(-1, -1);
            String patientIdDcm = attrs.getString(Tag.PatientID);
            String patientName = attrs.getString(Tag.PatientName);
            String patientBirthDate = attrs.getString(Tag.PatientBirthDate);
            String patientBirthTime = attrs.getString(Tag.PatientBirthTime);
            LocalDateTime birthDateTime = null;
            if(StringUtils.isNotBlank(patientBirthDate) && StringUtils.isNotBlank(patientBirthTime)){
                // 去掉时间字符串中的毫秒部分
                String truncatedTime = patientBirthTime.substring(0, patientBirthTime.lastIndexOf("."));

                // 创建日期和时间的格式化器
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                // 解析日期和时间
                java.time.LocalDate date = LocalDate.parse(patientBirthDate, dateFormatter);
                java.time.LocalTime time = LocalTime.parse(truncatedTime, timeFormatter);
                // 将日期和时间组合成 LocalDateTime
                birthDateTime = LocalDateTime.of(date, time);
            }
            String patientAddress= attrs.getString(Tag.PatientAddress);
//            String patientAge = attrs.getString(Tag.PatientAge);
            String patientSex = attrs.getString(Tag.PatientSex);
            if(attrs.getString(Tag.PatientSex).equals("M")||attrs.getString(Tag.PatientSex).equals("F")){
                patientSex =  attrs.getString(Tag.PatientSex).equals("M")? "男性":"女性";
            }
            String patientWeight = attrs.getString(Tag.PatientWeight);
            String patientTelephoneNumbers = attrs.getString(Tag.PatientTelephoneNumbers);
            LambdaQueryWrapper<PatientEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PatientEntity::getPatientIdDcm, patientIdDcm);
            PatientEntity one = patientService.getOne(wrapper);
            if(one != null){
                one.setPatientName(patientName);
                one.setDateOfBirth(birthDateTime);
                one.setAddress(patientAddress);
                one.setPatientGender(patientSex);
                if(StringUtils.isNotBlank(patientWeight))
                    one.setPatientWeight(Float.parseFloat(patientWeight));
                one.setTelephoneNumber(patientTelephoneNumbers);
                patientService.updateById(one);
                ret = one.getPatientId();
            }else{
                PatientEntity patient = new PatientEntity();
                patient.setPatientIdDcm(patientIdDcm);
                patient.setPatientName(patientName);
                patient.setDateOfBirth(birthDateTime);
                patient.setAddress(patientAddress);
                patient.setPatientGender(patientSex);
                patient.setCreateTime(LocalDateTime.now());
                patient.setCreatorId(userJson.getInt("userId"));
                if(StringUtils.isNotBlank(patientWeight))
                    patient.setPatientWeight(Float.parseFloat(patientWeight));
                patient.setTelephoneNumber(patientTelephoneNumbers);
                patientService.save(patient);
                ret = patient.getPatientId();
            }
            // 增加科室关联
            List<PatientDepartmentRelationEntity> relList = getPDRelListByGroupList(permissionList, ret);
            if(!relList.isEmpty()){
                boolean b = patientDepartmentRelationMapper.saveOrUpdateBatch(relList);
            }
        }catch (IOException exception){
            System.out.println("DicomInputStream open or read failed!");
            ret = -1;
        } catch (DateTimeParseException e) {
            System.out.println("dateFormat parse failed!");
            ret = -1;
        } finally {
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
        return ret;
    }

    private List<PatientDepartmentRelationEntity> getPDRelListByGroupList(List<SysPermissionEntity> sysPermissionList, Integer patientId){
        List<PatientDepartmentRelationEntity> relList = new ArrayList<>();
        for(SysPermissionEntity permission : sysPermissionList){
            PatientDepartmentRelationEntity relation = new PatientDepartmentRelationEntity();
            relation.setPatientId(patientId);
            relation.setDepartmentId(permission.getPermissionId());
            relation.setDepartmentName(permission.getPermissionName());
            relation.setDepartmentDesc(permission.getDescription());
            relList.add(relation);
        }
        return relList;
    }

    @Override
    public Integer saveStudyData(MultipartFile[] files, PatientEntity patient, List<SysPermissionEntity> permissionList, JSONObject userJson) {
        Integer ret = -1;
        DicomInputStream dis = null;
        InputStream inputStream = null;
        try {
            inputStream = files[0].getInputStream();
            dis = new DicomInputStream(inputStream);
            Attributes attrs = dis.readDataset(-1, -1);

            StudyEntity study = new StudyEntity();
            study.setStudyIdDcm(attrs.getString(Tag.StudyID));
            String studyInstanceUid = attrs.getString(Tag.StudyInstanceUID);
            study.setStudyInstanceUid(studyInstanceUid);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 使用DateTimeFormatter解析日期字符串并创建LocalDate对象
            LocalDate studyDate = LocalDate.parse(attrs.getString(Tag.StudyDate), formatter);

            // 尝试将字符串转换为double
            double timeInSecondsDouble = Double.parseDouble(attrs.getString(Tag.StudyTime));
            // 因为LocalTime不支持秒的小数部分，所以我们只取整数秒
            long timeInSecondsLong = (long) Math.floor(timeInSecondsDouble);
            // 将秒转换为小时、分钟和秒
            int hours = (int) (timeInSecondsLong / 10000);
            int minutes = (int) ((timeInSecondsLong % 10000) / 100);
            int seconds = (int) (timeInSecondsLong %100);
            // 创建LocalTime对象
            LocalTime studyTime = LocalTime.of(hours, minutes, seconds);

            study.setStudyDate(studyDate);
            study.setStudyTime(studyTime);
            study.setStudyDescription(attrs.getString(Tag.StudyDescription));
            study.setAccessionNumber(attrs.getString(Tag.AccessionNumber));
            study.setModalitiesInStudy(attrs.getString(Tag.ModalitiesInStudy));
            study.setBodyPartExamined(attrs.getString(Tag.BodyPartExamined));
            if (attrs.getString(Tag.PatientAge) != null && !attrs.getString(Tag.PatientAge).isEmpty()) {
                // 去除字符串末尾的'Y'（如果存在）和可能的空格
                String trimmedInput = attrs.getString(Tag.PatientAge).trim();
                if (trimmedInput.endsWith("Y")) {
                    trimmedInput = trimmedInput.substring(0, trimmedInput.length() - 1);
                }

                study.setPatientAge( Integer.parseInt(trimmedInput));
            }
            study.setPatientId(patient.getPatientId());

            study.setCreateTime(LocalDateTime.now());
            study.setCreatorId(userJson.getInt("userId"));

            if(StringUtils.isNotBlank(studyInstanceUid)){
                LambdaQueryWrapper<StudyEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(StudyEntity::getStudyInstanceUid, studyInstanceUid);
                wrapper.orderByDesc(StudyEntity::getStudyId);
                List<StudyEntity> list = this.list(wrapper);
                if(list != null && !list.isEmpty()){
                    StudyEntity one = list.get(0);
                    study.setStudyId(one.getStudyId());
                    this.updateById(study);
                    ret = study.getStudyId();
                }else{
                    this.save(study);
                    ret = study.getStudyId();
                }
            }else{
                this.save(study);
                ret = study.getStudyId();
            }
        }catch (IOException exception){
            System.out.println("DicomInputStream open or read failed!");
            ret = -1;
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
        if(ret != -1){
            List<StudyDepartmentRelationEntity> relList = getSDRelListByGroupList(permissionList, ret);
            if(!relList.isEmpty()){
                boolean b = studyDepartmentRelationMapper.saveOrUpdateBatch(relList);
            }
        }
        return ret;
    }

    @Override
    public boolean saveOneStudy(StudyEntity study, List<Integer> departmentIdList) {
        JSONObject userJson = UserUtils.getUserJsonObject();
        study.setCreatorId(userJson.getInt("userId"));
        study.setCreateTime(LocalDateTime.now());
        boolean save = this.save(study);
        boolean b = false;
        if (save){
            List<SysPermissionEntity> permissionList = sysPermissionService.listByIds(departmentIdList);
            List<StudyDepartmentRelationEntity> list = getSDRelListByGroupList(permissionList, study.getStudyId());
            b = studyDepartmentRelationMapper.saveOrUpdateBatch(list);
        }
        return save && b;
    }

    @Override
    public boolean saveOneSeries(SeriesEntity series, List<Integer> departmentIdList) {
        JSONObject userJson = UserUtils.getUserJsonObject();
        series.setCreatorId(userJson.getInt("userId"));
        series.setCreatorName(userJson.getStr("name"));
        series.setCreateTime(LocalDateTime.now());
        series.setSeriesStatus(0);
        series.setIsDeleted(0);
        if(series.getPatientId() != null){
            PatientEntity patient = patientService.getById(series.getPatientId());
            series.setPatientName(patient.getPatientName());
        }
        boolean save = seriesService.save(series);
        boolean b = false;
        if (save){
            List<SysPermissionEntity> permissionList = sysPermissionService.listByIds(departmentIdList);
            List<ImageDepartmentRelationEntity> relList = getIDRelListByGroupList(permissionList, series.getSeriesId());
            if(!relList.isEmpty()) {
                b = imageDepartmentRelationMapper.saveOrUpdateBatch(relList);
            }
            String departmentName = Modality2DepartmentName.getDepartmentByModality(series.getSeriesModality());
            if(departmentName != null){
                List<SysPermissionEntity> modalityPermissionList = new ArrayList<>();
                SysPermissionEntity permission = (SysPermissionEntity)redisTemplate.opsForValue().get(departmentName);
                modalityPermissionList.add(permission);
                // 保存序列对应
                relList = getIDRelListByGroupList(modalityPermissionList, series.getSeriesId());
                imageDepartmentRelationMapper.saveOrUpdateBatch(relList);
                // 保存检查对应
                if (series.getStudyId() != null) {
                    List<StudyDepartmentRelationEntity> list = getSDRelListByGroupList(modalityPermissionList, series.getStudyId());
                    studyDepartmentRelationMapper.saveOrUpdateBatch(list);
                }
                // 保存病人对应
                if (series.getPatientId() != null) {
                    List<PatientDepartmentRelationEntity> list = getPDRelListByGroupList(modalityPermissionList, series.getPatientId());
                    patientDepartmentRelationMapper.saveOrUpdateBatch(list);
                }
            }
        }
        return save && b;
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

    private List<SysPermissionEntity> getPermListByUserId(Integer userId){
        List<SysPermissionEntity> permissionList = sysPermissionService.getPermissionsByUserId(userId);
        HashSet<Integer> patientIdSet = new HashSet<>();
        permissionList.forEach(p-> patientIdSet.add(p.getPermissionParentId()));
        permissionList.removeIf(p -> patientIdSet.contains(p.getPermissionId()));
        return permissionList;
    }

    private void setImagesRel(List<SysPermissionEntity> permissionList, Integer patientId, Integer studyId, Integer seriesId){
        List<PatientDepartmentRelationEntity> relList1 = getPDRelListByGroupList(permissionList, patientId);
        if(!relList1.isEmpty()){
            patientDepartmentRelationMapper.saveOrUpdateBatch(relList1);
        }
        List<StudyDepartmentRelationEntity> relList2 = getSDRelListByGroupList(permissionList, studyId);
        if(!relList2.isEmpty()){
            studyDepartmentRelationMapper.saveOrUpdateBatch(relList2);
        }
        List<ImageDepartmentRelationEntity> relList3 = getIDRelListByGroupList(permissionList, seriesId);
        if(!relList3.isEmpty()) {
            imageDepartmentRelationMapper.saveOrUpdateBatch(relList3);
        }
    }
    @Override
    public boolean addImages(MultipartFile[] files, ImagesReq imagesReq) {
        boolean b = false,s=false;
        InputStream[] inputStreams = new InputStream[files.length];
        try {
            for (int i=0; i<files.length; i++) inputStreams[i] = files[i].getInputStream();
            // 检查相关id
            Integer patientId = imagesReq.getPatientId();
            Integer studyId = imagesReq.getStudyId();
            Integer seriesId = imagesReq.getSeriesId();
            if(patientId == null || studyId == null || seriesId == null ) return false;

            String originalFilename = files[0].getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")+1);

            // 保存文件
            String path = FileUtil.saveFiles(files, MyConstant.IMAGE_DIR, environment.getProperty("web.upload-path"));
            if(StringUtils.isBlank(path)) return false;

            final String sep = File.separator;
            int i = path.lastIndexOf(sep);
            String filePrefix = path.substring(i+1);

            PatientEntity patient = patientService.getById(patientId);
            SeriesEntity series = seriesService.getById(seriesId);
            JSONObject userJson = UserUtils.getUserJsonObject();
            Integer userId = userJson.getInt("userId");

            if("dcm".equals(ext)){
                b = saveSingleImageData(files, patient, series, path, userId);
            }else{
                List<ImageEntity> list = new ArrayList<>();
                for(MultipartFile f : files){
                    ImageEntity image = imagesReq.getImage();
                    if(image == null) image = new ImageEntity();
                    String oldName = f.getOriginalFilename();
                    String newName = filePrefix + "_" + oldName;
                    String newPath = path + sep + newName;
                    image.setSeriesId(series.getSeriesId());
                    image.setImageName(newName);
                    image.setImagePath(newPath);
                    image.setOperateId(series.getCreatorId());
                    image.setOperateName(series.getCreatorName());
                    image.setOperateTime(LocalDateTime.now());
                    String suffix = oldName.substring(oldName.lastIndexOf(".")+1);
                    image.setImageFormat(suffix);
                    image.setIsDeleted(0);
                    image.setImageStatus(0);
                    image.setPatientId(patient.getPatientId());
                    image.setPatientName(patient.getPatientName());
                    image.setCreatorId(userId);
                    image.setCreateTime(LocalDateTime.now());
                    list.add(image);
                }
                b = imageService.saveBatch(list);
            }
            // 更新关联的series信息
            series.setSeriesCount(files.length);
            series.setSeriesPath(path);
            series.setSeriesPreviewPath(path + sep + filePrefix + "_" + originalFilename);
            series.setSeriesFormat(ext);
            s = seriesService.updateById(series);

            // 关联科室信息
//            List<SysPermissionEntity> permissionList = getPermListByUserId(userId);
//            setPermissionListByFiles(permissionList, files);
//            setImagesRel(permissionList, patientId, studyId, seriesId);
        }catch (Exception ignored){}
        finally {
            for(InputStream in : inputStreams){
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("InputStream close failed!");
                }
            }
        }
        return b && s;
    }

    private List<StudyDepartmentRelationEntity> getSDRelListByGroupList(List<SysPermissionEntity> permissionList, Integer studyId){
        List<StudyDepartmentRelationEntity> relList = new ArrayList<>();
        for(SysPermissionEntity permission : permissionList){
            StudyDepartmentRelationEntity relation = new StudyDepartmentRelationEntity();
            relation.setStudyId(studyId);
            relation.setDepartmentId(permission.getPermissionId());
            relation.setDepartmentName(permission.getPermissionName());
            relation.setDepartmentDesc(permission.getDescription());
            relList.add(relation);
        }
        return relList;
    }

    @Override
    public boolean saveSeriesData(MultipartFile[] files, PatientEntity patient, JSONObject userJson, Integer studyId, List<SysPermissionEntity> permissionList) {
        boolean ret = false;
        DicomInputStream dis = null;
        InputStream[] inputStreams = new InputStream[files.length];
        String path = null;
        SeriesEntity series = new SeriesEntity();
        try {
            for (int i=0; i<files.length; i++) inputStreams[i] = files[i].getInputStream();
            InputStream inputStream = inputStreams[0];
            dis = new DicomInputStream(inputStream);
            Attributes attrs = dis.readDataset(-1, -1);

            series.setSeriesNumber(attrs.getString(Tag.SeriesNumber));
            String seriesInstanceUID = attrs.getString(Tag.SeriesInstanceUID);
            series.setSeriesInstanceUid(seriesInstanceUID);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 使用DateTimeFormatter解析日期字符串并创建LocalDate对象
            LocalDate seriesDate = LocalDate.parse(attrs.getString(Tag.SeriesDate), formatter);

            if(attrs.getString(Tag.SeriesTime) != null && !attrs.getString(Tag.SeriesTime).isEmpty()){
                // 尝试将字符串转换为double
                double timeInSecondsDouble = Double.parseDouble(attrs.getString(Tag.SeriesTime));
                // 因为LocalTime不支持秒的小数部分，所以我们只取整数秒
                long timeInSecondsLong = (long) Math.floor(timeInSecondsDouble);
                // 将秒转换为小时、分钟和秒
                int hours = (int) (timeInSecondsLong / 10000);
                int minutes = (int) ((timeInSecondsLong % 10000) / 100);
                int seconds = (int) (timeInSecondsLong %100);
                // 创建LocalTime对象
                LocalTime seriesTime = LocalTime.of(hours, minutes, seconds);
                series.setSeriesDate(seriesDate);
                series.setSeriesTime(seriesTime);
                }



            series.setSeriesModality(attrs.getString(Tag.Modality));
            series.setSeriesDesc(attrs.getString(Tag.SeriesDescription));
            series.setImagePosition(attrs.getString(Tag.ImagePosition));
            series.setSpacingBetweenSlices(attrs.getString(Tag.SpacingBetweenSlices));
            series.setMrAcquisitionType(attrs.getString(Tag.MRAcquisitionType));

            series.setStudyId(studyId);
            String originalFilename = files[0].getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            series.setSeriesFormat(suffix);
            series.setCreatorId(userJson.getInt("userId"));
            series.setCreatorName(userJson.getStr("userName"));
            series.setIsDeleted(0);
            series.setCreateTime(LocalDateTime.now());
            path = FileUtil.saveFiles(files, MyConstant.IMAGE_DIR, environment.getProperty("web.upload-path"));
            if(StringUtils.isBlank(path)) return false;
            series.setSeriesPath(path);
            // 设置预览图像
            String pre = path.substring(path.lastIndexOf(File.separator)+1);
            String fileName = pre + "_" + originalFilename;
            series.setSeriesPreviewPath(path + File.separator + fileName);

            series.setSeriesCount(files.length);
            series.setSeriesStatus(0);

            series.setPatientId(patient.getPatientId());
            series.setPatientName(patient.getPatientName());

            if(StringUtils.isNotBlank(seriesInstanceUID)){
                LambdaQueryWrapper<SeriesEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SeriesEntity::getStudyId, studyId);
                wrapper.eq(SeriesEntity::getSeriesInstanceUid, seriesInstanceUID);
                wrapper.orderByDesc(SeriesEntity::getSeriesId);
                List<SeriesEntity> list = seriesService.list(wrapper);
                if(list != null && !list.isEmpty()){
                    SeriesEntity one = list.get(0);
                    series.setSeriesId(one.getSeriesId());
                    ret = seriesService.updateById(series);
                }else{
                    ret = seriesService.save(series);
                }
            }else{
                ret = seriesService.save(series);
            }
        }catch (IOException exception){
            System.out.println("DicomInputStream open or read failed!");
        }finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    System.out.println("DicomInputStream close failed!");
                }
            }
            for(InputStream in : inputStreams){
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("InputStream close failed!");
                }
            }
        }

        boolean b = saveSingleImageData(files, patient, series, path, userJson.getInt("userId"));
        if(series.getSeriesId() != null){
            List<ImageDepartmentRelationEntity> relList = getIDRelListByGroupList(permissionList, series.getSeriesId());
            if(!relList.isEmpty()) {
                b = imageDepartmentRelationMapper.saveOrUpdateBatch(relList);
            }
        }
        return ret && b;
    }

    private List<ImageDepartmentRelationEntity> getIDRelListByGroupList(List<SysPermissionEntity> permissionList, Integer seriesId){
        List<ImageDepartmentRelationEntity> relList = new ArrayList<>();
        for(SysPermissionEntity permission : permissionList){
            ImageDepartmentRelationEntity relation = new ImageDepartmentRelationEntity();
            relation.setSeriesId(seriesId);
            relation.setDepartmentId(permission.getPermissionId());
            relation.setDepartmentName(permission.getPermissionName());
            relation.setDepartmentDesc(permission.getDescription());
            relList.add(relation);
        }
        return relList;
    }

    private boolean saveSingleImageData(MultipartFile[] files, PatientEntity patient, SeriesEntity series, String path, Integer userId) {
        final String sep = File.separator;
        int i = path.lastIndexOf(sep);
        String filePrefix = path.substring(i+1);
        List<ImageEntity> list = new ArrayList<>();
        for(MultipartFile file : files){
            ImageEntity imageEntity = getSingleImageData(file);
            String oldName = file.getOriginalFilename();
            String newName = filePrefix + "_" + oldName;
            String newPath = path + sep + newName;
            imageEntity.setSeriesId(series.getSeriesId());
            imageEntity.setImageName(newName);
            imageEntity.setImagePath(newPath);
            imageEntity.setOperateId(series.getCreatorId());
            imageEntity.setOperateName(series.getCreatorName());
            imageEntity.setOperateTime(LocalDateTime.now());
            String suffix = oldName.substring(oldName.lastIndexOf(".")+1);
            imageEntity.setImageFormat(suffix);
            imageEntity.setIsDeleted(0);
            imageEntity.setImageStatus(0);
            imageEntity.setPatientId(patient.getPatientId());
            imageEntity.setPatientName(patient.getPatientName());
            imageEntity.setCreatorId(userId);
            imageEntity.setCreateTime(LocalDateTime.now());
            list.add(imageEntity);
        }
        return imageService.saveBatch(list);
    }

    private ImageEntity getSingleImageData(MultipartFile file){
        InputStream inputStream = null;
        DicomInputStream dis = null;
        ImageEntity imageEntity = new ImageEntity();
        try{
            inputStream = file.getInputStream();
            dis = new DicomInputStream(inputStream);
            Attributes attrs = dis.readDataset(-1, -1);
            imageEntity.setImagePosition(attrs.getString(Tag.ImagePosition));
            imageEntity.setImageOrientation(attrs.getString(Tag.ImageOrientation));
            imageEntity.setSliceThickness(attrs.getString(Tag.SliceThickness));
            imageEntity.setSliceLocation(attrs.getString(Tag.SliceLocation));
            imageEntity.setImageRows(attrs.getString(Tag.Rows));
            imageEntity.setImageColumns(attrs.getString(Tag.Columns));
            imageEntity.setPixelSpacing(attrs.getString(Tag.PixelSpacing));
            imageEntity.setBitsAllocated(attrs.getString(Tag.BitsAllocated));
            imageEntity.setBitsStored(attrs.getString(Tag.BitsStored));
            imageEntity.setHighBit(attrs.getString(Tag.HighBit));
            imageEntity.setPixelRepresentation(attrs.getString(Tag.PixelRepresentation));
            imageEntity.setWindowCenter(attrs.getString(Tag.WindowCenter));
            imageEntity.setWindowWidth(attrs.getString(Tag.WindowWidth));
            imageEntity.setRescaleIntercept(attrs.getString(Tag.RescaleIntercept));
            imageEntity.setRescaleSlope(attrs.getString(Tag.RescaleSlope));
            imageEntity.setRescaleType(attrs.getString(Tag.RescaleType));
            imageEntity.setImageType(attrs.getString(Tag.ImageType));
            imageEntity.setSopInstanceUid(attrs.getString(Tag.SOPInstanceUID));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 使用DateTimeFormatter解析日期字符串并创建LocalDate对象
            LocalDate contentDate = LocalDate.parse(attrs.getString(Tag.ContentDate), formatter);

            // 尝试将字符串转换为double
            double timeInSecondsDouble = Double.parseDouble(attrs.getString(Tag.ContentTime));;
            // 因为LocalTime不支持秒的小数部分，所以我们只取整数秒
            long timeInSecondsLong = (long) Math.floor(timeInSecondsDouble);
            // 将秒转换为小时、分钟和秒
            int hours = (int) (timeInSecondsLong / 10000);
            int minutes = (int) ((timeInSecondsLong % 10000) / 100);
            int seconds = (int) (timeInSecondsLong %100);
            // 创建LocalTime对象
            LocalTime contentTime = LocalTime.of(hours, minutes, seconds);

            imageEntity.setContentDate(contentDate);
            imageEntity.setContentTime(contentTime);

            imageEntity.setImageNumber(attrs.getString(Tag.ImageID));
            imageEntity.setSamplesPerPixel(attrs.getString(Tag.SamplesPerPixel));
            imageEntity.setPhotometricInterpretation(attrs.getString(Tag.PhotometricInterpretation));
        }catch (IOException e){
            e.printStackTrace();
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
        return imageEntity;
    }

    @Override
    public CommonPage<StudyDTO> getPatientStudyPageByDepartmentId(PatientStudyQuery query) {
        Long total = studyMapper.countPatient(query);
        Long current = query.getCurrent();
        Long pageSize = query.getPageSize();
        Long totalPage = total % pageSize == 0 ?  total / pageSize : (total / pageSize) + 1;
        if(total == 0){
            return new CommonPage<>(null, total, pageSize, current, totalPage);
        }
        Long start = (current-1) * pageSize;
        query.setCurrent(start);
        List<StudyDTO> studyDTOList = studyMapper.getPatientStudyPageByDepartmentId(query);
        return new CommonPage<>(studyDTOList, total, pageSize, current, totalPage);
    }

    @Override
    public CommonPage<StudySeriesDTO> getStudySeriesPageByDepartmentId(StudySeriesQuery query) {
        Long total = studyMapper.countStudy(query);
        Long current = query.getCurrent();
        Long pageSize = query.getPageSize();
        Long totalPage = total % pageSize == 0 ?  total / pageSize : (total / pageSize) + 1;
        if(total == 0){
            return new CommonPage<>(null, total, pageSize, current, totalPage);
        }
        Long start = (current-1) * pageSize;
        query.setCurrent(start);
        List<StudySeriesDTO> studySeriesDTOList = studyMapper.getStudySeriesPageByDepartmentId(query);
        return new CommonPage<>(studySeriesDTOList, total, pageSize, current, totalPage);
    }

    @Override
    public CommonPage<SeriesImageDTO> getSeriesImagePageByDepartmentId(SeriesImageQuery query) {
        Long total = studyMapper.countSeries(query);
        Long current = query.getCurrent();
        Long pageSize = query.getPageSize();
        Long totalPage = total % pageSize == 0 ?  total / pageSize : (total / pageSize) + 1;
        if(total == 0){
            return new CommonPage<>(null, total, pageSize, current, totalPage);
        }
        Long start = (current-1) * pageSize;
        query.setCurrent(start);
        List<SeriesImageDTO> seriesImageDTOList = studyMapper.getSeriesImagePageByDepartmentId(query);
        return new CommonPage<>(seriesImageDTOList, total, pageSize, current, totalPage);
    }
}
