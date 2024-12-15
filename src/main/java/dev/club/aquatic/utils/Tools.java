package dev.club.aquatic.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vasty
 * @date 14/12/2024 @ 03:27
 * @url https://github.com/vastydev
 */

public class Tools {

    private final static int CENTER_PX = 154;

    public static String CenterMessage(String message) {

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {

            if (c == '§') {

                previousCode = true;
                continue;

            } else if (previousCode) {

                previousCode = false;

                if (c == 'l' || c == 'L') {

                    isBold = true;
                    continue;

                } else isBold = false;

            } else {

                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;

            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();

        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb.toString() + message;

    }
}