package luke.command;

import luke.component.StorageTest;
import luke.component.TaskListTest;
import luke.component.UiTest;

public abstract class CommandTest {
    public abstract void execute(TaskListTest tl, UiTest uiTest, StorageTest st);
}
