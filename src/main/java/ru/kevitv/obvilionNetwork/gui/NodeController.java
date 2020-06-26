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

package ru.kevitv.obvilionNetwork.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.dv8tion.jda.api.entities.Guild;
import ru.kevitv.obvilionNetwork.bot.Bot;

import java.util.Objects;

public class NodeController {

    @FXML public Label date;
    @FXML public Label members;
    @FXML public Label serverName;
    @FXML public Label link;

    public void initialize() { update(); }

    //[TODO] Срочно переделать
    public void update() {
        Guild guild = NodesGuilds.get();

        // Дата добавления бота на сервер
        date.setText(String.valueOf(Objects.requireNonNull(guild.getMember(Bot.bot.getSelfUser())).getTimeJoined()).substring(0,
                String.valueOf(Objects.requireNonNull(guild.getMember(Bot.bot.getSelfUser())).getTimeJoined()).length() - 14));

        // Обработка кол-ва участников
        String memb = String.valueOf(guild.getMembers().size());
        while (memb.length() < 4) {
            memb = memb + " ";
        }
        members.setText(memb);

        // Обработка имени сервера
        String name = guild.getName();
        while (name.length() < 14) {
            name = name + " ";
        }
        while (name.length() > 14) {
            name = name.substring(0, name.length() - 1);
        }
        serverName.setText(name);

        // Ссылка на приглашение на сервер (если есть)
        if (guild.retrieveInvites().complete().size() > 0) {
            link.setText(guild.retrieveInvites().complete().get(0).getUrl().replace("https://discord.gg/", ""));
        } else {
            link.setText("none   ");
        }
    }
}
