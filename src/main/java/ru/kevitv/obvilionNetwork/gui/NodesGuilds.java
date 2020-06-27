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

import net.dv8tion.jda.api.entities.Guild;
import ru.kevitv.obvilionNetwork.bot.Bot;

import java.util.ArrayList;
import java.util.List;

public class NodesGuilds {

    public static List<Guild> guilds = new ArrayList<>();
    public static int number = -1;

    public static void initialize() {
        number = -1;
        guilds = new ArrayList<>();
        guilds = Bot.bot.getGuilds();
    }

    public static Guild get() {
        if(guilds.size() > number + 1) {
            number++;
        } else {
            number = 0;
        }
        return guilds.get(number);
    }
}
