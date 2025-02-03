package luke.command;

import luke.component.*;

public abstract class Command {
    public abstract void execute(TaskList tl, Ui ui, Storage st);
}