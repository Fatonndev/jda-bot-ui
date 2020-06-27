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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.*;
import ru.kevitv.obvilionNetwork.commands.*;
import ru.kevitv.obvilionNetwork.gui.ErrorWindow;
import ru.kevitv.obvilionNetwork.gui.GUI;
import ru.kevitv.obvilionNetwork.gui.NodesUpdate;
import ru.kevitv.obvilionNetwork.utils.Config;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class Bot {

    public static JDA bot;
    public static JDABuilder builder;
    public static List<Command> commands = new ArrayList<>();

    public static void main() {
        builder = new JDABuilder(Config.get("token"));
        setActivity();

        try {
            bot = builder.build();
        } catch (LoginException e) {
            if (!Config.nogui) {
                new ErrorWindow("This token is invalid, edit it in config.properties");
            }
            e.printStackTrace();
        }

        loadCommands();

        bot.addEventListener(new MessageListener());
    }

    public static void setActivity() {
        ActivityType a = ActivityType.valueOf(Config.get("activity"));
        builder.setActivity(Activity.of(a, Config.get("status")));
    }

    public static void loadCommands() {
        addCommands(
            new Avatar(), new Ban(), new Kick(), new Langs(), new Log(), new Ping(), new Say(), new Setlang(),
            new Setmaxwarns(), new Setprefix(), new Unban(), new Unwarn(), new Warn()
        );
    }

    public static void addCommands(Command... commands1) {
        for (Command command : commands1) {
            commands.add(command);
        }
    }
}
