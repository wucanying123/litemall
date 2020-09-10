package org.linlinjava.litemall.db.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class ImageUtil {
    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null) return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     * @return
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
//
//    /**
//     * 示例
//     */
//    public static void main(String[] args) {
//        RequestData2 requestData2 = new RequestData2();
//        requestData2.setType("callCardService");
//        requestData2.setFn("screenshot");
//        requestData2.setArg1(100);
//        requestData2.setArg2(100);
//        String result = HttpUtil.postJsonObject(URL, requestData2);
//        JSONObject jsonObject = JSONObject.fromObject(result);
//        String imgString= "";
//        if (null != jsonObject.get("result")) {
//            imgString = jsonObject.get("result").toString();
//            imgString = StringUtilsXD.replaceBlank(imgString);
//        }
////        String strImg = getImageStr("C:/Users/Admin/Desktop/source/wu.jpg");
//        String strImg = imgString;
//        System.out.println(strImg);
//        generateImage(strImg, "C:/Users/Admin/Desktop/source/wu2.jpg");
//    }

//    public static void main(String[] args) {
//       String str = UUID.randomUUID().toString().replace("-", "");
//       String sub = str.substring(0,2);
//       System.out.println(str);
//       System.out.println(sub);
//    }
}
