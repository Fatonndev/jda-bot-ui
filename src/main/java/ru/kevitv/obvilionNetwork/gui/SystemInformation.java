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

package ru.kevitv.obvilionNetwork.gui;

import com.sun.management.OperatingSystemMXBean;
import ru.kevitv.obvilionNetwork.utils.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public class SystemInformation {

    public enum os {
        LINUX, WIN, OSX
    }

    public static OperatingSystemMXBean osMBean
            = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static int cpu() {
        return (int) Math.round(osMBean.getSystemCpuLoad()*100);
    }
    public static int cpuProcess() {
        return (int) Math.round(osMBean.getProcessCpuLoad()*100);
    }
    public static int ram() {
        return (int) ( (float) osMBean.getFreePhysicalMemorySize() / osMBean.getTotalPhysicalMemorySize()*100);
    }
    public static int hdd() {

        long freeMemory = 0;
        long allMemory = 0;

        File[] roots = File.listRoots();
        for (File file: roots) {
            freeMemory+=file.getFreeSpace();
            allMemory+=file.getTotalSpace();
        }
        return (int) ((float) freeMemory / allMemory * 100);
    }
    public static int network() {
        return (int) (Config.ping / 20);
    }

    public static os getOS() {
        String OS = System.getProperty("os.name").toUpperCase();
        if (OS.contains("WIN")) {
            return os.WIN;
        }
        else if (OS.contains("OS X")) {
            return os.OSX;
        }
        else {
            return os.LINUX;
        }
    }

    public static String getModelName() {
        try {
            String lineRead;
            String returnLine = "";
            BufferedReader br;
            boolean lineFound = false;

            switch (getOS()) {
                case WIN:
                    br = runCmdAndGetReader("wmic cpu get name");

                    // actually try to read
                    while ((lineRead = br.readLine()) != null) {
                        lineRead = lineRead.trim(); // strings are somehow shit dirty
                        // only keep the longest readLine, this is a rule of thumb
                        if (!lineRead.startsWith("Name") && lineRead.length() > returnLine.length()) {
                            returnLine = lineRead;
                        }
                    }

                    br.close();
                    return returnLine;
                case LINUX:
                    br = runCmdAndGetReader("cat /proc/cpuinfo");

                    // actually try to read
                    while ((lineRead = br.readLine()) != null) {
                        // filter line "model name    : ........"
                        if (lineRead.startsWith("model name")) {
                            int dropCnt = 0;
                            while (lineRead.charAt(dropCnt) != ':') {
                                dropCnt += 1;
                                //lineRead = lineRead.substring(1);
                            } // slowly drop a char from the beginning until we see ':'

                            lineRead = lineRead.substring(dropCnt + 2); // drop ': '
                            lineFound = true;
                            break;
                        }
                    }

                    br.close();

                    if (!lineFound) return null;

                    return lineRead;
                case OSX:
                    br = runCmdAndGetReader("sysctl -a");

                    // actually try to read
                    while ((lineRead = br.readLine()) != null) {
                        // filter line "model name    : ........"
                        if (lineRead.startsWith("machdep.cpu.brand_string")) {
                            int dropCnt = 0;
                            while (lineRead.charAt(dropCnt) != ':') {
                                dropCnt += 1;
                            } // slowly drop a char from the beginning until we see ':'

                            lineRead = lineRead.substring(dropCnt + 2); // drop ': '
                            lineFound = true;
                            break;
                        }
                    }

                    br.close();

                    if (!lineFound) return null;

                    return lineRead;
            }
        } catch (IOException ignored) {

        }

        return null;
    }
    public static String getRamSpeed() {
        try {
            String lineRead;
            String returnLine = "";
            BufferedReader br;
            boolean lineFound = false;

            switch (getOS()) {
                case WIN:
                    br = runCmdAndGetReader("wmic memorychip get speed");

                    // actually try to read
                    while ((lineRead = br.readLine()) != null) {
                        lineRead = lineRead.trim(); // strings are somehow shit dirty
                        // only keep the longest readLine, this is a rule of thumb
                        if (!lineRead.startsWith("Speed") && lineRead.length() > returnLine.length()) {
                            returnLine = lineRead;
                        }
                    }

                    br.close();
                    return returnLine;
                case LINUX:
                    br = runCmdAndGetReader("sudo lshw -short -C memory");

                    while ((lineRead = br.readLine()) != null) {
                        if (lineRead.startsWith("model name")) {
                            int dropCnt = 0;
                            while (lineRead.charAt(dropCnt) != ':') {
                                dropCnt += 1;
                            }

                            lineRead = lineRead.substring(dropCnt + 2);
                            lineFound = true;
                            break;
                        }
                    }

                    br.close();

                    if (!lineFound) return null;

                    return lineRead;
                case OSX:
                    return "OSX not supported";
            }
        } catch (IOException ignored) {

        }

        return null;
    }

    private static BufferedReader runCmdAndGetReader(String cmd) throws IOException {
        Process p = Runtime.getRuntime().exec(cmd);
        InputStreamReader ir = new InputStreamReader(p.getInputStream());
        BufferedReader br = new BufferedReader(ir);

        return br;
    }

}
