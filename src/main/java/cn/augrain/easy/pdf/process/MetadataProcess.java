package cn.augrain.easy.pdf.process;

import cn.augrain.easy.pdf.model.Metadata;
import cn.augrain.easy.pdf.utils.DateTimeUtils;
import cn.augrain.easy.tool.core.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.IOException;

/**
 * pdf文档元数据处理
 *
 * @author biaoy
 * @since 2023/01/29
 */
public class MetadataProcess implements PdfProcess {

    private final Metadata metadata;

    public MetadataProcess(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void process(PDDocument document) throws IOException {
        PDDocumentInformation info = document.getDocumentInformation();
        if (StringUtils.isNotBlank(metadata.getAuthor())) {
            info.setAuthor(metadata.getAuthor());
        }

        if (StringUtils.isNotBlank(metadata.getTitle())) {
            info.setTitle(metadata.getTitle());
        }

        if (StringUtils.isNotBlank(metadata.getCreator())) {
            info.setCreator(metadata.getCreator());
        }

        if (StringUtils.isNotBlank(metadata.getSubject())) {
            info.setSubject(metadata.getSubject());
        }

        if (StringUtils.isNotBlank(metadata.getKeywords())) {
            info.setKeywords(metadata.getKeywords());
        }

        if (StringUtils.isNotBlank(metadata.getProducer())) {
            info.setProducer(metadata.getProducer());
        }

        if (metadata.getCreationDate() != null) {
            info.setCreationDate(DateTimeUtils.toCalendar(metadata.getCreationDate()));
        }

        if (metadata.getModificationDate() != null) {
            info.setModificationDate(DateTimeUtils.toCalendar(metadata.getModificationDate()));
        }
    }
}
