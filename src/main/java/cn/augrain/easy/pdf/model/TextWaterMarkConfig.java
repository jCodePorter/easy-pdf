package cn.augrain.easy.pdf.model;

import cn.augrain.easy.pdf.enums.WaterMarkPadding;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文本水印配置
 *
 * @author biaoy
 * @since 2025/01/03
 */
@Getter
@Setter
@ToString
public class TextWaterMarkConfig {

    /**
     * 字体路径
     */
    private String fontPath;

    /**
     * 字体大小
     */
    private float fontSize = 40;

    /**
     * 旋转角度
     */
    private float angle = 45;

    /**
     * 字体颜色
     */
    private String fontColor = "FF0000";

    /**
     * 透明度,可选值范围[0,1]，值越大，透明度越高
     */
    private float fontOpaque = 0.2f;

    /**
     * 平铺方式
     */
    private WaterMarkPadding padding = WaterMarkPadding.TILE;

    public static TextWaterMarkConfig defaultConfig() {
        TextWaterMarkConfig config = new TextWaterMarkConfig();
        config.setFontOpaque(0.5f);
        return config;
    }
}
