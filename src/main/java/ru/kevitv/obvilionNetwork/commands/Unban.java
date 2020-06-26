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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.Database;
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import java.awt.*;

public class Unban extends Command {

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public void main(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {

        /* No pex to unban */
        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("unban.title", guildInfo.lang))
                    .setDescription(Lang.get("nopex", guildInfo.lang, Lang.get("pex.ban", guildInfo.lang)))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        String id = "";
        for (String arg : args) {
            if(arg.matches("[-+]?\\d+"))
                    id = arg;
        }

        Member member = null;
        if(event.getMessage().getMentionedMembers().size() > 1) {
            member = event.getMessage().getMentionedMembers().get(0);
        }

        /* Reason */
        String reason = "";
        for(int i = 1; i < args.length; i++) {
            reason += args[i] + " ";
        }
        if(reason == "") {
            reason = Lang.get("reason.none", guildInfo.lang);
        } else {
            reason = reason.replace(id, "");
        }

        for(Guild.Ban ban : event.getGuild().retrieveBanList().complete()) {
            if (ban.getUser().getId().equals(id)) {
                event.getGuild().unban(id).queue();
                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle(Lang.get("unban.title", guildInfo.lang))
                        .setDescription(Lang.get("unban.ok", guildInfo.lang))

                        .addField(Lang.get("unban.userLine", guildInfo.lang), ban.getUser().getName(), false)
                        .addField(Lang.get("unban.reason", guildInfo.lang), reason, false)

                        .setColor(new Color(19, 167, 246))
                        .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
                event.getChannel().sendMessage(eb.build()).queue();

                Database.createUpdateRequest("DELETE FROM bans WHERE (id='"+ id +"' AND guildId='"+ event.getGuild().getId() +"');");
                return;
            }
        }

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(Lang.get("unban.title", guildInfo.lang))
                .setDescription(Lang.get("unban.err", guildInfo.lang, guildInfo.prefix))
                .setColor(new Color(19, 167, 246))
                .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
