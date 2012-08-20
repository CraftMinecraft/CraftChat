# CraftChat Plugin

Craft Chat is the Chat Plugin designed for the Craft Minecraft Network.  Custom chat client to link servers, and link back to a master database through api

## Ideas

* **HelpOp** 
           * Will search for staff on other servers that are part of the network, will shop the helpop message to only the staff that can help
           * Have a reply command so user knows if someone is on the way to help (Maybe?)
           * Let the user know if noone is on that can help

* **Msg** - Inner server messaging, if on friends list
          - Check if user is on local server, if so message that user
          - If user is no on local server, check with master server to see if online, if online check if part of friend network, if so send message
          - Let user know if the user is not online, or not part of friend network if they are

* **Friend** - Allow users to create friend networks
             - Will show list on logon which friends are online and what server they are on
             - Friend will see message saying "Your friend, <name> has logged into <server>
             - Friend will need to accept new friends