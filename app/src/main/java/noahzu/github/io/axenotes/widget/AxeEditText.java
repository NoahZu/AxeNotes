package noahzu.github.io.axenotes.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.List;

import noahzu.github.io.axenotes.entity.AxePicture;

/**
 * Created by Administrator on 2016/3/8.
 */
public class AxeEditText extends EditText {


    public AxeEditText(Context context) {
        super(context);
    }

    public AxeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void insertBitmap(Bitmap bitmap,String imageHolder) {
        final SpannableString ss = new SpannableString(imageHolder);
        //得到drawable对象，即所要插入的图片
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, imageHolder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }
}
