package com.jeff_media.discordlogger

import joptsimple.OptionParser

fun main(args: Array<String>) {
    println("Starting Discord Logger...")

    val parser: OptionParser = OptionParser()

    parser.accepts("b", "Bot token").withRequiredArg().required()
    parser.accepts("c", "Channel ID").withRequiredArg().required()

    val options = parser.parse(*args)

    val botToken = options.valueOf("b") as String
    val channelId = options.valueOf("c") as String

    DiscordLogger(Config(botToken, channelId))
}