import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        super.onLoad();

        register();
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


            sender.getServer().dispatchCommand(sender, command.toString());

        });
    }
}
