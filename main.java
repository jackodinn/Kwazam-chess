/* Main.java
 - Main class to run the java application
 - Members invovlved: Andrew Wee
*/
import Controller.*;
import View.*;

public class main {

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        LauncherController controller = new LauncherController(launcher);
        launcher.setVisible(true);
    }
}
