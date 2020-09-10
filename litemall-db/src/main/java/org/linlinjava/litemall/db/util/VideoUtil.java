package org.linlinjava.litemall.db.util;

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
         * @param file
         * @return
         */
        public static Long getPlayTime(File file){
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
}