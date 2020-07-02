package ru.kevitv.obvilionNetwork.commands.music;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.music.PlayerManager;
import ru.kevitv.obvilionNetwork.utils.Lang;

public class Play extends Command {
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        MessageChannel channel = event.getChannel();
        if (args.length < 2) {
            channel.sendMessage(Lang.get("play.fewArguments", guildInfo.lang, guildInfo.prefix)).queue();
            return;
        }

        PlayerManager manager = PlayerManager.getInstance();
        manager.loadAndPlay(event, args[1], guildInfo);
    }
}