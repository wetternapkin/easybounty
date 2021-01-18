# EasyBounty
A easy Minecraft Spigot Server Plugin to make bounties on other players

# Important
This plugin is only a small project for me to discover how to make a Minecraft Server plugin.
I'm aware the code is not perfect, it's my first plugin. If I decide to add more functionalities in the future, some code will be refactored.

The plugin should work correctly, but **I don't give any garantee**.

# Pull request
Don't expect anything if you make a pull request. Again, this is a small personnal project and I don't expect anyone to request changes.
If you see an important issue, make an issue on GitHub and I will try to fix it.

# Commands
Base command: **/bounty create|remove|admin**

## Create
Usage: **/bounty create TARGET-NAME REWARD COUNT**
Permission: easybounty.create

## Remove
Usage: **/bounty remove TARGET-NAME**
Permission: easybounty.remove

## Admin
Usage: **/bounty admin remove BENEFACTOR-NAME TARGET-NAME**
Permission: easybounty.admin.remove

## Refunds
When you create a bounty, the reward you want to offer is removed from your inventory. If an admin removed your bounty, the plugin will wait for you to be online and will give you back the reward you gave.
