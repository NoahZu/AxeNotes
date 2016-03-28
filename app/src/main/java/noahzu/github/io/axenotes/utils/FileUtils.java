package noahzu.github.io.axenotes.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/3/24.
 */
public class FileUtils {
    /**
     * 在Environment.getExternalStorageDirectory()下边创建指定文件
     * @param path 目录，可多层
     * @param fileName 文件名，将创建于目录下边
     * @return
     */
    public static File createFile(String path,String fileName){
        File file = new File(Environment.getExternalStorageDirectory(),path);
        if(!file.exists()){
            file.mkdirs();
        }
        File targetFile = new File(file,fileName);
        if(!targetFile.exists()){
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return targetFile;
    }

}
