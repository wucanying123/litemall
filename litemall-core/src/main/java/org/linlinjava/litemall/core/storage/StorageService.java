package org.linlinjava.litemall.core.storage;

import javafx.application.Application;
import org.linlinjava.litemall.core.util.CharUtil;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.service.LitemallStorageService;
import org.linlinjava.litemall.db.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * 提供存储服务类，所有存储服务均由该类对外提供
 */
public class StorageService {
    private String active;
    private Storage storage;
    @Autowired
    private LitemallStorageService litemallStorageService;
    @Autowired
    private SourceService sourceService;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param contentLength 文件长度
     * @param contentType   文件类型
     * @param fileName      文件索引名
     */
    public LitemallStorage store(InputStream inputStream, long contentLength, String contentType, String fileName) {
        String key = generateKey(fileName);
        storage.store(inputStream, contentLength, contentType, key);

        String url = generateUrl(key);
        LitemallStorage storageInfo = new LitemallStorage();
        storageInfo.setName(fileName);
        storageInfo.setSize((int) contentLength);
        storageInfo.setType(contentType);
        storageInfo.setKey(key);
        storageInfo.setUrl(url);
        litemallStorageService.add(storageInfo);

        return storageInfo;
    }

    /**
     * 存储一个文件对象
     *
     * @param contentLength 文件长度
     * @param contentType   文件类型
     * @param fileName      文件索引名
     */
    public LitemallStorage store1(MultipartFile file, long contentLength, String contentType, String fileName) throws IOException {
        InputStream inputStream = file.getInputStream();
        String key = generateKey(fileName);
        storage.store(inputStream, contentLength, contentType, key);

        String url = generateUrl(key);
        LitemallStorage storageInfo = new LitemallStorage();
        storageInfo.setName(fileName);
        storageInfo.setSize((int) contentLength);
        storageInfo.setType(contentType);
        storageInfo.setKey(key);
        storageInfo.setUrl(url);
        litemallStorageService.add(storageInfo);

        String projectPath = this.getClass().getResource("/").getPath();
        //根据自己系统的resource 目录所在位置进行自行配置
        String destDir = projectPath + "upload" + File.separator;
        File dir = new File(destDir);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        // 文件保存路径
        String filePath = destDir + file.getOriginalFilename();
        // 转存文件
//        try {
//            file.transferTo(new File(filePath));
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(filePath);
//            fos.write(file.getBytes()); // 写入文件
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
//        Source source = addSource( fileName,  fileExt);
//        Source ssourceService.insertSource();
        return storageInfo;
    }

//    public Source addSource(String path, String sourceName, String fileExt,long contentLength) {
//        List<Source> sources = new ArrayList<>();
//        Source source = new Source();
//        String fileUrl = path + "/file/tool.mp4";
//        vedioSource.setUrl(fileUrl);
//        vedioSource.setMaxPlayTime(7);
//        vedioSource.set_type("Video");
//
////        vedioSource.setName(sourceName+"."+fileExt);
//        vedioSource.setName("tool.mp4");
//        vedioSource.setMime("video/mp4");
////        path = "C:/Users/Admin/Desktop/source/"+sourceName+fileExt;
//        path = "C:/Users/Admin/Desktop/source/tool.mp4";
//        File file = new File(path);
//        Long fileSize = 0L;
//        if (file.exists() && file.isFile()) {
//            String fileName = file.getName();
//            System.out.println("文件" + fileName + "的大小是：" + file.length());
//            fileSize = file.length();
//        }
//        String md5 = StringUtilsXD.getFileMD5(file);
//        vedioSource.setMd5(md5);
//        vedioSource.setSize(fileSize);
//        vedioSource.setEnabled(true);
//        vedioSource.setEnabledBy("check");
//        vedioSource.setMediaGroup(null);
//        vedioSource.setDeletedBy(null);
//        vedioSource.setDeleted(null);
//        vedioSource.setNewName(null);
//        vedioSource.setOldFilePath(null);
//        vedioSource.setFileExt(".mp4");
//        vedioSource.setId(UUID.randomUUID().toString().replace("-", ""));
//        vedioSource.setPlayTime(0);
//        vedioSource.setTimeSpan(VideoUtil.getPlayTime(file).intValue());
//        vedioSource.setMaxPlayTime(VideoUtil.getPlayTime(file).intValue());
//        vedioSource.setTheLeft(0);
//        vedioSource.setTop(0);
//        vedioSource.setWidth(180);
//        vedioSource.setHeight(320);
//        vedioSource.setEntryEffect("None");
//        vedioSource.setExitEffect("None");
//        vedioSource.setEntryEffectTimeSpan(0);
//        vedioSource.setExitEffectTimeSpan(0);
//        vedioSource.setLanguage("en");
//    }

    private String generateKey(String originalFilename) {
        int index = originalFilename.lastIndexOf('.');
        String suffix = originalFilename.substring(index);

        String key = null;
        LitemallStorage storageInfo = null;

        do {
            key = CharUtil.getRandomString(20) + suffix;
            storageInfo = litemallStorageService.findByKey(key);
        }
        while (storageInfo != null);

        return key;
    }

    public Stream<Path> loadAll() {
        return storage.loadAll();
    }

    public Path load(String keyName) {
        return storage.load(keyName);
    }

    public Resource loadAsResource(String keyName) {
        return storage.loadAsResource(keyName);
    }

    public void delete(String keyName) {
        storage.delete(keyName);
    }

    private String generateUrl(String keyName) {
        return storage.generateUrl(keyName);
    }
}
