package pt.iade.fastShopping;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pt.iade.fastShopping.controllers.LoginUtilizadorController;
import pt.iade.fastShopping.controllers.MapaScreenController;
import pt.iade.fastShopping.controllers.SidebarLojaController;

public class WindowManager {
	
	
	private static Stage primaryStage;

	public static void setPrimaryStage(Stage primaryStage) {
		WindowManager.primaryStage = primaryStage;
	}

	public static void openLoginWindow() {
		openWindow("views/LoginUtilizador.fxml", primaryStage, new LoginUtilizadorController(), "FastShopping", 600,
				400);
		primaryStage.show();
	}

	public static void openWindow(String viewPath, Stage window, Object controller, String titulo, double width,
			double height) {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource(viewPath));
			loader.setController(controller);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
			window.setTitle(titulo);
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			window.setX((screenBounds.getWidth() - width) / 2);
			window.setY((screenBounds.getHeight() - height) / 2);
			window.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void loadUI(String viewPath, Object controller, BorderPane pane) {
		AnchorPane root = null;
		try {
			
			FXMLLoader loader = new FXMLLoader(Main.class.getResource(viewPath));
			loader.setController(controller);
			root = loader.load();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		pane.setCenter(root);
	}
	
	public static void openMainWindow() {
		openWindow("views/MapaScreen.fxml", primaryStage, new MapaScreenController(), "FastShopping", 926, 471);
		primaryStage.show();
	}
	
	public static void openSidebarLojaWindow() {
		openWindow("views/SidebarLoja.fxml", primaryStage, new SidebarLojaController(), "FastShopping", 926, 471);
		primaryStage.show();
	}

}
