package cn.augrain.easy.pdf.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * PDF制作者的元数据信息
 *
 * @author biaoy
 * @since 2023/01/29
 */
@Setter
@Getter
@Builder
public class Metadata {

    /**
     * 作者
     */
    private String author;

    /**
     * 标题
     */
    private String title;

    /**
     * 创作者
     */
    private String creator;

    /**
     * 主题
     */
    private String subject;

    /**
     * 文档关键字
     */
    private String keywords;

    /**
     * pdf制作程序
     */
    private String producer;

    /**
     * 创建日期
     */
    private LocalDateTime creationDate;

    /**
     * 修改日期
     */
    private LocalDateTime modificationDate;

    public static Metadata defaultMetadata() {
        return Metadata.builder()
                .creationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .author("系统")
                .creator("系统")
                .title("转pdf")
                .build();
    }
}
