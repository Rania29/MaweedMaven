package entity.domain.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.MessagingException;
import javax.servlet.http.Part;

public class ImageUploader {

    private static ImageUploader imageUploader = null;
    private String folderName;
    private String absolutePath;
    private Part image;
    private final String APP_PATH = "../resources/images/";
    private final String ABSOLUTE_PATH = "C:\\Users\\sawad\\Documents\\NetBeansProjects\\ShifaaApp\\web\\resources\\images\\";
//    private final String ABSOLUTE_PATH = "/app/glassfish4/glassfish/domains/domain1/applications/ShifaaApp/resources/images/";
    private String appInternalPath;

    public ImageUploader(Part image, String folderName) {
        this.image = image;
        this.folderName = folderName;
    }

    public void setImage(Part image) {
        this.image = image;
    }

    public static ImageUploader getInstance(Part image, String folderName) {
        if (imageUploader == null) {
            imageUploader = new ImageUploader(image, folderName);
        }
        return imageUploader;
    }

    public String getAppInternalPath() {
        return appInternalPath;
    }

    public String getAppPath() {
        return APP_PATH + folderName + "/";
    }

    public String getAbsolutePath() {
//        return ABSOLUTE_PATH + folderName + "\\";
        return ABSOLUTE_PATH + folderName + "/";
    }

    public int doUpload() throws MessagingException {
        String imgName = image.getSubmittedFileName();
        if (!imgName.equals("")) {
            imgName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + imgName;
            absolutePath = getAbsolutePath() + imgName;
            try {
                InputStream inputStream = image.getInputStream();
                File file = new File(absolutePath);
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.close();
                inputStream.close();
            } catch (Exception e) {
                System.out.println("Unable to save file due to ......." + e.getMessage());
                return 1;
            }
            appInternalPath = getAppPath() + imgName;
        }
        return 0;
    }
}
