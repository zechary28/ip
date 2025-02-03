package duke.command;

import duke.component.*;

public abstract class Command {
    public abstract void execute(Luke.TaskList tl, Luke.Ui ui, Luke.Storage st);
}