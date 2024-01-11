package com.jeff_media.discordlogger

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.requests.GatewayIntent
import org.slf4j.LoggerFactory

class DiscordLogger(val config: Config) : EventListener {

    private val logger = LoggerFactory.getLogger(DiscordLogger::class.java)

    enum class MessageAction {
        SENT, EDITED, DELETED;

        fun getPastTense(): String {
            return when (this) {
                SENT -> "sent"
                EDITED -> "edited"
                DELETED -> "deleted"
            }
        }

        fun getParantheses(): String {
            return when (this) {
                SENT -> ""
                EDITED -> " (edited)"
                DELETED -> " (deleted)"
            }
        }
    }

    private val jda: JDA = JDABuilder
        .createDefault(config.botToken)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
        .build()

    init {
        jda.awaitReady()
        jda.addEventListener(this)
    }

    override fun onEvent(event: GenericEvent) {
        if (event is MessageReceivedEvent) {
            logMessage(event.message)
        } else if (event is MessageUpdateEvent) {
            logMessage(event.message, MessageAction.EDITED)
        }
    }

    fun logMessage(message: Message, action: MessageAction = MessageAction.SENT) {
        val author = message.author
        val channel = message.channel

        if (message.isWebhookMessage) return
        if (message.isEphemeral) return
        if (author.isBot) return
        if (author.isSystem) return
        if (channel.id == config.channelId) return

        val embed = EmbedBuilder()
            .setTitle(
                "${author.name} ${message.jumpUrl}${action.getParantheses()} @ ${
                    discordTimestamp(
                        message.timeCreated,
                        "R"
                    )
                }"
            )
            //.setImage(author.avatarUrl)
            .setThumbnail(author.avatarUrl)
            .addField("Message ${action.getPastTense()}", message.contentStripped.shorten(1000), false)

        var attachmentId = 1
        for (file in message.attachments) {
            embed.addField("Attachment ${attachmentId++}", file.url, false)
        }

        embed
            .addBlankField(false)
            .addField("Author Mention", author.asMention, true)
            .addField("Author Name", author.name, true)
            .addField("Author ID", author.id, true)
            .addField("Time", discordTimestamp(message.timeCreated), true)
            .addField("Channel Mention", channel.asMention, true)
            .addField("Message URL", message.jumpUrl, true)
            .addField("Message ID", message.id, true)



        jda.getTextChannelById(config.channelId)?.sendMessageEmbeds(embed.build())?.queue()

        logger.info("${author.name} # ${channel.name}${action.getParantheses()}: ${message.contentStripped.shorten(30)}")

    }

}