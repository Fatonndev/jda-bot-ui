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

package ru.kevitv.obvilionNetwork.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.Database;

public class GuildInfo {

    public String prefix;
    public String lang;
    public String id;
    public int maxwarns;

    public GuildInfo(MessageReceivedEvent event) {
        // Добавляем сервер в базу данных если не существует
        Database.createUpdateRequest(
                "INSERT INTO guilds (id, premium, prefix, lang, maxwarns) " +
                        "SELECT '" + event.getGuild().getId() + "','false', '!', 'en_US', 5 FROM DUAL " +
                        "WHERE NOT EXISTS " +
                        "(SELECT id FROM guilds WHERE id='" + event.getGuild().getId() + "');");

        // Добавляем участника в базу данных если не существует
        Database.createUpdateRequest(
                "INSERT INTO users (id, xp, guildId, money, warns) " +
                        "SELECT '" + event.getAuthor().getId() + "', 0, '"+event.getGuild().getId()+"', 0, 0 FROM DUAL " +
                        "WHERE NOT EXISTS " +
                        "(SELECT id, guildId FROM users WHERE (id='" + event.getAuthor().getId() + "' AND guildId='" + event.getGuild().getId() + "'));");

        prefix = Database.createQueryRequest("SELECT prefix FROM guilds WHERE id="+event.getGuild().getId());
        lang = Database.createQueryRequest("SELECT lang FROM guilds WHERE id="+event.getGuild().getId());
        id = event.getGuild().getId();
        maxwarns = Database.createQueryRequestInt("SELECT maxwarns FROM guilds WHERE id="+event.getGuild().getId());
    }
}
