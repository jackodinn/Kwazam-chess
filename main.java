import Controller.*;
import Model.*;
import View.*;

public class main {
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        LauncherController controller = new LauncherController(launcher);
        launcher.setVisible(true);
    }
}
