package ru.kevitv.obvilionNetwork.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.music.GuildMusicManager;
import ru.kevitv.obvilionNetwork.music.PlayerManager;
import ru.kevitv.obvilionNetwork.music.TrackScheduler;
import ru.kevitv.obvilionNetwork.utils.Lang;

public class Skip extends Command {

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        MessageChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;

        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage(Lang.get("join.noContains", guildInfo.lang)).queue();
            return;
        }

        if (player.getPlayingTrack() == null) {
            channel.sendMessage(Lang.get("skip.no", guildInfo.lang)).queue();
            return;
        }

        scheduler.nextTrack();
        channel.sendMessage(Lang.get("skip.ok", guildInfo.lang)).queue();
    }
}
