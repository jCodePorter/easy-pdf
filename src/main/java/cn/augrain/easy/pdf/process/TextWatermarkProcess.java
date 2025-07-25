package cn.augrain.easy.pdf.process;

import cn.augrain.easy.pdf.enums.WaterMarkPadding;
import cn.augrain.easy.pdf.model.TextWaterMarkConfig;
import cn.augrain.easy.pdf.utils.Tuple2;
import cn.augrain.easy.tool.core.StringUtils;
import cn.augrain.easy.tool.util.AssertUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.blend.BlendMode;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文本水印处理
 *
 * @author biaoy
 * @since 2023/01/16
 */
@Getter
@Setter
public class TextWatermarkProcess implements PdfProcess {

    private String text;

    private TextWaterMarkConfig waterMarkConfig;

    public TextWatermarkProcess(String text, TextWaterMarkConfig waterMarkConfig) {
        this.text = text;
        this.waterMarkConfig = waterMarkConfig;
    }

    @Override
    public void process(PDDocument document) throws IOException {
        PDFont font = getPdFont(document);

        WaterMarkPadding paddingType = waterMarkConfig.getPadding();
        if (paddingType == WaterMarkPadding.TILE) {
            doTilePadding(document, font);
        } else if (paddingType == WaterMarkPadding.CENTER) {
            doCenterPadding(document, font);
        }
    }

    private PDFont getPdFont(PDDocument document) throws IOException {
        // PDFont font = PDType1Font.HELVETICA_BOLD;

        if (StringUtils.isNotBlank(waterMarkConfig.getFontPath())) {
            File file = new File(waterMarkConfig.getFontPath());
            FileInputStream fis = new FileInputStream(file);
            return PDType0Font.load(document, fis, true);
        } else {
            InputStream is = ClassLoader.getSystemResourceAsStream("fonts/simfang.ttf");
            return PDType0Font.load(document, is, true);
        }
    }

    private void doTilePadding(PDDocument document, PDFont font) throws IOException {
        float angle = waterMarkConfig.getAngle();
        float fontSize = waterMarkConfig.getFontSize();

        // 角度转弧度
        float radians = (float) (Math.PI / 180 * angle);
        float strWidth = font.getStringWidth(text) / 1000 * fontSize;
        float strHeight = fontSize;
        float textH = (float) (Math.abs((strWidth + strHeight) * Math.sin(angle)));
        float textW = (float) (Math.abs((strWidth + strHeight) * Math.cos(angle)));

        // 颜色解析
        Color color = parseColor(waterMarkConfig.getFontColor());

        for (PDPage page : document.getPages()) {
            try (PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                Tuple2<Float, Float> wh = getWidthHeight(page, cs);
                float pageWidth = wh.f0;
                float pageHeight = wh.f1;

                PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
                gs.setNonStrokingAlphaConstant(waterMarkConfig.getFontOpaque());
                gs.setStrokingAlphaConstant(waterMarkConfig.getFontOpaque());
                gs.getCOSObject().setItem(COSName.BM, COSName.MULTIPLY);

                cs.setGraphicsStateParameters(gs);
                cs.setNonStrokingColor(color);
                cs.setStrokingColor(color);
                cs.beginText();
                cs.setFont(font, fontSize);

                float interval = 50;
                for (float height = textH / 4; height < pageHeight; height = height + textH) {
                    for (float width = textH / 4; width < pageWidth; width = width + textW + interval) {
                        cs.setTextMatrix(Matrix.getRotateInstance(radians, width, height));
                        cs.showText(text);
                    }
                }
                cs.endText();
            }
        }
    }

    public Color parseColor(String color) {
        if (StringUtils.isNotBlank(color)) {
            AssertUtils.assertTrue(color.length() == 6, "颜色位数有误");
            String str1 = color.substring(0, 2);
            String str2 = color.substring(2, 4);
            String str3 = color.substring(4, 6);

            int red = Integer.parseInt(str1, 16);
            int green = Integer.parseInt(str2, 16);
            int blue = Integer.parseInt(str3, 16);
            return new Color(red, green, blue);
        }
        return Color.RED;
    }

    public Tuple2<Float, Float> getWidthHeight(PDPage page, PDPageContentStream cs) throws IOException {
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        int rotation = page.getRotation();

        switch (rotation) {
            case 90:
                pageWidth = page.getMediaBox().getHeight();
                pageHeight = page.getMediaBox().getWidth();
                cs.transform(Matrix.getRotateInstance(Math.toRadians(90), pageHeight, 0));
                break;
            case 180:
                cs.transform(Matrix.getRotateInstance(Math.toRadians(180), pageWidth, pageHeight));
                break;
            case 270:
                pageWidth = page.getMediaBox().getHeight();
                pageHeight = page.getMediaBox().getWidth();
                cs.transform(Matrix.getRotateInstance(Math.toRadians(270), 0, pageWidth));
                break;
            default:
                break;
        }
        return Tuple2.of(pageWidth, pageHeight);
    }

    private void doCenterPadding(PDDocument document, PDFont font) throws IOException {
        // 颜色解析
        Color color = parseColor(waterMarkConfig.getFontColor());
        // 字体大小
        float fontSize = waterMarkConfig.getFontSize();

        for (PDPage page : document.getPages()) {
            try (PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                float width = page.getMediaBox().getWidth();
                float height = page.getMediaBox().getHeight();
                float diagonalLength = (float) Math.sqrt(width * width + height * height);
                float angle = (float) Math.atan2(height, width);
                cs.transform(Matrix.getRotateInstance(angle, 0, 0));

                final PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
                gs.setNonStrokingAlphaConstant(waterMarkConfig.getFontOpaque());
                gs.setStrokingAlphaConstant(waterMarkConfig.getFontOpaque());
                gs.setBlendMode(BlendMode.MULTIPLY);
                gs.setLineWidth(3f);
                cs.setGraphicsStateParameters(gs);

                // 设置水印颜色
                cs.setNonStrokingColor(color);
                cs.setStrokingColor(color);

                // 水印偏移位置
                float stringWidth = font.getStringWidth(text) / 1000 * fontSize;
                float x = (diagonalLength - stringWidth) / 2;
                float y = -fontSize / 4;
                cs.setFont(font, fontSize);
                cs.beginText();
                cs.newLineAtOffset(x, y);
                cs.showText(text);
                cs.endText();
            }
        }
    }
}
