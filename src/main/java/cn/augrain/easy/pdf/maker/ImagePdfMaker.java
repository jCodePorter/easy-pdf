package cn.augrain.easy.pdf.maker;

import cn.augrain.easy.tool.io.FileUtils;
import cn.augrain.easy.tool.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片创建pdf
 *
 * @author biaoy
 * @since 2023/01/16
 */
@Slf4j
public class ImagePdfMaker implements PdfMaker {

    private static final List<String> SUPORT_IMAGE = Arrays.asList("jpg", "png", "jpeg");

    public static PDDocument doCreatePdf(List<File> files) throws IOException {
        PDDocument doc = new PDDocument();
        doc.getDocument().setVersion(1.7F);
        for (File file : files) {
            BufferedImage bufferedImage = ImageIO.read(file);

            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bufferedImage);
            float w = pdImage.getWidth();
            float h = pdImage.getHeight();

            PDPage blankPage = new PDPage(new PDRectangle(w, h));
            PDPageContentStream contents = new PDPageContentStream(doc, blankPage);
            contents.drawImage(pdImage, 0, 0, w, h);
            contents.close();

            doc.addPage(blankPage);

            if (log.isDebugEnabled()) {
                log.debug("image inserted: {}", file.getName());
            }
        }
        return doc;
    }

    @Override
    public PDDocument make(String direction) {
        File imgDir = new File(direction);
        List<File> files = new ArrayList<>();
        if (imgDir.exists() && imgDir.isDirectory()) {
            File[] fileArr = imgDir.listFiles();
            AssertUtils.assertTrue(fileArr != null, "目录下图片为空");

            for (File file : fileArr) {
                if (file.isFile() && isImage(file.getName())) {
                    files.add(file);
                }
            }
        }

        AssertUtils.assertTrue(!files.isEmpty(), "目录下图片为空");
        try {
            return doCreatePdf(files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isImage(String name) {
        String extension = FileUtils.getExtension(name);
        return SUPORT_IMAGE.contains(extension);
    }
}
