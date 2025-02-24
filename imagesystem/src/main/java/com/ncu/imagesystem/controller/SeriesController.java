package com.ncu.imagesystem.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ncu.imagesystem.dto.*;
import com.ncu.imagesystem.entity.*;
import com.ncu.imagesystem.enums.ResultCode;
import com.ncu.imagesystem.mapper.ImageDepartmentRelationMapper;
import com.ncu.imagesystem.service.*;
import com.ncu.imagesystem.tools.FileUtil;
import com.ncu.imagesystem.tools.MyConstant;
import com.ncu.imagesystem.tools.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import org.dcm4che3.data.Attributes;
//import org.dcm4che3.data.Tag;
//import org.dcm4che3.io.DicomInputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.io.InputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api("图像集合信息表controller")
@RestController
@RequestMapping("/series")
public class SeriesController {

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
    private Environment environment;
    @Autowired
    private StudyService studyService;
    @Autowired
    private ImageDepartmentRelationMapper seriesDepartmentRelationMapper;

    @ApiOperation("新增一条图像信息接口")
    @PostMapping("/uploadImageForm")
    public Result uploadImageForm(MultipartRequest files, @RequestPart("form") String form){
        boolean b = true;
        //获取文件
        for(List<MultipartFile> multipartFiles : files.getMultiFileMap().values()){
            MultipartFile uploadFile = multipartFiles.get(0);
            //对文件的具体操作
            b = FileUtil.saveFile(uploadFile, environment.getProperty("web.upload-path"));
            if(!b) break;
        }
        if(!b) return Result.failed("图像文件添加失败！");

        //表单数据序列化
        SeriesEntity seriesEntity = JSONUtil.toBean(form, SeriesEntity.class);
        seriesEntity.setIsDeleted(0);
        boolean save = seriesService.save(seriesEntity);
        if(save){
            return Result.success("图像信息添加成功！");
        }else {
            return Result.failed("图像信息添加失败！");
        }
    }


    @ApiOperation("上传多个图像文件接口")
    @PreAuthorize("@vps.hasAnyPermission('admin,doctor')")
    @PostMapping(value = "/uploadImages", consumes ="multipart/form-data")
    public Result uploadImages(@RequestPart("files") MultipartFile[] files, @RequestPart("imagesInfo")String info){
        SeriesEntity seriesEntity = new SeriesEntity();
        PatientEntity patientEntity = null;
        JSONObject jsonObject = JSONUtil.parseObj(info);
        if(files == null || files[0] == null){
            return Result.failed("图像文件为空！");
        }
        // 解析描述信息
        if(info != null){
            seriesEntity = JSONUtil.toBean(info, SeriesEntity.class);
        }
        String patientInfo = jsonObject.getStr("patientInfo");
        //病人信息
        if(patientInfo != null){
            patientEntity = JSONUtil.toBean(patientInfo, PatientEntity.class);
            if(patientEntity.getPatientIdCardNumber() != null){
                LambdaQueryWrapper<PatientEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(PatientEntity::getPatientIdCardNumber, patientEntity.getPatientIdCardNumber());
                PatientEntity one = patientService.getOne(wrapper);
                if(one == null){
                    patientService.save(patientEntity);
                }else{
                    patientEntity = one;
                }
            }
        }

        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        // 1、解析图像文件
//        MultipartFile file = files[0];
//        try {
//            InputStream inputStream = file.getInputStream();
//            DicomInputStream dicomInputStream = new DicomInputStream(inputStream);
//            Attributes attributes = dicomInputStream.readDataset(-1, -1);
//            byte[] bytes = attributes.getBytes(Tag.PatientName);
//            String name = new String(bytes);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        // 2、保存文件到指定目录
        String path = FileUtil.saveFiles(files, MyConstant.IMAGE_DIR, environment.getProperty("web.upload-path"));
        // 3、保存对应信息到数据库
        seriesEntity.setSeriesPath(path);
        seriesEntity.setSeriesCount(files.length);
        seriesEntity.setSeriesStatus(0);
        String originalFilename = files[0].getOriginalFilename();
        // 获取文件后缀不包括点
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        seriesEntity.setSeriesFormat(suffix);
        // 获取用户信息
        SysUserEntity doctor = getDoctorByUserId(request);
        if(doctor != null){
            seriesEntity.setCreatorId(doctor.getUserId());
            seriesEntity.setCreatorName(doctor.getName());
        }
        seriesEntity.setIsDeleted(0);
        seriesEntity.setCreateTime(LocalDateTime.now());
        if(patientEntity != null){
            seriesEntity.setPatientId(patientEntity.getPatientId());
            seriesEntity.setPatientName(patientEntity.getPatientName());
        }
        boolean save = seriesService.save(seriesEntity);

        // 增加关联
        if(doctor != null){
            ImageDoctorRelationEntity imageDoctorRelation = new ImageDoctorRelationEntity();
            imageDoctorRelation.setSeriesId(seriesEntity.getSeriesId());
            imageDoctorRelation.setUserId(doctor.getUserId());
            imageDoctorRelationService.save(imageDoctorRelation);
        }

        // 4、生成图像文件记录并保存
        List<ImageEntity> list = produceSingleImageList(files, path, seriesEntity);
        boolean b = imageService.saveBatch(list);

        if(save && b){
            return Result.success("图像文件上传成功！");
        }else {
            return Result.failed("图像文件上传失败！");
        }
    }

    private List<ImageEntity> produceSingleImageList(MultipartFile[] files, String path, SeriesEntity series){
        // 1、获取文件前缀
        final String sep = File.separator;
        int i = path.lastIndexOf(sep);
        String filePrefix = path.substring(i+1);
        ArrayList<ImageEntity> list = new ArrayList<>();
        for(MultipartFile file : files){
            // 2、生成文件路径
            String oldName = file.getOriginalFilename();
            String newName = filePrefix + "_" + oldName;
            String newPath = path + sep + newName;
            // 3、生成文件对象
            ImageEntity imageEntity = new ImageEntity(series);
            imageEntity.setImageName(newName);
            imageEntity.setImagePath(newPath);
            imageEntity.setOperateId(series.getCreatorId());
            imageEntity.setOperateName(series.getCreatorName());
            imageEntity.setOperateTime(LocalDateTime.now());
            String suffix = oldName.substring(oldName.lastIndexOf(".")+1);
            imageEntity.setImageFormat(suffix);
            imageEntity.setIsDeleted(0);
            list.add(imageEntity);
        }
        return list;
    }

    private SysUserEntity getDoctorByUserId(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        authorization=authorization.replace("Bearer ", "");
        JWT jwt = new JWT(authorization);
        final String content = (String) jwt.getPayload(MyConstant.TOKEN_PAYLOAD);
        JSONObject object = new JSONObject(content);
        Integer userId = object.get("userId", Integer.class);
        return sysUserService.getById(userId);
    }

    // 前端请求获取图像文件URL的接口
    @ApiOperation("根据seriesId获取图像文件接口")
    @GetMapping("/getImagesURLById")
    public Result getImagesURL(Integer seriesId, HttpServletRequest request){
        List<ImageEntity> list = imageService.getListBySeriesId(seriesId);
        String baseURL = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() + "/";
        List<String> urlList = new ArrayList<>();
        if(!list.isEmpty()){
            for(ImageEntity imageEntity : list){
                String singleImagePath = imageEntity.getImagePath();
                urlList.add(baseURL + singleImagePath.replace("\\", "/"));
            }
        }
        if(!urlList.isEmpty()){
            return Result.success("获取文件url成功！", urlList);
        }
        return Result.failed("获取文件url失败！");
    }

    @ApiOperation("根据seriesId查询序列信息接口")
    @GetMapping("/getSeriesById")
    @PreAuthorize("@vps.hasAnyPermission('admin,doctor')")
    public Result getSeriesById(Integer seriesId){
        SeriesEntity seriesEntity = seriesService.getById(seriesId);
        if(seriesEntity != null){
            return Result.success(ResultCode.SUCCESS.getMsg(), seriesEntity);
        }else {
            return Result.failed("医生信息获取失败！");
        }
    }

    @ApiOperation("根据seriesId查询序列信息接口")
    @GetMapping("/getSeriesImageById")
    public Result getSeriesImageById(Integer seriesId){
        SeriesEntity seriesEntity = seriesService.getById(seriesId);
        if(seriesEntity == null)return Result.failed("医生信息获取失败！");
        List<ImageEntity> imageEntityList = imageService.getListBySeriesId(seriesId);
        SeriesImageDTO seriesImageDTO = new SeriesImageDTO();
        BeanUtils.copyProperties(seriesEntity, seriesImageDTO);
        seriesImageDTO.setImageList(imageEntityList);
        return Result.success(ResultCode.SUCCESS.getMsg(), seriesImageDTO);
    }

    @ApiOperation("根据seriesId更新图像信息接口")
    @PostMapping("/updateImageById")
    public Result updateImageById(MultipartRequest files, @RequestPart("form") String form){
        SeriesEntity seriesEntity = JSONUtil.toBean(form, SeriesEntity.class);
        boolean b = seriesService.updateById(seriesEntity);
        if(b){
            return Result.success("图像信息更新成功！");
        }else {
            return Result.failed("图像信息更新失败！");
        }
    }

    @ApiOperation("获取图像分页信息接口")
    @PostMapping("/getImagePage")
    public Result getImagePage(@RequestBody ImagePageQuery imagePageQuery){
        CommonPage<SeriesEntity> imagePage = seriesService.getImagePageByQuery(imagePageQuery);
        if(imagePage == null) return Result.failed("图像分页信息查询失败！");
        return Result.success(ResultCode.SUCCESS.getMsg(), imagePage);
    }

    @ApiOperation("根据医生id获取图像集和图像文件分页信息接口")
    @PostMapping("/getImagePageByDoctorId")
    public Result getImagePageByDoctorId(@RequestBody ImagePageQuery imagePageQuery){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        SysUserEntity doctor = getDoctorByUserId(request);
        imagePageQuery.setUserId(doctor.getUserId());
        CommonPage<ImageSetDTO> imagePage = seriesService.getImageSetPageByQuery(imagePageQuery);
        if(imagePage == null) return Result.failed("图像分页信息查询失败！");
        return Result.success(ResultCode.SUCCESS.getMsg(), imagePage);
    }

    @ApiOperation("根据seriesId删除图像接口")
    @GetMapping("/deleteImageById")
    public Result deleteImageById(Integer seriesId){
        boolean b = seriesService.deleteById(seriesId);
        if(b){
            return Result.success("图像信息删除成功！");
        }else {
            return Result.failed("图像信息删除失败！");
        }
    }

    @ApiOperation("新增一个序列信息接口")
    @PostMapping("/addOneSeries")
    public Result addOneSeries(@RequestBody OneSeriesReq oneSeriesReq){
        if(oneSeriesReq.getSeries() == null || oneSeriesReq.getSeries().getSeriesModality() == null){
            return Result.failed("没有序列modality信息！");
        }
        boolean b = studyService.saveOneSeries(oneSeriesReq.getSeries(), oneSeriesReq.getDepartmentIdList());
        if(b){
            return Result.success("序列新增成功！");
        }else {
            return Result.failed("序列新增失败！");
        }
    }

    @ApiOperation("删除单个序列接口")
    @GetMapping("/deleteOneSeries")
    public Result deleteOneSeries(Integer seriesId){
        LambdaQueryWrapper<ImageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImageEntity::getSeriesId, seriesId);
        imageService.removeList(wrapper);
        LambdaQueryWrapper<ImageDepartmentRelationEntity> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(ImageDepartmentRelationEntity::getSeriesId, seriesId);
        seriesDepartmentRelationMapper.delete(wrapper1);
        boolean b = seriesService.removeOneById(seriesId);
        if(b){
            return Result.success("序列删除成功！");
        }else {
            return Result.failed("序列删除失败！");
        }
    }

    @ApiOperation("删除多个序列接口")
    @PostMapping("/deleteSeries")
    public Result deleteSeries(@RequestBody List<Integer> ids){
        LambdaQueryWrapper<ImageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ImageEntity::getSeriesId, ids);
        imageService.removeList(wrapper);
        LambdaQueryWrapper<ImageDepartmentRelationEntity> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(ImageDepartmentRelationEntity::getSeriesId, ids);
        seriesDepartmentRelationMapper.delete(wrapper1);
        boolean b = seriesService.removeListByIds(ids);
        if(b){
            return Result.success("序列删除成功！");
        }else {
            return Result.failed("序列删除失败！");
        }
    }

    @ApiOperation("更新序列接口")
    @PostMapping("/updateOneSeries")
    public Result updateOneSeries(@RequestBody SeriesEntity series){
        boolean b = seriesService.updateById(series);
        if(b){
            return Result.success("序列更新成功！");
        }else {
            return Result.failed("序列更新失败！");
        }
    }
}
