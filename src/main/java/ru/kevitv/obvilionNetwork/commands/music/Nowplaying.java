package ru.kevitv.obvilionNetwork.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.music.GuildMusicManager;
import ru.kevitv.obvilionNetwork.music.PlayerManager;
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.utils.TimeUtil;

import java.awt.*;
import java.util.ArrayList;

public class Nowplaying extends Command {

    @Override
    public String getName() {
        return "np";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        MessageChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            channel.sendMessage("The player is not playing any song.").queue();
            return;
        }

        AudioTrackInfo info = player.getPlayingTrack().getInfo();

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(info.title, info.uri)
                .setColor(new Color(19, 167, 246))

                .addField(Lang.get("play.duration", guildInfo.lang), TimeUtil.msToTime(player.getPlayingTrack().getDuration()), true)
                .addField(Lang.get("np.remained", guildInfo.lang), TimeUtil.msToTime(player.getPlayingTrack().getDuration() - player.getPlayingTrack().getPosition()), true)

                .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

        if(musicManager.scheduler.getQueue().size() > 0) {
            eb.addField(Lang.get("np.next", guildInfo.lang), new ArrayList<>(musicManager.scheduler.getQueue()).get(0).getInfo().title, true);
        }

        channel.sendMessage(eb.build()).queue();
    }
}
