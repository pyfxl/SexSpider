package com.android.sexspider4.helper;

import android.util.Log;

import com.android.sexspider4.utils.StringUtils;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SegHelper {

    private SegHelper() { }

    public static String getSegString(String s, List<String> stops) {
        String str = "";
        StringBuilder sb = new StringBuilder();

        IKSegmenter ik = new IKSegmenter(new StringReader(removeString(s)), true);
        Lexeme lexeme = null;
        try {
            while ((lexeme=ik.next()) != null) {
                String text = lexeme.getLexemeText();
                //if (StringUtils.strLength(str) < 4) continue;
                //if (stops.contains(text)) continue;
                sb.append(text+"|");
            }
            str = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("SEG", StringUtils.removeStringRight(str, 1));
        return StringUtils.removeStringRight(str, 1);
    }

    private static String removeString(String s) {
        return s.replaceAll("\\d+[p|P|照片|偷拍|自拍|原创|出品]", "");
    }

}
