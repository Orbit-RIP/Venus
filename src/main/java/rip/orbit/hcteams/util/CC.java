package rip.orbit.hcteams.util;

import org.bukkit.ChatColor;
import rip.orbit.hcteams.HCF;

import java.util.ArrayList;
import java.util.List;

public class CC {
    public static String BLUE;
    public static String AQUA;
    public static String YELLOW;
    public static String RED;
    public static String GRAY;
    public static String GOLD;
    public static String GREEN;
    public static String WHITE;
    public static String BLACK;
    public static String BOLD;
    public static String ITALIC;
    public static String STRIKE_THROUGH;
    public static String RESET;
    public static String MAGIC;
    public static String DARK_BLUE;
    public static String DARK_AQUA;
    public static String DARK_GRAY;
    public static String DARK_GREEN;
    public static String DARK_PURPLE;
    public static String DARK_RED;
    public static String LIGHT_PURPLE;
    public static String CHAT_BAR;
    public static String MENU_BAR;
    public static String SB_BAR;
    public static String CONSOLE_NAME;
    public static String BLANK_LINE = "§8 §8 §1 §3 §3 §7 §8 §r";
    public static String prefix = CC.translate(HCF.getInstance().getConfig().getString("networkName")) + " §7┃§r ";
    public static String GRAY_LINE = " §7┃ ";

    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static String chat(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return toReturn;
    }

    public static List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return toReturn;
    }

    public static byte getByChatColor(ChatColor color) {
        switch (color) {
            case DARK_RED:
            case RED:
                return (short) 14;
            case BLUE:
            case DARK_BLUE:
                return (short) 11;
            case AQUA:
                return (short) 3;
            case BLACK:
                return (short) 15;
            case DARK_AQUA:
                return (short) 9;
            case DARK_GRAY:
                return (short) 7;
            case DARK_GREEN:
                return (short) 13;
            case DARK_PURPLE:
                return (short) 10;
            case GOLD:
                return (short) 1;
            case GRAY:
                return (short) 8;
            case LIGHT_PURPLE:
                return (short) 6;
            case WHITE:
                return (short) 0;
            case YELLOW:
                return (short) 4;
            case GREEN:
                return (short) 5;
        }

        return (short) 0;
    }

    public static String getNameByChatColor(ChatColor color) {
        switch (color) {
            case DARK_RED:
                return "Dark Red";
            case RED:
                return "Red";
            case BLUE:
                return "Blue";
            case DARK_BLUE:
                return "Dark Blue";
            case AQUA:
                return "Aqua";
            case BLACK:
                return "Black";
            case DARK_AQUA:
                return "Cyan";
            case DARK_GRAY:
                return "Dark Gray";
            case DARK_GREEN:
                return "Dark Green";
            case DARK_PURPLE:
                return "Purple";
            case GOLD:
                return "Orange";
            case GRAY:
                return "Gray";
            case LIGHT_PURPLE:
                return "Pink";
            case WHITE:
                return "White";
            case YELLOW:
                return "Yellow";
            case GREEN:
                return "Green";
        }

        return "White";
    }

    static {
        BLUE = ChatColor.BLUE.toString();
        AQUA = ChatColor.AQUA.toString();
        YELLOW = ChatColor.YELLOW.toString();
        RED = ChatColor.RED.toString();
        GRAY = ChatColor.GRAY.toString();
        GOLD = ChatColor.GOLD.toString();
        GREEN = ChatColor.GREEN.toString();
        WHITE = ChatColor.WHITE.toString();
        BLACK = ChatColor.BLACK.toString();
        BOLD = ChatColor.BOLD.toString();
        ITALIC = ChatColor.ITALIC.toString();
        STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
        RESET = ChatColor.RESET.toString();
        MAGIC = ChatColor.MAGIC.toString();
        DARK_BLUE = ChatColor.DARK_BLUE.toString();
        DARK_AQUA = ChatColor.DARK_AQUA.toString();
        DARK_GRAY = ChatColor.DARK_GRAY.toString();
        DARK_GREEN = ChatColor.DARK_GREEN.toString();
        DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
        DARK_RED = ChatColor.DARK_RED.toString();
        LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString();
        MENU_BAR = ChatColor.STRIKETHROUGH.toString() + "------------------------";
        CHAT_BAR = ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------";
        SB_BAR = ChatColor.STRIKETHROUGH.toString() + "----------------------";
        CONSOLE_NAME = "§4§lConsole";
    }
}
