package org.nabsha.ascender.api;

/**
 * Created by Nabeel Shaheen on 3/08/2015.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

/**
 * The type Json transformer.
 */
public class JsonTransformer implements ResponseTransformer {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public  String render(Object model) {
        synchronized (model) {
            return gson.toJson(model);
        }
    }

}