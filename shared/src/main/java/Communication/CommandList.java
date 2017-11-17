package Communication;

import java.util.List;

import Command.Command;

/**
 * Created by autumnchapman on 10/21/17.
 */

public class CommandList {

    private List<String> commands;

    public CommandList(){}

    public CommandList(List<String> cmds){
        this.commands = cmds;
    }

    public List<String> getCommands() {
        return commands;
    }
}
