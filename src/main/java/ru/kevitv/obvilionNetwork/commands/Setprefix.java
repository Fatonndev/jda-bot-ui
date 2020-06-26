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

public class Setprefix extends Command {
    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("setprefix.title", guildInfo.lang))
                    .setDescription(Lang.get("nopex", guildInfo.lang, Lang.get("pex.administrator", guildInfo.lang)))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        String ansPrefix = guildInfo.prefix;
        String prefix = "null";
        
        if(args.length > 1) prefix = args[1];
        if(prefix.split("").length > 1 || prefix.equals("@") || prefix.equals(ansPrefix)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("setprefix.title", guildInfo.lang))
                    .setDescription(Lang.get("setprefix.err", guildInfo.lang, ansPrefix))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        Database.createUpdateRequest("UPDATE guilds SET prefix='" + prefix + "' WHERE id='" + guildInfo.id + "';");

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(Lang.get("setprefix.title", guildInfo.lang))
                .setDescription(Lang.get("setprefix.successfull", guildInfo.lang, prefix))
                .addField(Lang.get("setprefix.line", guildInfo.lang), ansPrefix, false)
                .addField(Lang.get("setprefix.newline", guildInfo.lang), prefix, false)
                .setColor(new Color(19, 167, 246))
                .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

        event.getChannel().sendMessage(eb.build()).queue();
    }
}
