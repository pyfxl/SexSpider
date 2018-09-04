package com.android.sexspider4.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.android.sexspider4.MyApplication;
import com.android.sexspider4.R;
import com.android.sexspider4.image.bean.ImageBean;
import com.android.sexspider4.list.bean.ListBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by feng on 2017/5/5.
 */

public class ImageHelper {

    private ImageHelper() {
    }

    //从url取新文件名
    public static String getNewFileName(String link, int imageId) {
        String fileName = getFileName(link);
        String fileExtName = getExtName(fileName);
        return imageId + (fileName.equals("") ? ".jpg" : fileExtName);
    }

    //从url取新文件名
    public static String getThumbName(String link) {
        String fileName = getFileName(link);
        String fileExtName = getExtName(fileName);
        return "thumb" + (fileName.equals("") ? ".jpg" : fileExtName);
    }

    //从url取文件名
    private static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    //从文件名取扩展名
    private static String getExtName(String name) {
        try {
            String ext = name.substring(name.lastIndexOf("."));
            if(ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".bmp") || ext.equals(".gif") || ext.equals(".png")) {
                return ext;
            } else {
                return ".jpg";
            }
        } catch (Exception e) {
            return ".jpg";
        }
    }

    //取图片路径
    private static String getFilePath(ImageBean image) {
        return MyApplication.FILE_PATH + image.siteId + File.separator + image.listId + File.separator + image.imageName;
    }

    //从bytes保存图片
    private static boolean saveImageFromBytes(ImageBean image, byte[] bytes) {
        try {
            File file = getFile(image);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

            FileOutputStream output = new FileOutputStream(file, false);
            output.write(bytes);
            output.close();

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                if (bitmap != null) {
                    //保存缩略图
                    saveThumbImage(image);
                    return true;
                }
            }catch (OutOfMemoryError outOfMemoryError){
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //取图片画廊用
    public static File getFile(ImageBean image) {
        String path = getFilePath(image);
        return getFile(path);
    }

    //取图片画廊用
    private static File getFile(String path) {
        File file = new File(path);
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return file;
    }

    //保存.nomedia
    public static void saveNoMedia() {
        try {
            File file = getFile(MyApplication.FILE_PATH + ".nomedia");
            if(file != null) file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //保存缩略图
    private static void saveThumbImage(ImageBean image) {
        try {
            File file = getFile(image);

            String imageName = "t_" + image.imageName;
            ImageBean thumbImage = new ImageBean(image.siteId, image.listId, imageName);

            File thumbFile = getFile(thumbImage);
            if(!thumbFile.getParentFile().exists()) thumbFile.getParentFile().mkdirs();

            FileOutputStream output = new FileOutputStream(thumbFile, false);

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                if (bitmap != null) {
                    bitmap = resizeBitmap(bitmap, MyApplication.getAppContext().getResources().getDimensionPixelSize(R.dimen.list_item_height));
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)) {
                        output.flush();
                    }
                }
            } catch (OutOfMemoryError outOfMemoryError) {
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //取缩略图
    public static Bitmap getThumbImage(ListBean list) {
        String imageName = "t_" + list.listPicture;
        ImageBean image = new ImageBean(list.siteInfo.siteId, list.listId, imageName);

        return getThumbImage(image);
    }

    //取缩略图
    private static Bitmap getThumbImage(ImageBean image) {
        File file = getFile(image);
        if(!file.exists()) return null;

        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        if(bitmap == null) {
            //bitmap = BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(), R.mipmap.ic_launcher);
        }

        return bitmap;
    }

    //改变图片大小
    private static Bitmap resizeBitmap(Bitmap bitmap, int newSize) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = (float)newSize / width;
        float scaleHeight = (float)newSize / height;
        if (width >= height) {
            matrix.postScale(scaleWidth, scaleWidth);
        } else {
            matrix.postScale(scaleHeight, scaleHeight);
        }

        return Bitmap.createBitmap(bitmap, 0, 0, (int)width, (int)height, matrix, true);
    }

    //删除图片
    public static void deleteImage(ListBean list) {
        ImageBean image = new ImageBean(list.siteInfo.siteId, list.listId, "");

        File file = getFile(image);
        if (!file.isDirectory()) {
            return;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            }
        }
        file.delete();
    }

    //从网络下载图片
    public static boolean downImage(ImageBean image, String domain) {
        byte[] bytes = HttpHelper.getBytesFromLink(image.imageLink, domain);
        return ImageHelper.saveImageFromBytes(image, bytes);
    }

}
