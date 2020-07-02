package ru.kevitv.obvilionNetwork.commands.music;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.music.GuildMusicManager;
import ru.kevitv.obvilionNetwork.music.PlayerManager;
import ru.kevitv.obvilionNetwork.utils.Lang;

public class Stop extends Command {
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);

        event.getChannel().sendMessage(Lang.get("stop.ok", guildInfo.lang)).queue();
    }
}
