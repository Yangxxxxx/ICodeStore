package com.example.spider.dictionary;

import android.support.annotation.NonNull;

import com.example.spider.annotation.AttrName;
import com.example.spider.annotation.FieldPath;

public class Word {
    @FieldPath(".hw.dhw")
    public String name;

    @FieldPath(".uk.dpron-i .pron.dpron")
    public String ukPhonetic;

    @FieldPath(attr = AttrName.SRC, value =  ".uk.dpron-i source[type=audio/mpeg]")
    public String ukSound;

    @FieldPath(".us.dpron-i .pron.dpron")
    public String usPhonetic;

    @FieldPath(attr = AttrName.SRC, value =  ".us.dpron-i source[type=audio/mpeg]")
    public String usSound;

    @FieldPath(".pr.entry-body__el")
    public Attribution[] attributions;

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        builder.append("uk:" + ukPhonetic + "  us:" + usPhonetic);
        builder.append("\n");
        builder.append(ukSound);
        builder.append("\n");
        builder.append(usSound);
        builder.append("\n");
        builder.append("\n");
        if(attributions != null){
            for (Attribution item: attributions){
                builder.append(item.toString());
                builder.append("\n\n\n");
            }
        }

        return builder.toString();
    }
}
