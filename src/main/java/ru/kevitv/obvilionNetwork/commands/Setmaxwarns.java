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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.Database;
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;

import java.awt.*;

public class Setmaxwarns extends Command {

    @Override
    public String getName() {
        return "setmaxwarns";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("setmaxwarns.title", guildInfo.lang))
                    .setDescription(Lang.get("nopex", guildInfo.lang, Lang.get("pex.administrator", guildInfo.lang)))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        if(args.length < 2 || !args[1].matches("[-+]?\\d+")) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("setmaxwarns.title", guildInfo.lang))
                    .setDescription(Lang.get("setmaxwarns.err", guildInfo.lang, guildInfo.prefix))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        int maxwans = Integer.parseInt(args[1]);
        Database.createUpdateRequest("UPDATE guilds SET maxwarns=" + maxwans + " WHERE id='" + guildInfo.id + "';");

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(Lang.get("setmaxwarns.title", guildInfo.lang))
                .setDescription(Lang.get("setmaxwarns.ok", guildInfo.lang, ""+maxwans))
                .setColor(new Color(19, 167, 246))
                .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

        event.getChannel().sendMessage(eb.build()).queue();

    }
}
