package com.fststep.nxt2me.core.util

import java.util.regex.Pattern

object JsonUtility {
    fun escapeDoubleQuotes(jsonString: String?): String {
        val pattern = Pattern.compile("\"")
        val matcher = pattern.matcher(jsonString)
        val stringBuffer = StringBuffer()
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, "\\\\\"")
        }
        matcher.appendTail(stringBuffer)
        return stringBuffer.toString()
    }
}

