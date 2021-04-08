package com.example.spider.dictionary;

import androidx.annotation.NonNull;

import com.example.spider.annotation.FieldPath;

public class Translation {
    @FieldPath(".epp-xref")
    public String usageLevel; //单词的常见程度。例如：A1 A2 B1 B2...

    @FieldPath(".def-info.ddef-info .gc.dgc")
    public String[] attr; //单词的词性（不及物动词...）。例如 I T...  代码意义参照：https://dictionary.cambridge.org/help/codes.html

    @FieldPath(".def.ddef_d.db")
    public String enExplanation;

    @FieldPath(".trans.dtrans.dtrans-se")
    public String zhExplanation;

    @FieldPath(".examp.dexamp")
    public SampleSentence[] sentences;

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(usageLevel);
        builder.append("\n");
        if(attr != null) {
            for (String item: attr){
                builder.append(item);
                builder.append(":");
            }
            builder.append("\n");
        }
        builder.append(enExplanation);
        builder.append("\n");
        builder.append(zhExplanation);
        builder.append("\n\n");

        if(sentences != null) {
            for (SampleSentence item : sentences) {
                builder.append(item.toString());
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
