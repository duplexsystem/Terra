package com.dfsek.terra.command.structure.load;

import com.dfsek.terra.Terra;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.structure.Rotation;
import com.dfsek.terra.structure.Structure;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.DebugCommand;
import org.polydev.gaea.command.PlayerCommand;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadFullCommand extends PlayerCommand implements DebugCommand {
    private final boolean chunk;

    public LoadFullCommand(org.polydev.gaea.command.Command parent, boolean chunk) {
        super(parent);
        this.chunk = chunk;
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        try {
            Rotation r;
            try {
                r = Rotation.fromDegrees(Integer.parseInt(args[1]));
            } catch(NumberFormatException e) {
                LangUtil.send("command.structure.invalid-rotation", sender, args[1]);
                return true;
            }
            Structure struc = Structure.load(new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure"));
            if(chunk) struc.paste(sender.getLocation(), sender.getLocation().getChunk(), r);
            else struc.paste(sender.getLocation(), r);
            //sender.sendMessage(String.valueOf(struc.checkSpawns(sender.getLocation(), r)));
        } catch(IOException e) {
            e.printStackTrace();
            LangUtil.send("command.structure.invalid", sender, args[0]);
        }
        return true;
    }

    @Override
    public String getName() {
        return chunk ? "chunk" : "full";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        switch(args.length) {
            case 1:
                return LoadCommand.getStructureNames().stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
            case 2:
                return Stream.of("0", "90", "180", "270").filter(string -> string.toUpperCase().startsWith(args[1].toUpperCase())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
