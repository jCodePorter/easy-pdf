package cn.augrain.easy.pdf.process;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.*;

import java.io.IOException;

/**
 * 加密处理
 *
 * @author biaoy
 * @since 2023/01/16
 */
public class EncryptionProcess implements PdfProcess {

    private final String ownerPassword;

    private final String userPassword;

    public EncryptionProcess(String password) {
        this.ownerPassword = password;
        this.userPassword = password;
    }

    public EncryptionProcess(String ownerPassword, String userPassword) {
        this.ownerPassword = ownerPassword;
        this.userPassword = userPassword;
    }

    @Override
    public void process(PDDocument document) throws IOException {
        AccessPermission permissions = new AccessPermission();
        permissions.setCanExtractContent(false);
        permissions.setCanModify(false);
        permissions.setCanPrint(false);

        StandardProtectionPolicy p = new StandardProtectionPolicy(ownerPassword, userPassword, permissions);
        SecurityHandler sh = new StandardSecurityHandler(p);
        sh.prepareDocumentForEncryption(document);

        PDEncryption encryptionOptions = new PDEncryption();
        encryptionOptions.setSecurityHandler(sh);
        document.setEncryptionDictionary(encryptionOptions);
    }
}
