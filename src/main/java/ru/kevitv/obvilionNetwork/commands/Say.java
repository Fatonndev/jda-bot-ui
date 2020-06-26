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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;

public class Say extends Command {

    @Override
    public void main(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        event.getChannel().sendMessage(
                Lang.get("say", guildInfo.lang, event.getAuthor().getName()) +
                event.getMessage().getContentRaw()
                        .replace(guildInfo.prefix+"say", "")
                        .replaceAll("@everyone", "&everyone")
                        .replaceAll("@here", "&here")
        ).queue();
    }

    @Override
    public String getName() {
        return "say";
    }
}