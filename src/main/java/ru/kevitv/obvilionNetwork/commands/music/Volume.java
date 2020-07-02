package ru.kevitv.obvilionNetwork.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.music.PlayerManager;
import ru.kevitv.obvilionNetwork.utils.Lang;

import java.awt.*;

public class Volume extends Command {
    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        MessageChannel channel = event.getChannel();
        PlayerManager manager = PlayerManager.getInstance();
        AudioPlayer player = manager.getGuildMusicManager(event.getGuild()).player;

        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage(Lang.get("join.noContains", guildInfo.lang)).queue();
            return;
        }

        int volume;

        try {
            volume = Integer.parseInt(args[1]);
        } catch (NumberFormatException | NullPointerException e) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("volume.title", guildInfo.lang))
                    .setDescription(Lang.get("volume.noNumber", guildInfo.lang, guildInfo.prefix))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            channel.sendMessage(eb.build()).queue();
            return;
        }

        if(volume > 200 || volume < 0) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(Lang.get("volume.title", guildInfo.lang))
                    .setDescription(Lang.get("volume.big", guildInfo.lang))
                    .setColor(new Color(19, 167, 246))
                    .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());
            channel.sendMessage(eb.build()).queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(Lang.get("volume.title", guildInfo.lang))
                .setDescription(Lang.get("volume.ok", guildInfo.lang))

                .addField(Lang.get("volume.before", guildInfo.lang), player.getVolume()+"", true)
                .addField(Lang.get("volume.after", guildInfo.lang), volume+"", true)

                .setColor(new Color(19, 167, 246))
                .setFooter(Lang.get("commandRequested", guildInfo.lang, event.getAuthor().getName()), event.getAuthor().getAvatarUrl());

        player.setVolume(volume);

        channel.sendMessage(eb.build()).queue();
    }
}
