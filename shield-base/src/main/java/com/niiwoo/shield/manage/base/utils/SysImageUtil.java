package com.niiwoo.shield.manage.base.utils;

import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * 图片工具类
 *
 * @author zhangyuandi
 */
public class SysImageUtil {

    private static Logger log = LoggerFactory.getLogger(SysImageUtil.class);

    /**
     * 水印的边距
     */
    public static final int WATER_MARK_PADDING = 20;

    /**
     * 水印位置左上角
     */
    public static final int WATER_POSITION_LEFT_TOP = 1;

    /**
     * 水印位置右上角
     */
    public static final int WATER_POSITION_RIGHT_TOP = 2;

    /**
     * 水印位置右下角
     */
    public static final int WATER_POSITION_RIGHT_BOTTOM = 3;

    /**
     * 水印位置左下角
     */
    public static final int WATER_POSITION_LEFT_BOTTOM = 4;

    /**
     * 水印位置中间
     */
    public static final int WATER_POSITION_CENTER = 5;

    /**
     * 水印的透明度 取值区间为0到1，默认为0.6
     */
    public static final float WATER_OPACITY = 0.6f;

    /**
     * 支持的图片格式：JPG
     */
    public static final String IMAGE_TYPE_JPG = "jpg";

    /**
     * 支持的图片格式：GIF
     */
    public static final String IMAGE_TYPE_GIF = "gif";

    /**
     * 支持的图片格式：PNG
     */
    public static final String IMAGE_TYPE_PNG = "png";

    /**
     * 支持的图片格式：TIFF
     */
    public static final String IMAGE_TYPE_TIFF = "tif";

    /**
     * 支持的图片格式：BMP
     */
    public static final String IMAGE_TYPE_BMP = "bmp";

    /**
     * 压缩标识：成功
     */
    public static final int SCALE_STATUS_OK = 0;

    /**
     * 压缩标识：不支持的图片格式
     */
    public static final int SCALE_STATUS_NOT_SUPPORT = 1;

    /**
     * 压缩标识：原图不存在
     */
    public static final int SCALE_STATUS_NOT_FOUND = 2;

    /**
     * 压缩标识：原图比指定的压缩格式小
     */
    public static final int SCALE_STATUS_SMALLER_THAN_CANVAS = 3;

    /**
     * 压缩标识：压缩失败
     */
    public static final int SCALE_STATUS_ERROR = 4;

    /**
     * 压缩规格：小图
     */
    public static final String SCALE_FROMAT_SMALL = "s";

    /**
     * 压缩规格：中图
     */
    public static final String SCALE_FROMAT_MIDDLE = "m";

    /**
     * 压缩规格：大图
     */
    public static final String SCALE_FROMAT_BIG = "b";

    private SysImageUtil() {
    }

    public static void main(String[] args) {
        String src = "d:/pic/87.jpg";
        String des = "d:/pic/water1.jpg";
        String pri = "d:/pic/032.jpg";
        System.out.println(waterMark(src, des, pri, 5, 1.0f));
    }

    /**
     * <pre>
     * 水印图片和原图的边界关系如下
     *
     * ******************************************
     * *	星号矩形为图片的边界						*
     * *........................................*
     * *										*
     * *	小点矩形为水印的边界						*
     * *										*
     * *........................................*
     * *	星号矩形为图片的边界						*
     * *										*
     * ******************************************
     *
     *
     * ******************************************
     * *	星号矩形为图片的边界						*
     * *	.................................	*
     * *	.								.	*
     * *	.	小点矩形为水印的边界				.	*
     * *	.								.	*
     * *	.................................	*
     * *	星号矩形为图片的边界						*
     * *										*
     * ******************************************
     *
     *
     * *****.*******************************.****
     * * 星号 .								.	*
     * * 矩形	.								.	*
     * * 为	.								.	*
     * * 图片	.	小点矩形为水印的边界				.	*
     * * 边界	.								.	*
     * * 	.								.	*
     * * 	.								.	*
     * *	.								.	*
     * ******************************************
     * 添加图片水印
     *
     * @param srcImagePath    原图路径
     * @param destImagePath 目标路径
     * @param waterImgPath    水印图片路径
     * @param position        水印的位置 1左上角 2右上角 3右下角 4左下角 5中间 默认右下角
     * @param alpha        透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * </pre>
     */
    public final static String waterMark(String srcImagePath, String destImagePath, String waterImgPath, int position, float alpha) {
        try {
            File file = new File(srcImagePath);

            String type = getImageExtType(new FileInputStream(file));

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            Image waterImage = ImageIO.read(new File(waterImgPath)); // 水印文件
            int markWidth = waterImage.getWidth(null);
            int markHeight = waterImage.getHeight(null);

            position = validWaterMarkPosition(position);
            /** 长度为三的数组分别表示横坐标、纵坐标、越界标识位*/
            int[] xy = getWaterMarkPosition(position, width, height, markWidth, markHeight);

            double scaleRate = 1.0;
            /** 如果水印宽度或者高度超出图片的高度或者宽度则以图片的边框为界限重新计算水印等比压缩后的宽度和高度*/
            if (xy[2] < 0) {
                scaleRate = getScaleRate(width - 2 * WATER_MARK_PADDING, height - 2 * WATER_MARK_PADDING, markWidth, markHeight);
            }

            int newMarkWidth = (int) (markWidth * scaleRate);
            int newMarkHeight = (int) (markHeight * scaleRate);

            if (scaleRate != 1.0) {
                xy = getWaterMarkPosition(position, width, height, newMarkWidth, newMarkHeight);
            }

            int x = xy[0];
            int y = xy[1];

            BufferedImage bufferedImage = new BufferedImage(width, height,
                    type == IMAGE_TYPE_PNG ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            g.drawImage(waterImage, x, y, x + newMarkWidth, y + newMarkHeight, 0, 0, markWidth, markHeight, null); // 水印文件结束
            //g.drawImage(waterImage, x, y, markWidth, markHeight, null); // 水印文件结束
            g.dispose();
            ImageIO.write(bufferedImage, type, new File(destImagePath));
            return destImagePath;
        } catch (IOException e) {
            log.error("添加图片水印失败", e);
        }
        return null;
    }

    private static int[] getWaterMarkPosition(int position, int imageWidth, int imageHeight, int markWidth, int markHeight) {
        int x = 0, y = 0;
        int in = markWidth + 2 * WATER_MARK_PADDING < imageWidth && markHeight + 2 * WATER_MARK_PADDING < imageHeight ? 1 : -1;
        if (position == WATER_POSITION_LEFT_TOP) {
            x = y = WATER_MARK_PADDING;
        } else if (position == WATER_POSITION_RIGHT_TOP) {
            x = imageWidth - markWidth - WATER_MARK_PADDING;
            y = WATER_MARK_PADDING;
        } else if (position == WATER_POSITION_RIGHT_BOTTOM) {
            x = imageWidth - markWidth - WATER_MARK_PADDING;
            y = imageHeight - markHeight - WATER_MARK_PADDING;
        } else if (position == WATER_POSITION_LEFT_BOTTOM) {
            x = WATER_MARK_PADDING;
            y = imageHeight - markHeight - WATER_MARK_PADDING;
        } else if (position == WATER_POSITION_CENTER) {
            x = (imageWidth - markWidth) / 2;
            y = (imageHeight - markHeight) / 2;
        }
        return new int[]{x, y, in};
    }

    /**
     * 添加文字水印
     *
     * @param srcImagePath  目标图片路径，如：C://myPictrue//1.jpg
     * @param destImagePath 目标路径
     * @param pressText     水印文字， 如：中国证券网
     * @param fontName      字体名称， 如：宋体
     * @param fontStyle     字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
     * @param fontSize      字体大小，单位为像素
     * @param color         字体颜色
     * @param position      水印的位置 1左上角 2右上角 3右下角 4左下角 默认右下角
     * @param alpha         透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    public static String waterMark(String srcImagePath, String destImagePath, String pressText, String fontName, int fontStyle, int fontSize, Color color, int position, float alpha) {
        try {
            File file = new File(srcImagePath);

            String type = getImageExtType(new FileInputStream(file));

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            int markWidth = fontSize * getLength(pressText);
            int markHeight = fontSize;
            if (markWidth + 2 * WATER_MARK_PADDING < width && markHeight + 2 * WATER_MARK_PADDING < height) {
                position = validWaterMarkPosition(position);
                int x = 0, y = 0;
                if (position == WATER_POSITION_LEFT_TOP) {
                    x = y = WATER_MARK_PADDING;
                } else if (position == WATER_POSITION_RIGHT_TOP) {
                    x = width - markWidth - WATER_MARK_PADDING;
                    y = WATER_MARK_PADDING;
                } else if (position == WATER_POSITION_RIGHT_BOTTOM) {
                    x = width - markWidth - WATER_MARK_PADDING;
                    y = height - markHeight - WATER_MARK_PADDING;
                } else if (position == WATER_POSITION_LEFT_BOTTOM) {
                    x = WATER_MARK_PADDING;
                    y = height - markHeight - WATER_MARK_PADDING;
                } else if (position == WATER_POSITION_CENTER) {
                    x = (width - markWidth) / 2;
                    y = (height - markHeight) / 2;
                }
                BufferedImage bufferedImage = new BufferedImage(width, height, IMAGE_TYPE_PNG.equals(type) ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bufferedImage.createGraphics();
                g.drawImage(image, 0, 0, width, height, null);
                g.setFont(new Font(fontName, fontStyle, fontSize));
                g.setColor(color);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
                g.drawString(pressText, x, y + markHeight);
                g.dispose();
                ImageIO.write(bufferedImage, type, new File(destImagePath));
                return destImagePath;
            }
        } catch (Exception e) {
            log.error("添加文字水印失败", e);
        }
        return null;
    }

    public static int validWaterMarkPosition(int position) {
        return position <= 0 || position > 5 ? WATER_POSITION_CENTER : position;
    }

    public static float validWaterMarkOpacity(float opacity) {
        return opacity < 0 || opacity > 1 ? WATER_OPACITY : opacity;
    }

    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     *
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
     */
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    /**
     * 图片缩放
     *
     * @param srcPath        源文件路径
     * @param destPath       目标文件路径
     * @param canvasWidth    画布宽度
     * @param canvasHeight   画布高度
     * @param type           图片类型
     * @param scallIfSmaller 如果原图的宽度和高度均比画布的宽度和高度小时候要拉伸
     * @throws IOException
     */
    public static int scale(String srcPath, String destPath, int canvasWidth, int canvasHeight, String type, boolean scallIfSmaller) throws IOException {
        try {
            if (!checkType(type)) return SCALE_STATUS_NOT_SUPPORT;
            BufferedImage src = ImageIO.read(new File(srcPath));
            if (src == null) return SCALE_STATUS_NOT_FOUND;
            int imageWidth = src.getWidth();
            int imageHeight = src.getHeight();
            if (!scallIfSmaller && (canvasWidth == 0 || imageWidth <= canvasWidth) && (canvasHeight == 0 || imageHeight <= canvasHeight)) {
                return SCALE_STATUS_SMALLER_THAN_CANVAS;
            }
            double ratio = getScaleRate(canvasWidth, canvasHeight, imageWidth, imageWidth);
            if (canvasWidth <= 0) {
                canvasWidth = (int) (ratio * imageWidth);
            }
            if (canvasHeight <= 0) {
                canvasHeight = (int) (ratio * imageHeight);
            }
            if (ratio > 0) {
                BufferedImage bfImage = new BufferedImage(canvasWidth, canvasHeight,
                        IMAGE_TYPE_PNG.equals(type) ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_INT_RGB);
                bfImage.getGraphics().drawImage(src.getScaledInstance(canvasWidth, canvasHeight, Image.SCALE_SMOOTH), 0, 0, null);
                ImageIO.write(bfImage, type, new File(destPath));
                return SCALE_STATUS_OK;
            }
        } catch (IOException e) {
            log.error("图片 " + srcPath + " 压缩失败", e);
            throw e;
        }
        return SCALE_STATUS_ERROR;
    }

    /**
     * 缩放图像（按比例缩放）
     *
     * @param srcPath  源图像文件地址
     * @param destPath 缩放后的图像地址
     * @param ratio    缩放比例
     */
    public final static void scale(String srcPath, String destPath, double ratio, String type) {
        try {
            BufferedImage src = ImageIO.read(new File(srcPath));
            int imageWidth = src.getWidth();
            int imageHeight = src.getHeight();
            if (ratio > 0 && imageWidth > 0 && imageHeight > 0) {
                int canvasWidth = (int) (imageWidth * ratio);
                int canvasHeight = (int) (imageHeight * ratio);
                /*Image itemp = src.getScaledInstance(canvasWidth, canvasHeight,BufferedImage.SCALE_SMOOTH);
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(src, null);
				ImageIO.write((BufferedImage) itemp, type, new File(destPath));*/

                BufferedImage bfImage = new BufferedImage(canvasWidth, canvasHeight,
                        IMAGE_TYPE_PNG.equals(type) ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_INT_RGB);
                bfImage.getGraphics().drawImage(src.getScaledInstance(canvasWidth, canvasHeight, Image.SCALE_SMOOTH), 0, 0, null);
                ImageIO.write(bfImage, type, new File(destPath));
            }
        } catch (IOException e) {
            log.error("按比例缩放图片失败", e);
        }
    }

    private static double getScaleRate(int canvasWidth, int canvasHeight, int imageWidth, int imageHeight) {
        double ratio = 0;
        if (imageHeight <= 0 || imageWidth <= 0 || canvasWidth <= 0 && canvasHeight <= 0) return 0;
        if (canvasWidth <= 0) {
            ratio = canvasHeight * 1.0 / imageHeight;
        } else if (canvasHeight <= 0) {
            ratio = canvasWidth * 1.0 / imageWidth;
        } else {
            ratio = Math.min(canvasWidth * 1.0 / imageWidth, canvasHeight * 1.0 / imageHeight);
        }
        return ratio;
    }

    public static boolean checkType(String type) {
        return IMAGE_TYPE_JPG.equals(type) || IMAGE_TYPE_GIF.equals(type) || IMAGE_TYPE_PNG.equals(type)
                || IMAGE_TYPE_TIFF.equals(type) || IMAGE_TYPE_BMP.equals(type);
    }

    public static boolean checkImageSize(String size) {
        return SCALE_FROMAT_SMALL.equals(size) || SCALE_FROMAT_MIDDLE.equals(size) || SCALE_FROMAT_BIG.equals(size);
    }

    public static String getFormatSizeFileName(String fileName, String formatSize) {
        if (fileName == null || fileName.length() == 0) {
            return "";
        }
        int dot = fileName.lastIndexOf(".");
        String ft = fileName.substring(dot + 1);
        if (!checkType(ft)) {
            return "";
        }
        if (dot <= 2) {
            return "";
        }
        char sep = fileName.charAt(dot - 2);
        String sub = fileName.substring(dot - 2, dot);

        boolean thumb = sep == '_' && checkImageSize(sub.substring(1));
        formatSize = checkImageSize(formatSize) ? "_" + formatSize : null;
        if (thumb) {
            return fileName.replace(sub, formatSize == null ? "" : formatSize);
        } else {
            return fileName.substring(0, dot) + (formatSize != null ? formatSize : "") + "." + ft;
        }
    }

    /**
     * 旋转图片
     *
     * @param srcPath  原图路径
     * @param destPath 保存的图片路径
     * @param type     图片类型 IMAGE_TYPE_JPG IMAGE_TYPE_GIF IMAGE_TYPE_PNG
     * @param degree   旋转角度
     * @throws Exception
     */
    public static void rorate(String srcPath, String destPath, String type, int degree) throws Exception {
        //写到新的文件  
        FileOutputStream out = null;
        try {
            if (!checkType(type)) {
                throw new Exception("仅支持的图片格式(jpg,gif,png)");
            }
            if (StringUtils.isEmpty(srcPath)) {
                throw new Exception("图片路径不能为空");
            }
            if (StringUtils.isEmpty(destPath)) {
                throw new Exception("图片目标路径不能为空");
            }
            degree %= 360;
            if (degree <= 0) {
                throw new Exception("旋转角度非法");
            }
            BufferedImage srcImage = (BufferedImage) ImageIO.read(new File(srcPath));
            int iw = srcImage.getWidth();
            int ih = srcImage.getHeight();

            double theta = Math.toRadians(degree);
            double cw = iw * Math.abs(Math.cos(theta)) + ih * Math.abs(Math.sin(theta));
            double ch = iw * Math.abs(Math.sin(theta)) + ih * Math.abs(Math.cos(theta));
            //double dx = (ih - ch) * 0.5 * Math.sin(theta) + (iw - cw) * 0.5 * Math.cos(theta);
            //double dy = (ih - ch) * 0.5 * Math.cos(theta) - (iw - cw) * 0.5 * Math.sin(theta);

            BufferedImage destImage = new BufferedImage((int) cw, (int) ch, IMAGE_TYPE_PNG.equals(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = destImage.createGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, (int) cw, (int) ch);

            AffineTransform origXform = g2d.getTransform();
            origXform.translate(cw / 2, ch / 2);
            origXform.rotate(theta);

            g2d.setTransform(origXform);
            g2d.drawImage(srcImage, -iw / 2, -ih / 2, iw, ih, null);
            ImageIO.write(destImage, type, new File(destPath));
        } catch (IOException e) {
            log.error("图片旋转失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 根据字节流获取图片类型
     *
     * @param imageBytes
     * @return
     * @throws IOException
     */
    public static String getImageExtType(byte[] imageBytes) throws IOException {
        return getImageExtType(new ByteArrayInputStream(imageBytes));
    }

    /**
     * 根据字节流获取图片类型
     *
     * @param is
     * @return
     * @throws IOException
     */
    @SuppressWarnings("restriction")
    public static String getImageExtType(InputStream is) throws IOException {
        String type = "";
        MemoryCacheImageInputStream mcis = null;
        try {
            mcis = new MemoryCacheImageInputStream(is);
            Iterator<ImageReader> it = ImageIO.getImageReaders(mcis);
            while (it.hasNext()) {
                ImageReader reader = (ImageReader) it.next();
                if (reader instanceof GIFImageReader) {
                    return IMAGE_TYPE_GIF;
                } else if (reader instanceof JPEGImageReader) {
                    return IMAGE_TYPE_JPG;
                } else if (reader instanceof PNGImageReader) {
                    return IMAGE_TYPE_PNG;
                } else if (reader instanceof BMPImageReader) {
                    return IMAGE_TYPE_BMP;
                }
            }
        } finally {
            if (mcis != null) {
                try {
                    mcis.close();
                } catch (IOException ioe) {
                }
            }
        }
        return type;
    }

    /**
     * 根据文件名获取图片类型
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String getImageExtType(String fileName) {
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(new File(fileName));
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                return null;
            }
            ImageReader reader = iter.next();
            return reader.getFormatName();
        } catch (IOException e) {
        } finally {
            if (iis != null) {
                try {
                    iis.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

}
