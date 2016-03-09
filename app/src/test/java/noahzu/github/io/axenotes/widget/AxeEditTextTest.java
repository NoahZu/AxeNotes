package noahzu.github.io.axenotes.widget;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Created by Administrator on 2016/3/8.
 */
public class AxeEditTextTest extends TestCase {

    public void testConvertHolderToBitmap() throws Exception {
        String split = "axeImg";
        String mAxeString1 = "axeImgabcaxeImg";
        String mAxeString2 = "bbbbaxeImgnnnnnaxeImgnnnnnllkokpoaxeImgkkkpjoiaxeImg";
        String mAxeString3 = "axeImgbbbbaxeImgnnnnnaxeImgnnnnnllkokpoaxeImgkkkpjoi";
        String mAxeString4 = "bbbbaxeImgnnnnnaxeImgnnnnnllkokpoaxeImgkkkpjoi";



        String[] result1 = AxeEditText.splitStringIncludingSplits(mAxeString1,split);
        String[] result2 = AxeEditText.splitStringIncludingSplits(mAxeString2,split);
        String[] result3 = AxeEditText.splitStringIncludingSplits(mAxeString3,split);
        String[] result4 = AxeEditText.splitStringIncludingSplits(mAxeString4,split);

        for(int i = 0 ;i<result1.length;i++){
            System.out.print(" "+result1[i]);
        }
        System.out.println();
        for(int i = 0 ;i<result2.length;i++){
            System.out.print(" " + result2[i]);
        }
        System.out.println();
        for(int i = 0 ;i<result3.length;i++){
            System.out.print(" "+result3[i]);
        }
        System.out.println();
        for(int i = 0 ;i<result4.length;i++){
            System.out.print(" "+result4[i]);
        }
        System.out.println();
    }
}