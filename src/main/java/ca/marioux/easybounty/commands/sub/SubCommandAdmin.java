package ca.marioux.easybounty.commands.sub;

import ca.marioux.easybounty.application.BountyService;
import ca.marioux.easybounty.domain.*;
import ca.marioux.easybounty.domain.exception.BountyDoesntExistsException;
import ca.marioux.easybounty.domain.exception.EasyBountyException;
import ca.marioux.easybounty.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubCommandAdmin {

    private final BountyService bountyService;

    public SubCommandAdmin(BountyService bountyService) {
        this.bountyService = bountyService;
    }

    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length == 3) {
            if (args[0].equals("remove")) {
                return delete(args[1], args[2], sender);
            } else {
                return invalidArguments(sender);
            }
        } else {
            return invalidArguments(sender);
        }
    }

    private boolean delete(String target, String benefactor, CommandSender sender) {
        // TODO: if has permission

        PlayerId targetId = PlayerUtils.getPlayerIdFromServer(target);
        PlayerId benefactorId = PlayerUtils.getPlayerIdFromServer(benefactor);

        try {
            bountyService.remove(benefactorId, targetId);
            return bountyRemoved(sender, benefactor, target);
        } catch (BountyDoesntExistsException e) {
            return serviceExceptionMessage(sender, e);
        }
    }

    private boolean bountyRemoved(CommandSender sender, String benefactor, String target) {
        sender.sendMessage(ChatColor.AQUA + String.format("The bounty for %s placed by %s has been removed", target, benefactor));
        return true;
    }

    private boolean invalidArguments(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Invalid arguments");
        sender.sendMessage(ChatColor.RED + "Correct usage: /bounty admin remove <TARGET-NAME> <BENEFACTOR-NAME>");
        return true;
    }

    private boolean serviceExceptionMessage(CommandSender sender, EasyBountyException e) {
        sender.sendMessage(ChatColor.RED + e.getMessage());
        return true;
    }
}
