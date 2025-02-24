package com.ncu.imagesystem.tools;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.dcm4che3.tool.dcm2jpg.Dcm2Jpg;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FileUtil {

    static String sep = File.separator;
    final static String PIC = "pic";

    // 根据当前时间返回一个随机名称
    public static String randomName() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0
        return millis + String.format("%03d", end3);
    }

    public static boolean saveFile(MultipartFile uploadFile, String uploadPath){
        String oldName = uploadFile.getOriginalFilename();
        String newName = randomName() + oldName.substring(oldName.lastIndexOf("."));
        //1.3生成文件在服务器端存储的子目录
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String filePath = dateFormat.format(new Date());
        File dir = new File( uploadPath + File.separator + filePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String absolutePath = dir.getAbsolutePath();
        File file = new File(absolutePath + File.separator + newName);

        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 保存单个文件到指定的目录下
     * @param uploadFile 文件
     * @param path       指定的绝对路径目录
     * @return 生成的文件名
     */
    public static String saveFile2Path(MultipartFile uploadFile, String path){
        String oldName = uploadFile.getOriginalFilename();
        if(StringUtils.isBlank(oldName)) return null;
        String newName = randomName() + "_" + oldName;
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String absolutePath = dir.getAbsolutePath();
        File file = new File(absolutePath + File.separator + newName);
        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            return null;
        }
        return newName;
    }


    /**
     * 保存单个文件到指定的目录下
     * @param uploadFile 文件
     * @param path       指定的绝对路径目录
     * @return 生成的文件名
     */
    public static String saveFile2PathAddXlsx(MultipartFile uploadFile, String path){
        String oldName = uploadFile.getOriginalFilename();
        if(StringUtils.isBlank(oldName)) return null;
        String newName = randomName() + "_" + oldName +".xlsx";
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String absolutePath = dir.getAbsolutePath();
        File file = new File(absolutePath + File.separator + newName);
        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            return null;
        }
        return newName;
    }

    /**
     * 保存多个文件
     * @param uploadFiles 文件列表
     * @param dirName    多个文件保存的路径，uploadPath+dirName是完整目录路径
     * @param uploadPath 保存文件的根目录
     * @return 不包含uploadPath的路径
     */
    public static String saveFiles(MultipartFile[] uploadFiles, String dirName, String uploadPath){
        String ret = ""; //保存失败返回空字符串
        // 1、生成文件保存完整路径
        String randomName = randomName(); //本次文件上传的最终目录名
        File dir = new File(uploadPath + sep + dirName + sep + randomName);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String absolutePath = dir.getAbsolutePath();
        for(MultipartFile file : uploadFiles){
            // 2、生成新的文件名称包含后缀
            String oldName = file.getOriginalFilename();
            String newName = randomName + "_" + oldName;
            // 3、保存文件
            File f = new File(absolutePath + sep + newName);
            try {
                file.transferTo(f);
            } catch (IOException e) {
                deleteFileOrDir(dir);
                return ret;
            }
        }
        return dirName + sep + randomName;
    }

    // 删除文件夹以及子文件
    public static void deleteFileOrDir(File file){
        // 1、判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            return;
        }
        if(file.isFile()){
            file.delete();
        }
        if(file.isDirectory()){
            // 2、获取目录下子文件
            File[] files = file.listFiles();
            // 3、遍历该目录下的文件对象
            if(files != null) {
                for (File f : files) {
                    //判断子目录是否存在子目录,如果是文件则删除
                    if (f.isDirectory()) {
                        //递归删除目录下的文件
                        deleteFileOrDir(f);
                    } else {
                        //文件删除
                        f.delete();
                    }
                }
            }
            // 4、文件夹删除
            file.delete();
        }
    }

    // 获取传入目录下的所有文件的文件名并排序
    public static List<String> getFileNameFromDir(String dir, String uploadPath){
        File dirFile = new File(uploadPath + sep + dir);
        List<String> pathList = null;
        // 1、判断文件不为null或文件目录存在
        if (dirFile == null || !dirFile.exists()) {
            return null;
        }
        // 2、获取目录下子文件
        File[] files = dirFile.listFiles();
        // 3、遍历该目录下的文件对象
        if(files != null) {
            int len = files.length;
            String[] pathArr = new String[len];
            int arrIndex = 0;
            for (File f : files) {
                //判断是文件
                if (f.isFile()) {
                    String fileName = f.getName();
                    int i = fileName.lastIndexOf("_");
                    // 文件名称符合要求，否则跳过
                    if(i != -1){
                        String oldName = fileName.substring(i+1); // 原始的文件名，含后缀
                        String prefix = oldName.substring(0, oldName.lastIndexOf(".")); // 原始的文件名，不含后缀
                        if(arrIndex == 0){
                            pathArr[0] = fileName;
                            arrIndex++;
                        }else{
                            int preIndex = arrIndex-1;
                            while(preIndex >= 0){
                                String _fileName = pathArr[preIndex];
                                String _oldName = _fileName.substring(_fileName.lastIndexOf("_") + 1);
                                String _prefix = _oldName.substring(0, _oldName.lastIndexOf("."));
                                if(prefix.compareToIgnoreCase(_prefix) < 0){  //prefix < _prefix
                                    pathArr[preIndex+1] = _fileName;
                                    preIndex--;
                                }else{
                                    break;
                                }
                            }
                            pathArr[preIndex+1] = fileName;
                            arrIndex++;
                        }
                    }
                }
            }
            pathList = Arrays.asList(pathArr);
        }
        return pathList;
    }

    public static List<String> getPathByDir(String basePath, String dir, List<String> fileNameList){
        File dirFile = new File(basePath + sep + dir);
        List<String> pathList = new ArrayList<>();
        // 1、判断文件不为null或文件目录存在
        if (!dirFile.exists()) {
            return null;
        }
        // 2、获取目录下子文件
        File[] files = dirFile.listFiles();
        for(File f : files){
            //判断是文件
            if (f.isFile()) {
                String fileName = f.getName();
                fileNameList.add(fileName);
                pathList.add(dir + sep + fileName);
            }
        }
        return pathList;
    }


    public static String saveFile(File file, String dirName, String uploadPath){
        String name = file.getName();
        // 1、生成文件保存完整路径
        String randomName = randomName(); //本次文件上传的最终目录名
        File dir = new File(uploadPath + sep + dirName + sep + randomName);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String absolutePath = dir.getAbsolutePath();
        boolean ret = true;
        if("zip".equals(name.split("\\.")[1])){
            ret = unzip(file, absolutePath);
        }else{
            ret = storeFile(file, absolutePath);
        }
        if(!ret) return "";
        return dirName + sep + randomName;
    }

    public static boolean unzip(File file, String dir){
        try (ZipFile zipFile = new ZipFile(file)){
            Enumeration<ZipArchiveEntry> enumeration = zipFile.getEntries();
            while (enumeration.hasMoreElements()) {
                // 依次获取压缩包内的文件实体对象
                ZipArchiveEntry entry = enumeration.nextElement();
//                System.out.println("this file size:" + entry.getSize());
                String name = entry.getName();
                if (entry.isDirectory()) {
                    String outDir = dir + sep + name;
                    File outDirFile = new File(outDir);
                    outDirFile.mkdir();
                }else{
                    try (BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(entry))) {
                        String outName = dir + sep + name;
                        File outFile = new File(outName);
                        outFile.getParentFile().mkdir();
                        try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(outFile.toPath()))) {
                            int len;
                            byte[] buffer = new byte[2048];
                            while ((len = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, len);
                            }
                        }
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean storeFile(File file, String dir){
        try(FileInputStream in = new FileInputStream(file)){
            try (BufferedInputStream inputStream = new BufferedInputStream(in)) {
                String name = file.getName();
                String outName = dir + sep + name;
                File outFile = new File(outName);
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outFile))) {
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void is2File(InputStream is, File file) throws IOException{
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[8192];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } finally {
            os.close();
            is.close();
        }
    }

    public static String dcm2pic(String path){
        File file = new File(path);
        String outDir;
        File[] files = new File[1];
        if(!file.isDirectory()){
            String parent = file.getParent();
            files[0] = file;
            outDir = parent + sep + PIC;
        }else{
            String absolutePath = file.getAbsolutePath();
            files = file.listFiles();
            outDir = absolutePath + sep + PIC;
        }
        File out = new File(outDir);
        if(!out.exists()){
            out.mkdirs();
        }
        if(files == null || files.length == 0) return null;
        String prefix = null;
        for(File f : files){
            // 获取目录下的文件
            String name = f.getName();
            if(f.isFile() && "dcm".equals(name.substring(name.lastIndexOf(".")+1))){
                // 把dcm文件转化为jpg
                // 获取文件名
                prefix = name.substring(0, name.lastIndexOf("."));
                try{
                    File outFile = new File(outDir + sep + prefix + ".jpg");
                    Dcm2Jpg dcm2Jpg = new Dcm2Jpg();
                    dcm2Jpg.initImageWriter("JPEG", null, null, null, 1);
                    dcm2Jpg.convert(f, outFile);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        }
        if(file.isFile()) return outDir + sep + prefix + ".jpg";
        return outDir;
    }
}
