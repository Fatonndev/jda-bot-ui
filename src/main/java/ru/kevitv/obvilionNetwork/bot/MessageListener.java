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

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        if (event.isFromType(ChannelType.PRIVATE)) {
            // Private commands here.
        } else {
            GuildInfo guildInfo = new GuildInfo(event);

            String message = event.getMessage().getContentStripped();

            if(!message.startsWith(guildInfo.prefix)) {
                if(event.getMessage().getMentionedMembers().size() == 0) return;
                Member member = event.getMessage().getMentionedMembers().size() == 1 ? event.getMessage().getMentionedMembers().get(0) : null;
                if(!event.getMessage().getMentionedMembers().get(0).getId().equals(Bot.bot.getSelfUser().getId())) {
                    return;
                } else {
                    message = message.replace("@" + Bot.bot.getSelfUser().getName(), "").replaceAll(" ", "");
                }
            } else {
                message = message.replace(guildInfo.prefix, "");
            }

            String[] args = event.getMessage().getContentRaw()
                    .split(" ");

            for (Command command : Bot.commands) {
                if(message.startsWith(command.getName() + " ") || message.equals(command.getName())) {
                    command.run(event, guildInfo, args);
                }
            }
        }
    }
}
