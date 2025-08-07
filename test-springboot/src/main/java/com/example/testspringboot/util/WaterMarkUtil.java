package com.example.testspringboot.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StopWatch;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/14 14:29
 */
@Slf4j
public class WaterMarkUtil {
    public static void main(String[] args) throws Exception {
        pdfWaterMark("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\waterMark\\附件简历.pdf",
                Files.newOutputStream(Paths.get("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\waterMark\\附件简历(水印).pdf")),
                "谭健新");

        imgWaterMark("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\waterMark\\图片简历.png",
                Files.newOutputStream(Paths.get("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\waterMark\\图片简历(水印).png")),
                "谭健新");
    }

    /**
     * pdf添加平铺水印
     */
    public static void pdfWaterMark(String filePath, OutputStream outputStream, String waterMarkText) throws Exception {
        StopWatch stopWatch = new StopWatch("pdfWaterMark");
        stopWatch.start();
        try {
            // 参数默认赋值
            int textH = 50;
            int textW = 80;
            int fontSize = 16;
            // 间隔距离
            int interval = -100;
            PdfReader reader = new PdfReader(filePath);
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            // 使用itext-asian依赖字体
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            PdfGState gs = new PdfGState();
            //改透明度
            gs.setFillOpacity(0.5f);
            gs.setStrokeOpacity(0.4f);

            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            label.setText(waterMarkText);
            // FontMetrics metrics = label.getFontMetrics(label.getFont());
            // int textH = metrics.getHeight();
            // int textW = metrics.stringWidth(label.getText());
            // log.info("文本水印: height={},width={}", textH, textW);

            PdfContentByte under;
            com.itextpdf.text.Rectangle pageRect;

            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, fontSize);
                under.setColorFill(BaseColor.LIGHT_GRAY);

                // 调整数值，影响水印位置
                for (int height = interval + textH; height < pageRect.getHeight(); height = height + textH * 3) {
                    for (int width = interval + textW; width < pageRect.getWidth() + textW; width = width + textW * 2) {
                        under.showTextAligned(Element.ALIGN_LEFT, waterMarkText, width, height, 30);
                        under.setGState(gs);
                    }
                }
                under.endText();
            }
            stamper.close();
            reader.close();
        } catch (Exception e) {
            log.error("WaterMarkUtil#pdf添加平铺水印失败!");
            e.printStackTrace();
            throw e;
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

    /**
     * 图片添加水印
     */
    public static void imgWaterMark(String filePath, OutputStream outputStream, String waterMarkText) throws Exception {
        StopWatch stopWatch = new StopWatch("imgWaterMark");
        stopWatch.start();
        log.info("maxMemory---->" + (Runtime.getRuntime().maxMemory()>>20));
        try {
            //获取到文件的后缀名
            File srcFile = new File(filePath);
            String fileName = srcFile.getName();
            String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
            Image image = ImageIO.read(srcFile);
            // 获取图片的宽
            int imgWidth = image.getWidth(null);
            // 获取图片的高
            int imgHeight = image.getHeight(null);
            log.info("原始图片, width: {}, height: {} ", imgWidth, imgHeight);

            //旋转角度
            int angel = 315;
            //每个水印垂直间隔
            int ypadding = 120;
            int fontSize = 25;

            BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = bi.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //绘制原图片
            float alpha = 1F;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha);
            g.setComposite(ac);
            g.drawImage(image, 0, 0, imgWidth, imgHeight, null);
            g.setBackground(Color.BLACK);

            //开始绘制水印
            //水印字体
            Font font = loadStyleFont(Font.BOLD, fontSize);
            g.setFont(font);

            //水印串宽度
            int stringWidth = g.getFontMetrics(g.getFont()).charsWidth(waterMarkText.toCharArray(), 0, waterMarkText.length());
            //每个水印水平间隔
            log.info("水印宽度: {}", stringWidth);

            FontRenderContext frc = g.getFontRenderContext();
            TextLayout tl = new TextLayout(waterMarkText, font, frc);

            //旋转水印
            g.rotate(Math.toRadians(angel), (double) imgWidth / 2, (double) imgHeight / 2);
            //水印透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5F));
            // 字体色
            g.setColor(Color.LIGHT_GRAY);

            int x = -imgHeight / 2;
            int y;

            //循环绘制
            while (x < imgWidth + imgHeight / 2) {
                y = -imgWidth / 2;
                while (y < imgHeight + imgWidth / 2) {
                    Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(x, y));
                    g.fill(sha);
                    y += ypadding;
                }
                x += stringWidth + 100;
            }

            //释放资源
            g.dispose();
            ImageIO.write(bi, formatName, outputStream);
        } catch (Exception e) {
            log.error("WaterMarkUtil#图片添加平铺水印失败!");
            e.printStackTrace();
            throw e;
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
    }

    /**
     * @param style    字体样式
     * @param fontSize 字体大小
     * @return Font
     */
    public static Font loadStyleFont(int style, float fontSize) {
        try {
            // 读取项目字体路径
            InputStream in = new ClassPathResource("font/华文新宋.ttf").getInputStream();
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, in);
            Font dynamicFontPt = dynamicFont.deriveFont(style, fontSize);
            in.close();
            return dynamicFontPt;
        } catch (Exception e) {
            log.error("加载font/华文新宋.ttf字体失败!");
            e.printStackTrace();
            return new Font("宋体", Font.PLAIN, 20);
        }
    }
}