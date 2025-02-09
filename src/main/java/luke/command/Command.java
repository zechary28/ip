package luke.command;

import luke.component.Storage;
import luke.component.TaskList;
import luke.component.Ui;

public abstract class Command {
    public abstract void execute(TaskList tl, Ui ui, Storage st);
}
