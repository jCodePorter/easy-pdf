package cn.augrain.easy.pdf.maker;

import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * 通过其他类型文件来构建pdf
 */
public interface PdfMaker {

    /**
     * 创建pdf文档
     *
     * @param direction 文件目录
     * @return PDDocument
     */
    PDDocument make(String direction);
}
