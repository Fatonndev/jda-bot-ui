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

package ru.kevitv.obvilionNetwork.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.utils.Config;
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.bot.Bot;

import java.awt.*;

public class Ping extends Command {

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        long time = System.currentTimeMillis();

        event.getChannel().sendMessage("Pong!")
            .queue(response -> {
                long ping = System.currentTimeMillis() - time;

                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle(Lang.get("ping.title", guildInfo.lang), null);
                eb.setColor(new Color(19, 167, 246));
                eb.setThumbnail(Bot.bot.getSelfUser().getAvatarUrl());
                eb.addField(Lang.get("ping.api", guildInfo.lang), ping+" "+Lang.get("time.ms", guildInfo.lang)+" \uD83D\uDCE8", false);
                eb.addField(Lang.get("ping.jda", guildInfo.lang), Config.ping+" "+Lang.get("time.ms", guildInfo.lang)+" \uD83D\uDC99", false);
                eb.addField(Lang.get("ping.db", guildInfo.lang), Config.dbPing+" "+Lang.get("time.ms", guildInfo.lang)+" \uD83D\uDDC3", false);
                eb.setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

                response.editMessage(eb.build()).queue();
            });
    }

    @Override
    public String getName() {
        return "ping";
    }
}
