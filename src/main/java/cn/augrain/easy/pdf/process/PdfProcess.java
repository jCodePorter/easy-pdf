package cn.augrain.easy.pdf.process;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

/**
 * @author biaoy
 * @since 2023/01/16
 */
public interface PdfProcess {
    void process(PDDocument document) throws IOException;
}
