package cn.augrain.easy.pdf;

import cn.augrain.easy.pdf.model.Metadata;
import cn.augrain.easy.tool.exception.UtilsRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * pdf工具类
 *
 * @author biaoy
 * @since 2022/09/23
 */
@Slf4j
public class PDFUtils {

    private PDFUtils() {

    }

    /**
     * 读取网络pdf文件的页数
     *
     * @param url 网络url路径
     * @return 页数
     */
    public static int readPages(String url) {
        try {
            return EasyPDF.loadUrl(url).readPages();
        } catch (IOException e) {
            throw new UtilsRuntimeException(e);
        }
    }

    /**
     * 读取本地pdf文件的页数
     *
     * @param file 本地文件
     * @return 页数
     */
    public static int readPages(File file) {
        try {
            return EasyPDF.load(file).readPages();
        } catch (IOException e) {
            throw new UtilsRuntimeException(e);
        }
    }

    /**
     * 加密
     *
     * @param srcFilePath  源文件路径
     * @param descFilePath 目标文件路径
     * @param password     密码，所有者密码和用户密码设置为一样的
     */
    public static void addPassword(String srcFilePath, String descFilePath, String password) {
        addPassword(srcFilePath, descFilePath, password, password);
    }

    /**
     * 加密
     * 用户密码：要求用户在打开文件时，需要输入密码
     * 所有者密码：打开PDF文件并进行阅读，并不需要所有者密码，只有更改权限设置或进行受限制操作时（打印，编辑和复制PDF中的内容），才需要输入所有者密码。
     *
     * @param srcFilePath   源文件路径
     * @param ownerPassword 所有者密码
     * @param userPassword  用户密码
     */
    public static void addPassword(String srcFilePath, String descFilePath, String ownerPassword, String userPassword) {
        try {
            EasyPDF.load(srcFilePath)
                    .password(ownerPassword, userPassword)
                    .asFile(descFilePath);
        } catch (Exception e) {
            throw new UtilsRuntimeException(e);
        }
    }

    /**
     * 添加水印
     *
     * @param srcFilePath  源文件路径
     * @param descFilePath 目标文件路径
     * @param watermark    水印名称
     */
    public static void addWatermark(String srcFilePath, String descFilePath, String watermark) {
        try {
            EasyPDF.load(srcFilePath)
                    .textWatermark(watermark)
                    .asFile(descFilePath);
        } catch (Exception e) {
            throw new UtilsRuntimeException(e);
        }
    }

    /**
     * 移除密码
     *
     * @param srcFilePath  源文件
     * @param descFilePath 输出文件
     */
    public static void removePassword(String srcFilePath, String descFilePath) {
        try {
            EasyPDF.load(srcFilePath)
                    .removePassword()
                    .asFile(descFilePath);
        } catch (Exception e) {
            throw new UtilsRuntimeException(e);
        }
    }

    /**
     * 通过图片创建pdf
     *
     * @param srcDirection 图片所在目录
     * @param outPath      输出路径
     */
    public static void createWithImage(String srcDirection, String outPath) {
        try {
            EasyPDF.createWithImage(srcDirection)
                    .metadata(Metadata.defaultMetadata())
                    .asFile(outPath);
        } catch (Exception e) {
            throw new UtilsRuntimeException(e);
        }
    }


    /**
     * 通过图片创建pdf
     *
     * @param srcDirection 图片所在目录
     * @param metadata     元数据
     * @param outPath      输出路径
     */
    public static void createWithImage(String srcDirection, Metadata metadata, String outPath) {
        try {
            EasyPDF.createWithImage(srcDirection)
                    .metadata(metadata)
                    .asFile(outPath);
        } catch (Exception e) {
            throw new UtilsRuntimeException(e);
        }
    }
}
