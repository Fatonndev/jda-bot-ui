package ru.kevitv.obvilionNetwork.commands.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.music.PlayerManager;
import ru.kevitv.obvilionNetwork.utils.Config;
import ru.kevitv.obvilionNetwork.utils.Lang;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Play extends Command {

    private final YouTube youTube;

    public Play() {
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Obvilion Bot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {

        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        if (!memberVoiceState.inVoiceChannel()) {
            event.getChannel().sendMessage(Lang.get("join.noContains", guildInfo.lang)).queue();
            return;
        }

        MessageChannel channel = event.getChannel();
        if (args.length < 2) {
            channel.sendMessage(Lang.get("play.fewArguments", guildInfo.lang, guildInfo.prefix)).queue();
            return;
        }

        String input = String.join(" ", args).replace(guildInfo.prefix+"play ", "");

        PlayerManager manager = PlayerManager.getInstance();
        if(!isURL(input)) {
            List<SearchResult> links = searchYoutube(input);

            if(links == null) {
                channel.sendMessage("no results").queue();
                return;
            }


            /*EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("play.title", guildInfo.lang))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

            for (int i = 0; i < links.size(); i++) {
                eb.appendDescription(Lang.get("play.result", guildInfo.lang, (i+1)+"", links.get(i).getSnippet().getTitle()));
            }

            channel.sendMessage(eb.build()).queue();*/


            manager.loadAndPlay(event, "https://www.youtube.com/watch?v="+links.get(0).getId().getVideoId(), guildInfo);
        } else {
            manager.loadAndPlay(event, input, guildInfo);
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();
        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(voiceChannel);
        }
    }

    private List<SearchResult> searchYoutube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Math.random() < 0.5 ? Config.get("yt_api_key_1") : Config.get("yt_api_key_2"))
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                return results;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isURL(String link) {
        try {
            new URL(link);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}