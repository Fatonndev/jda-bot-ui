package ru.kevitv.obvilionNetwork.commands.music;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import ru.kevitv.obvilionNetwork.bot.Command;
import ru.kevitv.obvilionNetwork.bot.GuildInfo;
import ru.kevitv.obvilionNetwork.utils.Lang;

public class Leave extends Command {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public void run(MessageReceivedEvent event, GuildInfo guildInfo, String[] args) {
        MessageChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            channel.sendMessage(Lang.get("leave.noConnected", guildInfo.lang)).queue();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(event.getMember())) {
            channel.sendMessage(Lang.get("leave.noContains", guildInfo.lang)).queue();
            return;
        }

        audioManager.closeAudioConnection();
        channel.sendMessage(Lang.get("leave.ok", guildInfo.lang)).queue();
    }
}
