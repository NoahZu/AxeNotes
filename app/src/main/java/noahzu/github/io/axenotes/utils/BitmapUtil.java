package noahzu.github.io.axenotes.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Administrator on 2016/3/11.
 */
public class BitmapUtil {

    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight){
        if (bm == null){
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
        if (bm != null & !bm.isRecycled()){
            bm.recycle();//销毁原图片
            bm = null;
        }
        return newbm;
    }
}
