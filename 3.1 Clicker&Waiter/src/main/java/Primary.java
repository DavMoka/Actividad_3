import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class Primary {
    @FXML public Label lblClick;
    @FXML public Label lblWait;
    @FXML public ProgressBar barWait;
    @FXML public Button btnWait;

    private int clicks = 0;


    @FXML public void actionClick() {
        clicks++;
        lblClick.setText(""+clicks);
    }

    @FXML public void actionWait() {
        btnWait.setDisable(true);

        new Thread(() -> {
            long startTime = System.nanoTime();

            while(true) {
                long elapsed = System.nanoTime() - startTime;

                double progress = (double)elapsed/10_000_000_000L > 1 ? 1 : (double)elapsed/10_000_000_000L;
                int progSec = (int)(progress*10);

                Platform.runLater(() -> {
                    lblWait.setText(progSec == 10 ? "¡Listo!" : (""+progSec+"/10"));
                    barWait.setProgress(progress);
                });

                if(progress >= 1.0) {
                    Platform.runLater(() -> { btnWait.setDisable(false); });
                    break;
                }

                try { Thread.sleep(50); } catch(InterruptedException ex) {}
            }
        }).start();
    }
}
