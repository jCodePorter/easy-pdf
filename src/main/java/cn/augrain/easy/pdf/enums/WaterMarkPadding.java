package cn.augrain.easy.pdf.enums;

public enum WaterMarkPadding {
    TILE("平铺"),
    CENTER("居中"),
    ;

    private String desc;

    WaterMarkPadding(String desc){
        this.desc = desc;
    }
}
