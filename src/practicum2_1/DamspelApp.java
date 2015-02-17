package practicum2_1;

import javafx.util.Duration;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author martijn en iedereen op deze mooie wereld
 *
 */
public class DamspelApp extends Application implements EventHandler<ActionEvent> {
	private Button[][] buttonbord = new Button[10][10];
	private Button reset = new Button("Reset");
	private Label melding = new Label("Meldingen komen hier!");
	private CheckBox roteren = new CheckBox();
	private Label speler = new Label("Speler :");
	private Label stenenZ = new Label("Stenen Z: ");
	private Label stenenW = new Label("Stenen W: ");
	private Damspel spel = new Damspel();
	private GridPane bord = new GridPane();
	private boolean clicked = false;
	private Button prevButton;

	@Override
	public void start(Stage primaryStage) {
		roteren.setSelected(true);
		BorderPane root = new BorderPane();
		VBox links = new VBox();
		links.getChildren().addAll(reset, roteren, speler, stenenZ, stenenW);
		links.setSpacing(3);
		links.setPadding(new Insets(20, 0, 0, 10));

		speler.setPrefSize(100, 30);
		stenenZ.setPrefSize(100, 30);
		stenenW.setPrefSize(100, 30);

		bord.setPrefSize(300, 200);
		bord.setAlignment(Pos.BOTTOM_RIGHT);

		reset.setOnAction(this);
		reset.setPrefSize(120, 20);
		reset.getStyleClass().add("reset");

		melding.getStyleClass().add("melding");
		root.setAlignment(melding, Pos.TOP_RIGHT);
		root.setPadding(new Insets(5, 5, 5, 5));
		root.setTop(melding);

		int k = 0;
		for (int i = 0; i < buttonbord.length; i++) {
			for (int j = 0; j < buttonbord.length; j++) {
				buttonbord[i][j] = new Button();
				buttonbord[i][j].setOnAction(this);
				buttonbord[i][j].setPrefSize(40, 40);
				buttonbord[i][j].setId(String.valueOf(k));
				buttonbord[i][j].getStyleClass().add(spel.getVeldStatus(k));
				k++;

				bord.getChildren().add(buttonbord[i][j]);
				bord.setRowIndex(buttonbord[i][j], i);
				bord.setColumnIndex(buttonbord[i][j], j);

			}
		}

		Scene scene = new Scene(root, 550, 430);
		scene.getStylesheets().add("practicum2_1/stylesheet.css");
		root.getStylesheets().add("practicum2_1/stylesheet.css");
		root.setCenter(bord);
		root.setLeft(links);
		primaryStage.setTitle("DamApp");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
	}

	public void handle(ActionEvent event) {
		if (event.getSource() == reset) {
			spel.reset();
			update();
		} else {
			Button but = (Button) event.getSource();
			if (!clicked && spel.isVeldSpeelbaar(Integer.valueOf(but.getId()))) {
				but.setStyle("-fx-border-color: red; -fx-border-width: 3;");
				prevButton = but;
				clicked = true;
				upMel();
			} else {
				prevButton.setStyle("");
				but.getStyleClass().add(spel.getVeldStatus(Integer.valueOf(but.getId())));
				if (spel.doeZet(Integer.valueOf(prevButton.getId()), Integer.valueOf(but.getId()))) {
					if (roteren.isSelected()) {
						roteren();
					}
				}
				update();
				clicked = false;
				upMel();
			}
		}
	}

	public boolean upMel() {
		melding.setText(spel.getMelding());
		return false;
	}

	public void update() {
		int k = 0;
		for (int i = 0; i < buttonbord.length; i++) {
			for (int j = 0; j < buttonbord.length; j++) {
				buttonbord[i][j].getStyleClass().removeAll("ZWART", "WIT", "LEEG", "ZWARTDAM", "WITDAM", "NIETSPEELBAAR", "SPEELBAAR");
				buttonbord[i][j].getStyleClass().add(spel.getVeldStatus(k));
				k++;
			}
		}
	}

	public void roteren() {
		RotateTransition timer = new RotateTransition(Duration.millis(3000), bord);
		timer.setByAngle(180);
		timer.setCycleCount(1);
		timer.setAutoReverse(false);
		timer.play();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
