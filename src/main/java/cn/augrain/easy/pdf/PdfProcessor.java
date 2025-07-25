package cn.augrain.easy.pdf;

import cn.augrain.easy.pdf.maker.PdfMaker;
import cn.augrain.easy.pdf.process.PdfProcess;
import cn.augrain.easy.tool.exception.UtilsRuntimeException;
import cn.augrain.easy.tool.io.FileUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF处理器
 *
 * @author biaoy
 * @since 2023/01/16
 */
@Slf4j
public class PdfProcessor {
    private final PDDocument document;

    @Getter
    private final List<PdfProcess> processes = new ArrayList<>();

    public PdfProcessor(File file) throws IOException {
        this.document = PDDocument.load(file);
    }

    public PdfProcessor(InputStream in) throws IOException {
        this.document = PDDocument.load(in);
    }

    public PdfProcessor(PdfMaker pdfMaker, String srcDirection) throws IOException {
        this.document = pdfMaker.make(srcDirection);
    }

    /**
     * 将处理的pdf保存为本地文件
     *
     * @param descFilePath 本地文件路径
     * @throws IOException pdf转换过程中，或者输出为本地文件过程中，可能抛出IO异常
     */
    public void asFile(String descFilePath) throws IOException {
        doProcess();
        document.save(descFilePath);
        document.close();
    }

    private void doProcess() throws IOException {
        for (PdfProcess process : processes) {
            process.process(document);
        }
    }

    /**
     * 返回处理后的pdf对应的字节流
     *
     * @throws IOException pdf转换过程中，或者输出过程中可能抛出IO异常
     */
    public byte[] toBytes() throws IOException {
        doProcess();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);
        document.close();
        return out.toByteArray();
    }

    /**
     * 读取pdf页数
     *
     * @return 返回页数
     * @throws IOException document关流过程中，可能抛出IO异常
     */
    public int readPages() throws IOException {
        int pages = document.getNumberOfPages();
        document.close();
        return pages;
    }

    public void removePassword() {
        document.setAllSecurityToBeRemoved(true);
    }

    public void toSplitImage(String descFileDirection, String imageNamePrefix, int dpi) throws IOException {
        doProcess();
        if (FileUtils.createDirectory(descFileDirection)) {
            PDFRenderer renderer = new PDFRenderer(this.document);
            int pages = this.document.getNumberOfPages();
            for (int i = 0; i < pages; i++) {
                String imgFilePathPrefix = descFileDirection + File.separator + imageNamePrefix;
                String imgFilePath = imgFilePathPrefix + "_" + (i + 1) + ".png";
                File dstFile = new File(imgFilePath);
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                ImageIO.write(image, "png", dstFile);
            }
            if (log.isDebugEnabled()) {
                log.debug("PDF文档转PNG图片成功！");
            }
        } else {
            throw new UtilsRuntimeException("目录创建异常");
        }
    }
}
