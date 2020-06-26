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

import ru.kevitv.obvilionNetwork.Database;
import ru.kevitv.obvilionNetwork.bot.Bot;

import java.util.List;

public class LoadBans {
    public static void load() {
        List<List<String>> users = Database.createQueryRequestArray("SELECT * FROM bans;", 3);

        for (int i = 0; i < users.size(); i++) {
            List<String> userInfo = users.get(i);

            if(Long.parseLong(userInfo.get(3)) >= System.currentTimeMillis() / 1000) {
                Bot.bot.getGuildById(userInfo.get(1)).unban(userInfo.get(0));
            } else {
                long time = Long.parseLong(userInfo.get(3)) - (System.currentTimeMillis() / 1000);
                new BanTimer(time, userInfo.get(1), userInfo.get(0));
            }
        }
    }
}
