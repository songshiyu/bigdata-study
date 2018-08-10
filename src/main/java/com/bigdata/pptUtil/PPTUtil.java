package com.bigdata.pptUtil;

import com.bigdata.bean.PPTTestEntity;
import org.apache.poi.xslf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by songshiyu on 2018/8/10.
 */
public class PPTUtil {

    /**
     *  将ppt的每页生成一张图片,并返回新组装的PPTTestEntity
     * */
    public static java.util.List<PPTTestEntity> picFromPPTPerPage(String uploadFileRoot) throws FileNotFoundException {
        List<PPTTestEntity> thinkTankList = new LinkedList<PPTTestEntity>();
        List<String> imgList = null;
        PPTTestEntity thinkTank = null;
        StringBuffer stringBuffer = null;
        BufferedOutputStream ourbr = null;
        if (uploadFileRoot == null || "".equals(uploadFileRoot)){
            throw new FileNotFoundException("ppt路径不能为空");
        }
        File file = new File(String.valueOf(uploadFileRoot));
        //获取到单个的ppt对象
        FileInputStream in = new FileInputStream(file);
        FileOutputStream out = null;
        XMLSlideShow xmlSlideShow = null;
        try {
            xmlSlideShow = new XMLSlideShow(in);
            //获取到ppt的size
            Dimension pageSize = xmlSlideShow.getPageSize();
            //获取到每一个ppt的slide
            XSLFSlide[] slides = xmlSlideShow.getSlides();
            for (int i = 0;i < slides.length ;i++) {
                stringBuffer = new StringBuffer();
                thinkTank = new PPTTestEntity();
                XSLFSlide slide = slides[i];
                imgList = new LinkedList<String>();
                BufferedImage bufferedImage = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_BGR);
                Graphics2D graphics = bufferedImage.createGraphics();
                graphics.setPaint(Color.WHITE);
                graphics.fill(new Rectangle.Float(0, 0, pageSize.width, pageSize.height));
                slide.draw(graphics);
                String picName = uploadFileRoot + "_" + i + ".png";
                out = new FileOutputStream(picName);
                ImageIO.write(bufferedImage, "png", out);
                out.flush();
                //设置每一页生成的图片(此处应该为图片地址)
                thinkTank.setUploadImgsId(picName);
                //设置每一页ppt的title
                thinkTank.setTitleName(slide.getTitle());
                //设置每一个ppt的content
                XSLFCommonSlideData commonSlideData = slide.getCommonSlideData();

                List<DrawingParagraph> text = commonSlideData.getText();
                for (DrawingParagraph drawingParagraph:text){
                    stringBuffer.append(drawingParagraph.getText().toString().trim()).append("\n");
                }
                thinkTank.setContentName(stringBuffer.toString());
                //设置每一页的摘要?目前取每页ppt的第一段内容
                String summary = commonSlideData.getText().iterator().next().getText().toString();
                thinkTank.setSummaryName(summary);
                //设置每一页内的图片地址
                XSLFShape[] shapes = slide.getShapes();
                for (int j = 0; j < shapes.length; j++) {
                    XSLFShape shape = shapes[j];
                    if (shape instanceof XSLFPictureShape) {
                        XSLFPictureShape pictureShape = (XSLFPictureShape) shape;
                        XSLFPictureData pictureData = pictureShape.getPictureData();
                        byte[] data = pictureData.getData();
                        String fileName = pictureData.getFileName();
                        String outPath = uploadFileRoot.toString()  + "_" + i + "\\";
                        File outfile = new File(outPath);
                        if (!outfile.exists()){
                            outfile.mkdirs();
                        }
                        ourbr = new BufferedOutputStream(new FileOutputStream(outPath + fileName));
                        ourbr.write(data);
                        imgList.add(outPath + fileName);
                    }
                    if (shape instanceof XSLFTextShape){
                        XSLFTextShape shape1 = (XSLFTextShape) shape;
                        List<XSLFTextParagraph> textParagraphs = shape1.getTextParagraphs();
                        for (XSLFTextParagraph xslfTextRuns:textParagraphs){
                            List<XSLFTextRun> textRuns = xslfTextRuns.getTextRuns();
                            for (XSLFTextRun xslfTextRun:textRuns){
                                xslfTextRun.setFontFamily("宋体");
                            }
                        }
                    }
                }
                thinkTank.setUploadImgsRoot(imgList);  //本页内所有图片的地址
                thinkTankList.add(thinkTank);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
                ourbr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return thinkTankList;
    }
}
