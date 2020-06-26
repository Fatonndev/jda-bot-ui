/*
 * Copyright 2020 KeviTV.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.kevitv.obvilionNetwork.utils;

import ru.kevitv.obvilionNetwork.Main;
import ru.kevitv.obvilionNetwork.commands.Langs;

import java.io.*;
import java.util.Properties;

public class Lang {
    public static void init() {
        // Создаём языковые файлы если не существуют
        final File dir = new File("config");
        if (!dir.exists()) dir.mkdir();

        final File dir1 = new File("config/lang");
        if (!dir1.exists()) dir1.mkdir();

        for (String[] langs : Langs.langs) {
            final File langFile = new File("config/lang/" + langs[0] + ".properties");
            if (!langFile.exists()) {
                try (InputStream in = Main.class
                        .getClassLoader()
                        .getResourceAsStream("lang/" + langs[0] + ".properties");
                    OutputStream out = new FileOutputStream("config/lang/" + langs[0] + ".properties")) {
                    int data;
                    while ((data = in.read()) != -1) {
                        out.write(data);
                    }
                    System.out.println("Lang file " + langs[0] + " (" + langs[1] + ") is created!");
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }
    }

    /* Получение компонента из языка */
    public static String get(String nameStr) {
        return get(nameStr, "en_US");
    }

    public static String get(String nameStr, String lang) {
        String out = "LANG_ERROR";
        FileInputStream fileInputStream;
        Properties prop = new Properties();
        try {
            fileInputStream = new FileInputStream("config/lang/" + lang + ".properties");
            prop.load(fileInputStream);
            out = prop.getProperty(nameStr);

            if (out != null) {
                out = new String(out.getBytes("ISO-8859-1"), "UTF-8");
            } else {
                out = "LANG_ERROR("+nameStr+")";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "LANG_ERROR";
        }
        return out;
    }

    public static String get(String nameStr, String lang, String... replace) {
        String result = get(nameStr, lang);
        for(int i = 0; replace.length > i; i++) {
            result = result.replace("{"+i+"}", replace[i]);
        }
        return result;
    }
}
