/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dherik.util;

import com.google.gson.Gson;

public class JsonTransformer {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }
    
    public static <T> T  fromJson(String json, Class<T> classe) {
        return new Gson().fromJson(json, classe);
    }

}
