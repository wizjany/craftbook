package com.sk89q.craftbook.bukkit.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.craftbook.bukkit.CircuitCore;
import com.sk89q.craftbook.bukkit.CraftBookPlugin;
import com.sk89q.craftbook.circuits.ic.ICDocsParser;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class CircuitCommands {

    private static CircuitCore circuitCore = CircuitCore.inst();

    public CircuitCommands(CraftBookPlugin plugin) {

    }

    @Command(aliases = {"ic"}, desc = "Commands to manage Craftbook IC's")
    @NestedCommand(ICCommands.class)
    public void icCmd(CommandContext context, CommandSender sender) {

    }

    public static class ICCommands {

        public ICCommands(CraftBookPlugin plugin) {

        }

        @Command(aliases = {"docs"}, desc = "Documentation on CraftBook IC's",
                usage = "<ic>", min = 1, max = 1)
        public void docsCmd(CommandContext args, CommandSender sender) {

            if (!(sender instanceof Player)) return;
            Player player = (Player) sender;
            ICDocsParser.generateICDocs(player, args.getString(0));
        }

        @Command(aliases = {"list"}, desc = "List available IC's",
                flags = "p:", usage = "[-p page]", min = 0, max = 0)
        public void listCmd(CommandContext args, CommandSender sender) {

            if (!(sender instanceof Player)) return;
            Player player = (Player) sender;
            char[] ar = null;
            try {
                ar = args.getString(1).toCharArray();
            } catch (Exception ignored) {
            }
            String[] lines = circuitCore.generateICText(player, null, ar);
            int pages = (lines.length - 1) / 9 + 1;
            int accessedPage;

            try {
                accessedPage = !args.hasFlag('p') ? 0 : args.getFlagInteger('p') - 1;
                if (accessedPage < 0 || accessedPage >= pages) {
                    player.sendMessage(ChatColor.RED + "Invalid page \"" + args.getFlagInteger('p') + "\"");
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid page \"" + args.getFlag('p') + "\"");
                return;
            }

            player.sendMessage(ChatColor.BLUE + "  ");
            player.sendMessage(ChatColor.BLUE + "CraftBook ICs (Page " + (accessedPage + 1) + " of " + pages + "):");

            for (int i = accessedPage * 9; i < lines.length && i < (accessedPage + 1) * 9; i++) {
                player.sendMessage(lines[i]);
            }
        }

        @Command(aliases = {"search"}, desc = "Search available IC's with names",
                flags = "p:", usage = "[-p page] <name>", min = 1, max = 1)
        public void searchCmd(CommandContext args, CommandSender sender) {

            if (!(sender instanceof Player)) return;
            Player player = (Player) sender;
            char[] ar = null;
            try {
                ar = args.getString(2).toCharArray();
            } catch (Exception ignored) {
            }
            String[] lines = circuitCore.generateICText(player, args.getString(0), ar);
            int pages = (lines.length - 1) / 9 + 1;
            int accessedPage;

            try {
                accessedPage = !args.hasFlag('p') ? 0 : args.getFlagInteger('p') - 1;
                if (accessedPage < 0 || accessedPage >= pages) {
                    player.sendMessage(ChatColor.RED + "Invalid page \"" + args.getFlagInteger('p') + "\"");
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid page \"" + args.getFlag('p') + "\"");
                return;
            }

            player.sendMessage(ChatColor.BLUE + "  ");
            player.sendMessage(ChatColor.BLUE + "CraftBook ICs \"" + args.getString(0) + "\" (Page " +
                    (accessedPage + 1) + " of " + pages + "):");

            for (int i = accessedPage * 9; i < lines.length && i < (accessedPage + 1) * 9; i++) {
                player.sendMessage(lines[i]);
            }
        }

        @Command(aliases = {"midis"}, desc = "List MIDI's available for Melody IC",
                flags = "p:", usage = "[-p page]", min = 0, max = 0)
        public void midiListCmd(CommandContext args, CommandSender sender) {

            if (!(sender instanceof Player)) return;
            Player player = (Player) sender;
            List<String> lines = new ArrayList<String>();

            FilenameFilter fnf = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {

                    return name.endsWith("mid") || name.endsWith(".midi");
                }
            };
            for (File f : circuitCore.getMidiFolder().listFiles(fnf)) {
                lines.add(f.getName().replace(".midi", "").replace(".mid", ""));
            }
            Collections.sort(lines, new Comparator<String>() {

                @Override
                public int compare(String f1, String f2) {

                    return f1.compareTo(f2);
                }
            });
            int pages = (lines.size() - 1) / 9 + 1;
            int accessedPage;

            try {
                accessedPage = !args.hasFlag('p') ? 0 : args.getFlagInteger('p') - 1;
                if (accessedPage < 0 || accessedPage >= pages) {
                    player.sendMessage(ChatColor.RED + "Invalid page \"" + args.getFlagInteger('p') + "\"");
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid page \"" + args.getFlag('p') + "\"");
                return;
            }

            player.sendMessage(ChatColor.BLUE + "  ");
            player.sendMessage(ChatColor.BLUE + "CraftBook MIDIs (Page " + (accessedPage + 1) + " of " + pages + "):");

            for (int i = accessedPage * 9; i < lines.size() && i < (accessedPage + 1) * 9; i++) {
                player.sendMessage(ChatColor.GREEN + lines.get(i));
            }
        }

        @Command(aliases = {"fireworks"}, desc = "List Fireworks available for PFD IC",
                flags = "p:", usage = "[-p page]", min = 0, max = 0)
        public void fireworkListCmd(CommandContext args, CommandSender sender) {

            if (!(sender instanceof Player)) return;
            Player player = (Player) sender;
            List<String> lines = new ArrayList<String>();

            FilenameFilter fnf = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {

                    return name.endsWith(".fwk") || name.endsWith(".txt");
                }
            };
            for (File f : circuitCore.getFireworkFolder().listFiles(fnf)) {
                lines.add(f.getName().replace(".txt", "").replace(".fwk", ""));
            }
            Collections.sort(lines, new Comparator<String>() {

                @Override
                public int compare(String f1, String f2) {

                    return f1.compareTo(f2);
                }
            });
            int pages = (lines.size() - 1) / 9 + 1;
            int accessedPage;

            try {
                accessedPage = !args.hasFlag('p') ? 0 : args.getFlagInteger('p') - 1;
                if (accessedPage < 0 || accessedPage >= pages) {
                    player.sendMessage(ChatColor.RED + "Invalid page \"" + args.getFlagInteger('p') + "\"");
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid page \"" + args.getFlag('p') + "\"");
                return;
            }

            player.sendMessage(ChatColor.BLUE + "  ");
            player.sendMessage(ChatColor.BLUE + "CraftBook Firework Displays (Page " + (accessedPage + 1) + " of " + pages + "):");

            for (int i = accessedPage * 9; i < lines.size() && i < (accessedPage + 1) * 9; i++) {
                player.sendMessage(ChatColor.GREEN + lines.get(i));
            }
        }
    }
}