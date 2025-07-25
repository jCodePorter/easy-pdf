package cn.augrain.easy.pdf.process;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.blend.BlendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

/**
 * 图片水印处理
 */
public class ImageWatermarkProcess implements PdfProcess {

    private String imgPath;

    public ImageWatermarkProcess(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public void process(PDDocument document) throws IOException {
        PDImageXObject pdImage = PDImageXObject.createFromFile(imgPath, document);

        WatermarkOptions options = new WatermarkOptions()
                .size(120, 60)
                .padding(20)
                .layout(4, 3)
                .rotate(30);

        for (PDPage page : document.getPages()) {
            addImgWatermark(document, page, pdImage, options);
        }
    }


    private static void addImgWatermark(PDDocument doc, PDPage page, PDImageXObject pdImage, WatermarkOptions options) throws IOException {
        try (PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            float width = page.getMediaBox().getWidth();
            float height = page.getMediaBox().getHeight();

            //有一些pdf页面是有角度翻转的，修正一下
            int rotation = page.getRotation();
            switch (rotation) {
                case 90:
                    width = page.getMediaBox().getHeight();
                    height = page.getMediaBox().getWidth();
                    cs.transform(Matrix.getRotateInstance(Math.toRadians(90), height, 0));
                    break;
                case 180:
                    cs.transform(Matrix.getRotateInstance(Math.toRadians(180), width, height));
                    break;
                case 270:
                    width = page.getMediaBox().getHeight();
                    height = page.getMediaBox().getWidth();
                    cs.transform(Matrix.getRotateInstance(Math.toRadians(270), 0, width));
                    break;
                default:
                    break;
            }

            PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
            gs.setNonStrokingAlphaConstant(0.2f);// 设置透明度
            gs.setAlphaSourceFlag(true);
            gs.setBlendMode(BlendMode.NORMAL);
            cs.setGraphicsStateParameters(gs);

            int row = options.row;
            int column = options.column;
            float imgWidth = options.width;
            float imgHeight = options.height;
            float padding = options.padding;
            int degree = options.degree;

            // 计算获得每个单元格的宽高
            float cellWidth = (width - padding * 2) / column;
            float cellHeight = (height - padding * 2) / row;

            // 偏移量，如果单元格宽高大于图片宽高，这可以使图片居中
            float xOffset = padding + (cellWidth - imgWidth) / 2;
            float yOffset = padding + (cellHeight - imgHeight) / 2;

            float x;
            float y;
            for (int i = 0; i < row; i++) {
                y = i * cellHeight + yOffset;

                for (int j = 0; j < column; j++) {
                    x = j * cellWidth + xOffset;

                    // 旋转导致的x位置修正
                    x += Math.sin(Math.toRadians(degree)) * imgHeight;

                    Matrix matrix = new Matrix();
                    // 先移位
                    matrix.translate(x, y);
                    // 旋转
                    matrix.rotate(Math.toRadians(degree));
                    // 修改尺寸（必须在旋转后面，否则会变形）
                    matrix.scale(imgWidth, imgHeight);

                    // 画图
                    cs.drawImage(pdImage, matrix);
                }
            }

        }

    }

    static class WatermarkOptions {
        /**
         * 边距
         */
        float padding = 20;
        /**
         * 图片宽度
         */
        float width;
        /**
         * 图片高度
         */
        float height;
        /**
         * 旋转角度
         */
        int degree = 0;
        /**
         * 行数
         */
        int row = 1;
        /**
         * 列数
         */
        int column = 1;

        public WatermarkOptions() {

        }

        public WatermarkOptions padding(int p) {
            if (p < 0) {
                throw new IllegalArgumentException("边距不能小于0");
            }
            this.padding = p;
            return this;
        }

        public WatermarkOptions layout(int row, int column) {
            if (row <= 0 || column <= 0) {
                throw new IllegalArgumentException("行数或列数必须大于0");
            }
            this.row = row;
            this.column = column;

            return this;
        }

        public WatermarkOptions size(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public WatermarkOptions rotate(int degree) {
            this.degree = degree;
            return this;
        }

    }

}
