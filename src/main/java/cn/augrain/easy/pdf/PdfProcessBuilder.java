package cn.augrain.easy.pdf;

import cn.augrain.easy.pdf.maker.PdfMaker;
import cn.augrain.easy.pdf.model.Metadata;
import cn.augrain.easy.pdf.model.TextWaterMarkConfig;
import cn.augrain.easy.pdf.process.EncryptionProcess;
import cn.augrain.easy.pdf.process.MetadataProcess;
import cn.augrain.easy.pdf.process.TextWatermarkProcess;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * PdfProcessor处理器构建者
 *
 * @author biaoy
 * @since 2023/01/16
 */
public class PdfProcessBuilder {
    private final PdfProcessor pdfProcessor;

    public PdfProcessBuilder(String srcFileName) throws IOException {
        pdfProcessor = new PdfProcessor(new File(srcFileName));
    }

    public PdfProcessBuilder(File srcFile) throws IOException {
        pdfProcessor = new PdfProcessor(srcFile);
    }

    public PdfProcessBuilder(InputStream in) throws IOException {
        pdfProcessor = new PdfProcessor(in);
    }

    public PdfProcessBuilder(PdfMaker pdfMaker, String srcDirection) throws IOException {
        pdfProcessor = new PdfProcessor(pdfMaker, srcDirection);
    }

    public PdfProcessBuilder textWatermark(String watermark) {
        return textWatermark(watermark, TextWaterMarkConfig.defaultConfig());
    }

    public PdfProcessBuilder textWatermark(String watermark, TextWaterMarkConfig waterMarkConfig) {
        pdfProcessor.getProcesses().add(new TextWatermarkProcess(watermark, waterMarkConfig));
        return this;
    }

    public PdfProcessBuilder removePassword() {
        pdfProcessor.removePassword();
        return this;
    }

    public PdfProcessBuilder password(String password) {
        pdfProcessor.getProcesses().add(new EncryptionProcess(password));
        return this;
    }

    public PdfProcessBuilder password(String ownerPassword, String userPassword) {
        pdfProcessor.getProcesses().add(new EncryptionProcess(ownerPassword, userPassword));
        return this;
    }

    public PdfProcessBuilder metadata(Metadata pdfMetadata) {
        pdfProcessor.getProcesses().add(new MetadataProcess(pdfMetadata));
        return this;
    }

    /**
     * 读取pdf页数
     *
     * @return 返回页数
     * @throws IOException 读取文件过程中可能抛出IO异常
     */
    public int readPages() throws IOException {
        return pdfProcessor.readPages();
    }

    /**
     * 输出到本地文件，此方法终结构建者继续构建
     *
     * @param descFilePath 本地文件路径
     * @throws IOException 抛出IO异常，比如文件不可写
     */
    public void asFile(String descFilePath) throws IOException {
        pdfProcessor.asFile(descFilePath);
    }

    /**
     * 返回处理pdf处理的字节流
     *
     * @throws IOException 读取文件过程中可能抛出IO异常
     */
    public byte[] toBytes() throws IOException {
        return pdfProcessor.toBytes();
    }

    /**
     * 拆分成一个一个图片
     *
     * @param descFileDirection 输出图片路径
     * @param imageNamePrefix   拆分成图片的名称前缀
     * @param dpi               精度, dpi越大转换后越清晰，相对转换速度越慢
     * @throws IOException 处理文件过程中可能的IO异常
     */
    public void toSplitImage(String descFileDirection, String imageNamePrefix, int dpi) throws IOException {
        pdfProcessor.toSplitImage(descFileDirection, imageNamePrefix, dpi);
    }
}
