import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class Primary implements Initializable {
    @FXML Button btnStart;
    @FXML Label lblHare;
    @FXML ProgressBar barHare;
    @FXML Label lblTurtle;
    @FXML ProgressBar barTurtle;

    // Goal: 1000
    int hareProgress, turtleProgress, hareEnergy;

    private static int RNG(int min, int max) {
        return (int)Math.floor(Math.random() * (max - min + 1)) + min;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepare();
    }
    
    @FXML public void actionRun() {
        prepare();
        btnStart.setDisable(true);
        hareProgress = 0;
        turtleProgress = 0;
        hareEnergy = RNG(100, 300);

        // Hare run
        new Thread(new Runnable() { @Override public void run() {
            while(turtleProgress != 1000 && hareProgress != 1000) {
                int hareAdvance = RNG(10, 100);
                hareEnergy -= hareAdvance;
                hareProgress += hareAdvance;
                if(hareProgress > 1000) hareProgress = 1000;
                Platform.runLater(() -> {
                    if(hareProgress == 1000) lblHare.setText("¡La liebre completado la carrera!");
                    else lblHare.setText("La liebre ha avanzado " + hareAdvance + "m.");
                    barHare.setProgress((double)hareProgress/1000);
                });
                if(hareEnergy <= 0 && hareProgress != 1000) {
                    int sleep = RNG(5000, 20000);
                    Platform.runLater(() -> {
                      lblHare.setText(lblHare.getText() + "... y se ha dormido durante " + sleep/1000 + " segundos.");
                    });
                    try { Thread.sleep(sleep); } catch(InterruptedException ex) {}
                    hareEnergy = RNG(300, 500);
                }
                else try { Thread.sleep(RNG(100, 1000)); } catch(InterruptedException ex) {}
            }
        }}).start();
        // Turtle run
        new Thread(new Runnable() { @Override public void run() {
            while(turtleProgress != 1000 && hareProgress != 1000) {
                int turtleAdvance = RNG(10, 100);
                turtleProgress += turtleAdvance;
                if(turtleProgress > 1000) turtleProgress = 1000;
                Platform.runLater(() -> {
                    if(turtleProgress == 1000) lblTurtle.setText("¡La tortuga completado la carrera!");
                    else lblTurtle.setText("La tortuga ha avanzado " + turtleAdvance + "m.");
                    barTurtle.setProgress((double)turtleProgress/1000);
                });
                try { Thread.sleep(RNG(500, 2000)); } catch(InterruptedException ex) {}
            }
        }}).start();
        // Button activate
        new Thread(new Runnable() { @Override public void run() {
            while(turtleProgress != 1000 && hareProgress != 1000) try { Thread.sleep(100); } catch(InterruptedException ex) {}
            Platform.runLater(() -> { btnStart.setDisable(false); });
        }}).start();
    }

    public void prepare() {
        btnStart.setDisable(false);
        lblHare.setText("La liebre se prepara para la carrera...");
        barHare.setProgress(0);
        lblTurtle.setText("La tortuga se prepara para la carrera...");
        barTurtle.setProgress(0);
    }
}
