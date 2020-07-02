package ru.kevitv.obvilionNetwork.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.utils.Lang;
import ru.kevitv.obvilionNetwork.utils.TimeUtil;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(MessageReceivedEvent event, String trackUrl, GuildInfo gi) {
        GuildMusicManager musicManager = getGuildMusicManager(event.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle(track.getInfo().title, trackUrl)
                        .setColor(new Color(19, 167, 246))
                        .setDescription(Lang.get("play.added", gi.lang))

                        .addField(Lang.get("play.author", gi.lang), track.getInfo().author+"", true)
                        .addField(Lang.get("play.duration", gi.lang), TimeUtil.msToTime(track.getDuration()), true)
                        .addField(Lang.get("play.position", gi.lang), "" + (musicManager.scheduler.getQueue().size() + 1), true)

                        .setFooter(Lang.get("commandRequested", gi.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

                event.getChannel().sendMessage(eb.build()).queue();

                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle(Lang.get("play.title", gi.lang))
                        .setColor(new Color(19, 167, 246))
                        .setFooter(Lang.get("commandRequested", gi.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

                int trackCount = Math.min(playlist.getTracks().size(), 20);
                List<AudioTrack> tracks = new ArrayList<>(playlist.getTracks());

                for (int i = 0; i < trackCount; i++) {
                    AudioTrack track = tracks.get(i);
                    AudioTrackInfo info = track.getInfo();

                    eb.appendDescription(Lang.get("play.playlistTrack", gi.lang,
                            (i+1)+"", info.title, info.author, TimeUtil.msToTime(info.length)));

                    play(musicManager, track);
                }

                event.getChannel().sendMessage(eb.build()).queue();
            }

            @Override
            public void noMatches() {
                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle(Lang.get("play.title", gi.lang))
                        .setColor(new Color(19, 167, 246))
                        .setDescription(Lang.get("play.noMatches", gi.lang, trackUrl))
                        .setFooter(Lang.get("commandRequested", gi.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

                event.getChannel().sendMessage(eb.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle(Lang.get("play.title", gi.lang))
                        .setColor(new Color(19, 167, 246))
                        .setDescription(Lang.get("play.loadFailed", gi.lang, exception.getMessage()))
                        .setFooter(Lang.get("commandRequested", gi.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

                event.getChannel().sendMessage(eb.build()).queue();
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }


}

