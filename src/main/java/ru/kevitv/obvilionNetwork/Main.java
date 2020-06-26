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

package ru.kevitv.obvilionNetwork;

import ru.kevitv.obvilionNetwork.bot.Bot;
import ru.kevitv.obvilionNetwork.gui.ConsoleOutput;
import ru.kevitv.obvilionNetwork.gui.GUI;
import ru.kevitv.obvilionNetwork.gui.NodesUpdate;
import ru.kevitv.obvilionNetwork.utils.Config;
import ru.kevitv.obvilionNetwork.utils.Lang;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Object obj = new Object();
    private static Thread th;

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length == 0 || !args[0].equals("nogui")) {
            ConsoleOutput.setOutput();
        } else {
            Config.nogui = true;
        }

        Config.init();
        Lang.init();
        Bot.main();

        Database.createUpdateRequest("CREATE TABLE IF NOT EXISTS guilds (id VARCHAR(20), premium VARCHAR(20), prefix VARCHAR(20), lang VARCHAR(20), maxwarns INT)");
        Database.createUpdateRequest("CREATE TABLE IF NOT EXISTS users (id VARCHAR(20), guildId VARCHAR(20), xp INT, money INT, warns INT)");
        Database.createUpdateRequest("CREATE TABLE IF NOT EXISTS bans (id VARCHAR(20), guildId VARCHAR(20), timeEnd VARCHAR(30))");

        th = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);

                    if (args.length == 0 || !args[0].equals("nogui")) {
                        NodesUpdate.initialize();
                        GUI.main(args);
                    }
                    th.interrupt();
                } catch (InterruptedException ignored) {
                }
            }
        );
        th.start();
    }
}
