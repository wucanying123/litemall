package org.linlinjava.litemall.db.util;

import gui.ava.html.image.generator.HtmlImageGenerator;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.util.Iterator;

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

    public static void cutImage(String path,int cubPx) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(path));
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        String targetPath = path;
        cut(path, targetPath, cubPx, cubPx, width-cubPx*2, height-cubPx*2);
    }


    /**
     *
     * @Description HTML转Image
     * @param htmText HTML文本字符串
     * @return 希望生成的Image Location
     */
    public static String html2Img(String htmText, String saveImageLocation){

        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        try {
            imageGenerator.loadHtml(htmText);
            Thread.sleep(100);
            imageGenerator.getBufferedImage();
            Thread.sleep(200);
            imageGenerator.saveAsImage(saveImageLocation);
            BufferedImage sourceImg = ImageIO.read(new File(saveImageLocation));

            sourceImg.getSubimage(10,10,sourceImg.getWidth()-20,sourceImg.getHeight()-20);
            //不需要转换位图的，下面三行可以不要
            BufferedImage sourceImg1 = ImageIO.read(new File(saveImageLocation));
            sourceImg = transform_Gray24BitMap(sourceImg1);
            ImageIO.write(sourceImg, "jpg", new File(saveImageLocation));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("将HTML文件转换成图片异常");
        }
        return saveImageLocation;
    }

    /**
     *
     * @Description 转换成24位图
     * @param image
     * @return
     */
    public static BufferedImage transform_Gray24BitMap(BufferedImage image){
        int h = image.getHeight();
        int w = image.getWidth();
        int[] pixels = new int[w * h]; // 定义数组，用来存储图片的像素
        PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels(); // 读取像素值
        } catch (InterruptedException e) {
            throw new RuntimeException("转换成24位图时，处理像素值异常");
        }
        MemoryImageSource s= new MemoryImageSource(w,h,pixels,0,w);
        Image img =Toolkit.getDefaultToolkit().createImage(s);
        BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);//如果要转换成别的位图，改这个常量即可
        buf.createGraphics().drawImage(img, 0, 0, null);
        return buf;
    }

    /**
     *
     * @Description 裁剪图片
     * @param srcPath 图片源地址
     * @param subPath 裁剪后图片地址
     * @param x 裁剪开始x坐标
     * @param y 裁剪开始y坐标
     * @param width 裁剪宽度
     * @param height 裁剪高度
     * @return
     */
    public static void cut(String srcPath,String subPath,int x,int y,int width,int height) throws IOException{
        FileInputStream is = null ;
        ImageInputStream iis =null ;
        try{
            //读取图片文件
            is = new FileInputStream(srcPath);

            /**//*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader
             * 声称能够解码指定格式。参数：formatName - 包含非正式格式名称 .
             *（例如 "jpeg" 或 "tiff"）等。
             */
            String suffix = srcPath.substring(srcPath.lastIndexOf(".")+1);


            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix);
            ImageReader reader = it.next();
            //获取图片流
            iis = ImageIO.createImageInputStream(is);

            /**//*
             * <p>iis:读取源.true:只向前搜索 </p>.将它标记为‘只向前搜索’。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader
             *  避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis,true) ;

            /**//*
             * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件
             * 将从其 ImageReader 实现的 getDefaultReadParam 方法中返回
             * ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

            /**//*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = new Rectangle(x, y, width, height);


            //提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);

            /**//*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将
             * 它作为一个完整的 BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0,param);

            //保存新图片
            ImageIO.write(bi, "jpg", new File(subPath));
        }

        finally{
            if(is!=null)
                is.close() ;
            if(iis!=null)
                iis.close();
        }
    }
}
