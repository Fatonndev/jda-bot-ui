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
import ru.kevitv.obvilionNetwork.bot.Main;

public class BanTimer {
    Thread th;

    public BanTimer(long timeSeconds, String guildId, String userId) {
        th = new Thread(() -> {
            try {
                Thread.sleep(timeSeconds * 1000);
                Main.bot.getGuildById(guildId).unban(userId).queue();
                Database.createUpdateRequest("DELETE FROM bans WHERE (id='"+ userId +"' AND guildId='"+ guildId +"');");
            } catch (InterruptedException ignored) {

            }
        });
        th.start();
    }
}
