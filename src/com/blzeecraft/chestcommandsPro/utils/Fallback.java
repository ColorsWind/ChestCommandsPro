package com.blzeecraft.chestcommandsPro.utils;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.serializer.CommandSerializer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.gmail.filoghost.chestcommands.util.FormatUtils.addColors;

public class Fallback {

    public static List<IconCommand> readCommands(String input) {
        String separator = ChestCommands.getSettings().multiple_commands_separator;
        if (separator == null || separator.isEmpty())
            separator = ";";
        List<IconCommand> iconCommands = new ArrayList<>();
        for (String str : input.split(Pattern.quote(separator)))
            if (str.length() > 0)
                iconCommands.add(CommandSerializer.matchCommand(str));
        return iconCommands;
    }

    public static List<String> colorizeLore(List<String> input) {
        if (input == null || input.isEmpty()) return input;
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            if (!line.isEmpty())
                if (line.charAt(0) != 167) {
                    input.set(i, ChestCommands.getSettings().default_color__lore + addColors(line));
                } else {
                    input.set(i, addColors(line));
                }
        }
        return input;
    }

    public static String colorizeName(String input) {
        if (input == null || input.isEmpty()) return input;
        if (input.charAt(0) != 167)
            return ChestCommands.getSettings().default_color__name + addColors(input);
        return addColors(input);
    }
}
