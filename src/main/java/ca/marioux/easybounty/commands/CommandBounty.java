package ca.marioux.easybounty.commands;

import ca.marioux.easybounty.application.BountyService;
import ca.marioux.easybounty.application.exception.BenefactorAndTargetAreSameException;
import ca.marioux.easybounty.application.exception.PlayerDoesntHaveRewardInInventoryException;
import ca.marioux.easybounty.commands.sub.SubCommandAdmin;
import ca.marioux.easybounty.domain.*;
import ca.marioux.easybounty.domain.exception.BountyAlreadyExistsException;
import ca.marioux.easybounty.domain.exception.EasyBountyException;
import ca.marioux.easybounty.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBounty implements CommandExecutor {

    private final SubCommandAdmin subCommandAdmin;
    private final BountyService bountyService;

    public CommandBounty(BountyService bountyService) {
        this.bountyService = bountyService;

        subCommandAdmin = new SubCommandAdmin(bountyService);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        String subcommand = args[0];
        if (subcommand.equals("create")) {
            return subCommandCreate(sender, args);
        } else if (subcommand.equals("remove")) {
            return subCommandRemove(sender, args);
        } else if (subcommand.equals("admin")) {
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            return subCommandAdmin.onCommand(sender, subArgs);
        } else {
            sender.sendMessage(ChatColor.RED + "command " + subcommand + " doesn't exist");
            return false;
        }
    }

    private boolean subCommandRemove(CommandSender sender, String[] args) {
        if (args.length != 2) {
            return invalidArgumentsForRemove(sender);
        }

        if (!sender.hasPermission("easybounty.remove")) {
            return notPermitted(sender);
        }

        String targetName = args[1];
        PlayerId targetId = PlayerUtils.getPlayerIdFromServer(targetName);

        if (sender instanceof Player) {
            Player benefactor = (Player) sender;

            try {
                bountyService.selfRemove(benefactor, targetId);
                sender.sendMessage("You got the reward back in your inventory");
            } catch (EasyBountyException e) {
                return serviceExceptionMessage(sender, e);
            }
        } else {
            return invalidCommandInConsole(sender);
        }

        return true;
    }

    private boolean subCommandCreate(CommandSender sender, String[] args) {
        if (args.length != 4) {
            return invalidArgumentsForCreate(sender);
        }

        if (!(sender instanceof Player)) {
            return invalidCommandInConsole(sender);
        }

        if (!sender.hasPermission("easybounty.create")) {
            return notPermitted(sender);
        }

        Player benefactorPlayer = (Player) sender;

        String targetName = args[1];
        String rewardAsString = args[2];
        int quantity = Integer.parseInt(args[3]);

        Material rewardMaterial = Material.getMaterial(rewardAsString);
        ItemStack reward = new ItemStack(rewardMaterial, quantity);

        PlayerId target = PlayerUtils.getPlayerIdFromServer(targetName);

        if (target == null) {
            return playerDoesntExist(sender);
        }

        try {
            bountyService.create(benefactorPlayer, target, reward);
        } catch (EasyBountyException e) {
            return serviceExceptionMessage(sender, e);
        }

        return bountyCreated(sender, targetName);

    }

    private boolean invalidArgumentsForRemove(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Invalid arguments");
        sender.sendMessage(ChatColor.RED + "Correct usage: /bounty remove <PLAYER-NAME>");
        return true;
    }

    private boolean invalidArgumentsForCreate(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Invalid arguments");
        sender.sendMessage(ChatColor.RED + "Correct usage: /bounty create <PLAYER-NAME> <REWARD> <AMOUNT>");
        return true;
    }

    private boolean invalidCommandInConsole(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "In the console, you must manage bounties with /bounty admin ...");
        return true;
    }

    private boolean serviceExceptionMessage(CommandSender sender, EasyBountyException e) {
        sender.sendMessage(ChatColor.RED + e.getMessage());
        return true;
    }

    private boolean playerDoesntExist(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "The specified player has not been found on this server");
        return true;
    }

    private boolean bountyCreated(CommandSender sender, String targetName) {
        return successMessage(sender, String.format("Your bounty for %s has been created successfully", targetName));
    }

    private boolean successMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.AQUA + message);
        return true;
    }

    private boolean notPermitted(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You don't have the permission to execute that command");
        return true;
    }
}
