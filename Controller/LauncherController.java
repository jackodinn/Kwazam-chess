package Controller;

import Model.*;
import View.*;

public class LauncherController {

    private ChessModel model;
    private Chessboard view;
    private final Launcher launcher;

    public LauncherController(Launcher launcher) {
        this.launcher = launcher;
        launcher.getLaunchButton().addActionListener(e -> handleLaunch());
    }

    private void handleLaunch() {
        System.out.println("Launching...");
        this.view = new Chessboard();
        this.model = new ChessModel();
        ChessController chessController = new ChessController(model, view);
        view.setVisible(true);
        System.out.println("Launched!");
        launcher.setVisible(true);
    }
}
