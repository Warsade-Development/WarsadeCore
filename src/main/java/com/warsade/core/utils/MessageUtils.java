package com.warsade.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageUtils {

    public static void sendMessageList(CommandSender sender, List<String> message) {
        for (String line : message) {
            sender.sendMessage(line);
        }
    }

    public static String replaceColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> replaceColor(List<String> message) {
        ArrayList<String> list = new ArrayList<>();
        message.forEach(line -> list.add(replaceColor(line)));

        return list;
    }

    public static List<String> replace(List<String> message, Object one, Object two) {
        ArrayList<String> list = new ArrayList<>();
        message.forEach(line -> list.add(line.replace(one.toString(), two.toString())));

        return list;
    }

    public static List<String> replace(List<String> message, Object[] one, Object[] two) {
        List<Object> listObjectsOne = Arrays.stream(one).collect(Collectors.toList());
        ArrayList<String> list = new ArrayList<>();
        message.forEach(line -> {
            String currentLine = line;
            for (Object selected : listObjectsOne) {
                currentLine = currentLine.replace(selected.toString(), two[listObjectsOne.indexOf(selected)].toString());
            }
            list.add(currentLine);
        });

        return list;
    }

}