package org.linlinjava.litemall.db.util;

import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.File;

public class VideoUtil {

//    public static void main(String[] args) {
//        File mp4file = new File("C:/Users/Admin/Desktop/source/tool.mp4");
//        long mp4Time = getPlayTime(mp4file);
//        System.out.println(mp4Time);
//        File mp3file = new File("C:/Users/Admin/Desktop/source/song.mp3");
//        long mp3Time = getPlayTime(mp3file);
//        System.out.println(mp3Time);
//    }


    /**
     * 获取mp3、mp4文件播放时长：秒
     *
     * @param file
     * @return
     */
    public static Long getPlayTime(File file) {
        try {
            MultimediaObject instance = new MultimediaObject(file);
            MultimediaInfo result = instance.getInfo();
            long ls = result.getDuration() / 1000;
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static Long getPlayTime(MultipartFile multiFile) {
        try {
            File file = MultipartFileToFile(multiFile);
            MultimediaObject instance = new MultimediaObject(file);
            MultimediaInfo result = instance.getInfo();
            long ls = result.getDuration() / 1000;
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}