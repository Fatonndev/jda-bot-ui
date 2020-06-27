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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.Database;
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.utils.BanTimer;

import java.awt.*;

public class Ban extends Command {

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {

        /* No pex to ban */
        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("ban.title", guildInfo.lang))
                    .setDescription(Lang.get("nopex", guildInfo.lang, Lang.get("pex.ban", guildInfo.lang)))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        /* Not enough arguments */
        if (event.getMessage().getMentionedMembers().size() != 1) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("ban.title", guildInfo.lang))
                    .setDescription(Lang.get("ban.err", guildInfo.lang, guildInfo.prefix))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        Member member = event.getMessage().getMentionedMembers().get(0);

        /* No pex */
        if (member.hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("ban.title", guildInfo.lang))
                    .setDescription(Lang.get("ban.nopex", guildInfo.lang, member.getEffectiveName(), Lang.get("pex.kick", guildInfo.lang)))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        int m = 0;
        int h = 0;
        int d = 0;

        for (String[] lang : Langs.langs) {
            for (int i = 0; i < args.length; i++) {
                if (m == 0 && args[i].contains(Lang.get("time.m", lang[0]).replace(".", ""))) {
                    args[i] = args[i].replace(Lang.get("time.m", guildInfo.lang).replace(".", ""), "");
                    if (args[i].matches("[-+]?\\d+")) {
                        m = Integer.parseInt(args[i]);
                        args[i] = "";
                    }
                }

                if (h == 0 && args[i].contains(Lang.get("time.h", lang[0]).replace(".", ""))) {
                    args[i] = args[i].replace(Lang.get("time.h", guildInfo.lang).replace(".", ""), "");
                    if (args[i].matches("[-+]?\\d+")) {
                        h = Integer.parseInt(args[i]);
                        args[i] = "";
                    }
                }

                if (d == 0 && args[i].contains(Lang.get("time.d", lang[0]).replace(".", ""))) {
                    args[i] = args[i].replace(Lang.get("time.d", guildInfo.lang).replace(".", ""), "");
                    if (args[i].matches("[-+]?\\d+")) {
                        d = Integer.parseInt(args[i]);
                        args[i] = "";
                    }
                }
            }
        }

        /* Reason */
        String reason = "";
        for(int i = 1; i < args.length; i++) {
            reason += args[i] + " ";
        }
        reason = reason.replace("<@"+member.getId()+"> ", "");

        long seconds = m*60 + h*60*60 + d*24*60*60;
        long out = (System.currentTimeMillis() / 1000) + seconds;

        Database.createUpdateRequest("INSERT INTO `bans` (`id`, `guildId`, `timeEnd`) VALUES ('" + member.getId() + "', '" + guildInfo.id + "', '" + out + "');");

        /* Permanent ban */
        if(seconds == 0) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("ban.title", guildInfo.lang))
                    .setDescription(Lang.get("ban.ok", guildInfo.lang))

                    .addField(Lang.get("ban.userLine", guildInfo.lang), member.getEffectiveName(), false)
                    .addField(Lang.get("ban.timeLine", guildInfo.lang), Lang.get("ban.forever", guildInfo.lang), false)
                    .addField(Lang.get("ban.reason", guildInfo.lang), reason, false)

                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            member.ban(1, reason).queue();
            return;
        }

        new BanTimer(seconds, guildInfo.id, member.getId());
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(Lang.get("ban.title", guildInfo.lang))
                .setDescription(Lang.get("ban.ok", guildInfo.lang))

                .addField(Lang.get("ban.userLine", guildInfo.lang), member.getEffectiveName(), false)
                .addField(Lang.get("ban.timeLine", guildInfo.lang), Lang.get("ban.time", guildInfo.lang, d+"", h+"", m+""), false)
                .addField(Lang.get("ban.reason", guildInfo.lang), reason, false)

                .setColor(new Color(19, 167, 246))
                .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(eb.build()).queue();
        member.ban(1, reason).queue();
    }
}
