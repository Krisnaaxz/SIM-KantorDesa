/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sim.kantordesa.mailtemplate;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Razik
 */
public class MailData {
    private static Map<String, String> data = new HashMap<>();

    public static void put(String key, String value) {
        data.put(key, value);
    }

    public static String get(String key) {
        return data.get(key);
    }
    
    public static Map<String, String> getMap() {
        return data;
    }
}
