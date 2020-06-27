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

import ru.kevitv.obvilionNetwork.gui.Controller;

import java.io.*;
import java.util.Properties;

public class Config {
    public static int maxNodes = getInt("maxNodes");
    public static int UpdateTime = getInt("updateTime");
    public static int FastUpdateTime = getInt("fastUpdateTime");

    public static long ping;
    public static long dbPing;
    public static long startTime = System.currentTimeMillis();

    public static String prefix = get("prefix");
    public static String logFileName = "LOG.log";
    public static String[] args;

    public static boolean nogui = false;

    public static Controller controller;

    public static void init() {
        // Создаём конфиг файлы если не существуют
        final File dir = new File("config");
        if (!dir.exists()) {
            dir.mkdir();
        }

        final File file = new File("config/config.properties");
        if (!file.exists()) {
            try (InputStream in = Config.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");
            OutputStream out = new FileOutputStream("config/config.properties")) {
                int data;
                while ((data = in.read()) != -1) {
                    out.write(data);
                }
                System.out.println("Config file is created!");
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    /* Получение компонента из конфига */
    public static String get(String nameStr) {
        String out = "CONFIG_ERROR";
        FileInputStream fileInputStream;
        Properties prop = new Properties();
        try {
            fileInputStream = new FileInputStream("config/config.properties");
            prop.load(fileInputStream);
            out = prop.getProperty(nameStr);
            out = new String(out.getBytes("ISO-8859-1"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static int getInt(String nameStr) {
        String result = get(nameStr);
        if(result.equals("CONFIG_ERROR")) {
            return -1;
        }
        return Integer.parseInt(get(nameStr));
    }
}
