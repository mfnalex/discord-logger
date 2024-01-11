package com.jeff_media.discordlogger

import net.dv8tion.jda.api.utils.TimeUtil
import java.time.OffsetDateTime

fun discordTimestamp(timestamp: OffsetDateTime): String {
    return TimeUtil.getDateTimeString(timestamp)
}

fun String.shorten(maxLen: Int): String {
    if(this.length <= maxLen) return this
    return this.substring(0, maxLen-3) + "..."
}