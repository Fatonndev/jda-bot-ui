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

import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import ru.kevitv.obvilionNetwork.utils.Config;
import ru.kevitv.obvilionNetwork.bot.Bot;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class NodesUpdate {

    public static Thread th, th2;
    public static XYChart.Series series = new XYChart.Series();
    public static XYChart.Series ramSeries = new XYChart.Series();

    public static void initialize() {
        th = new Thread(() -> {
            try {
                while (true) {
                    NodesGuilds.initialize();

                    TimeUnit.SECONDS.sleep(Config.UpdateTime);

                    Platform.runLater(() -> {
                        Config.controller.update();
                    });
                }
            } catch (InterruptedException ignored) {

            }
        });
        th.start();

        th2 = new Thread(() -> {
            try {

                while (true) {
                    Config.ping = Bot.bot.getGatewayPing();

                    TimeUnit.SECONDS.sleep(Config.FastUpdateTime);

                    Platform.runLater(() -> {

                        try {
                            Config.controller.updConsole();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Controller c = Config.controller;

                        c.ping.setText("Ping: " + Config.ping + " ms");

                        c.arc1.setLength(180 - SystemInformation.cpu() * 1.4);
                        c.percentage1.setText(SystemInformation.cpu() + "%");

                        c.arc2.setLength(180 - (100 - SystemInformation.ram()) * 1.6);
                        c.percentage2.setText(100 - SystemInformation.ram() + "%");

                        c.arc3.setLength(180 - SystemInformation.hdd() * .3);
                        c.percentage3.setText(100 - SystemInformation.hdd() + "%");

                        c.arc4.setLength(180 - SystemInformation.network() * 1.3);
                        c.percentage4.setText(SystemInformation.network() + "%");

                        c.cpuName.setText("CPU: "+SystemInformation.getModelName());
                        c.ramName.setText("RAM: "+SystemInformation.osMBean.getTotalPhysicalMemorySize() / 1024 / 1024 / 1024 + " GB, "+SystemInformation.getRamSpeed()+" MHz");

                        if(series.getData().size() > 15) {
                            series.getData().remove(0);
                        }
                        series.getData().add(new XYChart.Data(LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond(), SystemInformation.cpu()));

                        if (Config.controller.cpuChart.getData().size() > 0) {
                            c.cpuChart.getData().set(0, series);
                        } else {
                            c.cpuChart.getData().addAll(series);
                        }

                        if(ramSeries.getData().size() > 15) {
                            ramSeries.getData().remove(0);
                        }

                        ramSeries.getData().add(new XYChart.Data(LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond(), 100-SystemInformation.ram()));
                        if (Config.controller.ramChart.getData().size() > 0) {
                            c.ramChart.getData().set(0, ramSeries);
                        } else {
                            c.ramChart.getData().addAll(ramSeries);
                        }

                    });
                }
            } catch (InterruptedException ignored) {}

        });
        th2.start();
    }


}
