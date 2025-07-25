package cn.augrain.easy.pdf;

import cn.augrain.easy.pdf.maker.ImagePdfMaker;
import cn.augrain.easy.pdf.maker.PdfMaker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * PDF操作入口，通过简单的load方法，返回PdfProcessBuilder对象，方便进行后续的构建
 *
 * @author biaoy
 * @since 2023/01/16
 */
public class EasyPDF {

    /**
     * 加载本地文件
     *
     * @param srcFilePath 源文件路径
     * @return 返回PdfProcessBuilder
     * @throws IOException 如果文件不存在抛出IOException
     */
    public static PdfProcessBuilder load(String srcFilePath) throws IOException {
        return new PdfProcessBuilder(srcFilePath);
    }

    /**
     * 加载本地文件
     *
     * @param srcFile 源文件
     * @return 返回PdfProcessBuilder
     * @throws IOException 如果文件不存在抛出IOException
     */
    public static PdfProcessBuilder load(File srcFile) throws IOException {
        return new PdfProcessBuilder(srcFile);
    }

    /**
     * 加载远程文件
     *
     * @param url 文件url地址
     * @return 返回PdfProcessBuilder
     * @throws IOException 如果文件不存在抛出IOException
     */
    public static PdfProcessBuilder loadUrl(String url) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            return new PdfProcessBuilder(in);
        }
    }

    /**
     * 通过图片创建
     *
     * @param srcDirection 图片所在目录
     * @return 返回PdfProcessBuilder
     * @throws IOException 如果文件不存在抛出IOException
     */
    public static PdfProcessBuilder createWithImage(String srcDirection) throws IOException {
        PdfMaker maker = new ImagePdfMaker();
        return new PdfProcessBuilder(maker, srcDirection);
    }
}
