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
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;

import java.awt.*;

public class Avatar extends Command {

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {

        User user =
                event.getMessage().getMentionedMembers().size() == 0 ?
                event.getAuthor() :
                event.getMessage().getMentionedMembers().get(0).getUser();

        if (user.equals(event.getAuthor())) {
            if(args.length > 1)
                if(args[1].matches("[-+]?\\d+"))
                user =  event.getGuild().getMemberById(args[1]) == null ?
                        event.getAuthor() :
                        event.getGuild().getMemberById(args[1]).getUser();
        }

        if(user.getAvatarUrl() == null) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("avatar.title", guildInfo.lang, user.getName()))
                    .setColor(new Color(19, 167, 246))
                    .setImage(user.getDefaultAvatarUrl() + "?size=512")
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(eb.build()).queue();
        } else {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("avatar.title", guildInfo.lang, user.getName()))
                    .setColor(new Color(19, 167, 246))
                    .setImage(user.getAvatarUrl() + "?size=512")
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(eb.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "avatar";
    }
}
