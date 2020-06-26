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

public class Setlang extends Command {

    @Override
    public String getName() {
        return "setlang";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("setlang.title", guildInfo.lang))
                    .setDescription(Lang.get("nopex", guildInfo.lang, Lang.get("pex.administrator", guildInfo.lang)))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        String ansLang = guildInfo.lang;
        String lang = "";

        if(args.length > 1)
            lang = args[1];

        for (int i = 0; i < Langs.langs.length; i++) {
            if(lang.equals(Langs.langs[i][0])) {
                Database.createUpdateRequest("UPDATE guilds SET lang='" + lang + "' WHERE id='" + guildInfo.id + "';");

                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle(Lang.get("setlang.title", lang))
                        .setDescription(Lang.get("setlang.successfull", lang, lang))
                        .addField(Lang.get("setlang.line", lang), guildInfo.lang, false)
                        .addField(Lang.get("setlang.newline", lang), lang, false)
                        .setColor(new Color(19, 167, 246))
                        .setFooter(Lang.get("commandRequested", lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

                event.getChannel().sendMessage(eb.build()).queue();
                return;
            }
        }

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(Lang.get("setlang.title", guildInfo.lang))
                .setDescription(Lang.get("setlang.err", guildInfo.lang, guildInfo.prefix, guildInfo.prefix))
                .setColor(new Color(19, 167, 246))
                .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

        event.getChannel().sendMessage(eb.build()).queue();
    }
}
