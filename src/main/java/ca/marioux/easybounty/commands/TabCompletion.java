package ca.marioux.easybounty.commands;

import ca.marioux.easybounty.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {
    private static final String SUB_COMMAND_CREATE = "create";
    private static final String SUB_COMMAND_REMOVE = "remove";
    private static final List<String> SUB_COMMANDS = new ArrayList<String>();

    static {
        SUB_COMMANDS.add(SUB_COMMAND_CREATE);
        SUB_COMMANDS.add(SUB_COMMAND_REMOVE);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String subcommand = args[0];
        if (args.length == 1) {
            return filterSearch(SUB_COMMANDS, subcommand.toLowerCase());
        } else if (subcommand.equals(SUB_COMMAND_CREATE)) {
            if (args.length == 2) {
                return onlinePlayersAutoComplete(args[1]);
            } else if (args.length == 3) {
                final String enteredValue = args[2].toUpperCase();

                List<String> values = new ArrayList<String>();
                for(Material material : Material.values()) {
                    values.add(material.name());
                }

                return filterSearch(values, enteredValue, "LEGACY_");
            }
        } else if (subcommand.equals(SUB_COMMAND_REMOVE)) {
            if (args.length == 2) {
                return onlinePlayersAutoComplete(args[1]);
            }
        }

        return null;
    }

    private List<String> onlinePlayersAutoComplete(String enteredName) {
        List<String> playerNames = new ArrayList<String>();
        playerNames.addAll(PlayerUtils.getAllPlayersOnlineAndOffline().values());

        return filterSearch(playerNames, enteredName);
    }

    private List<String> filterSearch(List<String> values, String search) {
        if (search == null || search.length() == 0) {
            return values;
        }

        List<String> filteredList = new ArrayList<String>();
        for (String s : values) {
            if (s.contains(search)) {
                filteredList.add(s);
            }
        }

        return filteredList;
    }

    private List<String> filterSearch(List<String> values, String search, String withoutBeguinningBy) {
        if (withoutBeguinningBy == null || withoutBeguinningBy.length() == 0) {
            return filterSearch(values, search);
        }

        List<String> firstPassFilteredList = filterSearch(values, search);
        List<String> secondPassFilteredList = new ArrayList<String>();

        for(String s : firstPassFilteredList) {
            if (!s.startsWith(withoutBeguinningBy)) {
                secondPassFilteredList.add(s);
            }
        }

        return secondPassFilteredList;
    }
}
