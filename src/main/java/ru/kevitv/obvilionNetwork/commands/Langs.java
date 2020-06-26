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
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;

import java.awt.*;

public class Langs extends Command {
    public static String[][] langs = {
        { "en_US", "English" },
        { "ru_RU", "Russian" },
        { "be_BY", "Belarussian" },
        { "fr_FR", "French" },
        { "uk_UA", "Ukrainian" },
        { "fi_FI", "Finnish" }
    };

    @Override
    public void main(MessageReceivedEvent event, GuildInfo guildInfo, String[] args){

        EmbedBuilder eb = new EmbedBuilder()
            .setTitle(Lang.get("langlist.title", guildInfo.lang))
            .setDescription(Lang.get("langlist.description", guildInfo.lang))
            .setColor(new Color(19, 167, 246))
            .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

        for (int i = 0; i < Langs.langs.length; i++) {
            eb.addField(langs[i][1], langs[i][0], true);
        }

        event.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getName() {
        return "langs";
    }
}
