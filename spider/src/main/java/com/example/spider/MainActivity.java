package com.example.spider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.spider.annotation.ClassPath;
import com.example.spider.annotation.FieldPath;
import com.example.spider.dictionary.Translation;
import com.example.spider.dictionary.Word;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String PATH_SEPARATOR = ">";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTranslation();
    }


    private void getTranslation(){
        runOnSpiderThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e(TAG, "start");
                    Document doc = Jsoup.connect("https://dictionary.cambridge.org/zhs/%E8%AF%8D%E5%85%B8/%E8%8B%B1%E8%AF%AD-%E6%B1%89%E8%AF%AD-%E7%AE%80%E4%BD%93/organize").get();
                    Log.e(TAG, doc.title());

                    Word word = elementTransfer2Object(doc, Word.class);

                    Log.e(TAG, "word " + word.name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private <T> T elementTransfer2Object(Element parentElement, Class<T> tClass){
        try {
            T result = tClass.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            for (Field item: fields){
                String fieldPath = item.getAnnotation(FieldPath.class).value();
//                Elements itemElements = parentElement.getElementsByClass(fieldPath);
                Elements itemElements = getElementsByPath(parentElement, fieldPath);
                if(itemElements == null) continue;

                if(item.getType().isArray()){
                    Class<?> subClass = item.getType().getComponentType();
                    Object array = Array.newInstance(subClass, itemElements.size());

                    for (int i = 0; i < itemElements.size(); i++){
                        Array.set(array, i, elementTransfer2Object(itemElements.get(i), subClass));
                    }

                    item.set(result, array);
                }else if(!itemElements.isEmpty()){
                    item.set(result, itemElements.first().text());
                }
            }

            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        path 多个路径中间中 @see PATH_SEPARATOR 符号分开
     */
    private Elements getElementsByPath(Element parentElement, String path){
        String[] paths = path.split(PATH_SEPARATOR);
        String firstPath = paths[0];
        if(paths.length == 1){
            if(!TextUtils.isEmpty(firstPath)) {
                return parentElement.getElementsByClass(firstPath);
            }
        }else {
            String newPath = getNextPath(paths);
            Elements elements = parentElement.getElementsByClass(firstPath);
            for(Element item: elements){
                Elements result = getElementsByPath(item, newPath);
                if(result != null && !result.isEmpty()){
                    return result;
                }
            }
        }
        return null;
    }

    private String getNextPath(String[] path){
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < path.length; i++){
            builder.append(path[i]);
            builder.append(PATH_SEPARATOR);
        }
        return builder.toString();
    }

    private void runOnSpiderThread(final Runnable task){
        new Thread(){
            @Override
            public void run() {
                super.run();
                task.run();
            }
        }.start();
    }
}
