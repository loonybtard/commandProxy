import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        super.onLoad();

        register();
    }

    @Override
    public void onDisable() {

        CommandAPI.getInstance().unregister("commandproxy");

        super.onDisable();
    }

    private String[] getAllCommands() {

        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

        List<String> commands_set = new ArrayList<String>();

        for (Plugin plugin : plugins) {
            Set<String> list = plugin.getDescription().getCommands().keySet();

            commands_set.addAll(list);

        }

        return commands_set.toArray(new String[commands_set.size()]);

    }

    private void register() {
        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<String, Argument>();
        arguments.put("command", new GreedyStringArgument());
        CommandAPI.getInstance().register("commandproxy", arguments, (sender, args) -> {

            //fixme
            List<String> command_raw = new ArrayList<String>();

            for (Object str : args) {
                if (command_raw.size() != 0)
                    command_raw.add(" ");
                command_raw.add(str.toString());
            }

            StringBuilder command = new StringBuilder();
            for (String part : command_raw) {
                command.append(part);
            }


            // check our target
            if(sender instanceof ProxiedCommandSender){
                CommandSender calee = ((ProxiedCommandSender) sender).getCallee();

                // if its player
                if(calee instanceof Player){
                    // command will executed on its behalf
                    sender = calee;
                }
            }

            sender.getServer().dispatchCommand(sender, command.toString());

        });
    }
}
