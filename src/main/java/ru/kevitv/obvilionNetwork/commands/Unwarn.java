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

import java.awt.*;

public class Unwarn extends Command {

    @Override
    public String getName() {
        return "unwarn";
    }

    @Override
    public void main(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {

        if (!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("unwarn.title", guildInfo.lang))
                    .setDescription(Lang.get("nopex", guildInfo.lang, Lang.get("pex.kick", guildInfo.lang)))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        if(event.getMessage().getMentionedMembers().size() != 1) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("unwarn.title", guildInfo.lang))
                    .setDescription(Lang.get("unwarn.err", guildInfo.lang, guildInfo.prefix))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        Member member = event.getMessage().getMentionedMembers().get(0);

        if(member.hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("unwarn.title", guildInfo.lang))
                    .setDescription(Lang.get("unwarn.nopex", guildInfo.lang, member.getNickname(), Lang.get("pex.kick", guildInfo.lang)))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        int warns = Database.createQueryRequestInt("SELECT warns FROM users WHERE (id='"+member.getId()+"' AND guildId='" + member.getGuild().getId() + "');");

        if(warns>0) {
            warns--;
        } else {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("unwarn.title", guildInfo.lang))
                    .setDescription(Lang.get("unwarn.few", guildInfo.lang, member.getNickname()))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        String reason = "";
        for (int i = 0; i < args.length; i++) {
            if(i > 1) {
                reason += args[i] + " ";
            }
        }

        if (reason == "")
            reason = Lang.get("reason.none", guildInfo.lang);

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(Lang.get("unwarn.title", guildInfo.lang))
                .addField(Lang.get("unwarn.removed", guildInfo.lang), member.getNickname(), false)
                .addField(Lang.get("warn.list", guildInfo.lang), Lang.get("warn.list1", guildInfo.lang, warns+"", guildInfo.maxwarns+""), false)
                .addField(Lang.get("warn.reason", guildInfo.lang), reason, false)
                .setColor(new Color(19, 167, 246))
                .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

        event.getChannel().sendMessage(eb.build()).queue();

        Database.createUpdateRequest("UPDATE users SET warns=" + warns + " WHERE (guildId='" + guildInfo.id + "' AND id='" + member.getId() + "');");
    }
}
