/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sim.kantordesa.config;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Razik
 */
public class AppContext {
    private static Map<String, Object> data = new HashMap<>();

    public static void put(String key, Object value) {
        data.put(key, value);
    }

    public static Object get(String key) {
        return data.get(key);
    }
}
