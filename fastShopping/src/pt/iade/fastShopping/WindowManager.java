package pt.iade.fastShopping;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pt.iade.fastShopping.controllers.SidebarLojaController;
import pt.iade.fastShopping.controllers.LoginUtilizadorController;
import pt.iade.fastShopping.controllers.LojaAddProdutosController;
import pt.iade.fastShopping.controllers.LojaComentariosScreenController;
import pt.iade.fastShopping.controllers.LojaProdutosController;
import pt.iade.fastShopping.controllers.MapaAdminScreenController;
import pt.iade.fastShopping.controllers.MapaScreenController;

public class WindowManager {
	
	private static Stage primaryStage;

	public static void setPrimaryStage(Stage primaryStage) {
		WindowManager.primaryStage = primaryStage;
	}

	
	/**
	 * Metodo que serve para abrir a janela do Mapa das Lojas.
	 */
	public static void openMainWindow() {
		openWindow("views/MapaScreen.fxml", primaryStage, new MapaScreenController(), "FastShopping", 926, 471);
		primaryStage.show();
	}

	/**
	 * Metodo que serve para abrir a janela de Login.
	 */
	public static void openLoginWindow() {
		openWindow("views/LoginUtilizador.fxml", primaryStage, new LoginUtilizadorController(), "FastShopping", 600,
				400);
		primaryStage.show();
	}

	/**
	 * Metodo que serve para abrir a janela do Admin.
	 */
	public static void openAdminWindow() {
		openWindow("views/MapaScreenAdmin.fxml", primaryStage, new MapaAdminScreenController(), "FastShopping", 926,
				471);
		primaryStage.show();
	}
	
	/**
	 * Metodo que serve para abrir a janela do Proprietario da Loja.
	 */
	public static void openLojaAddProdutosWindow() {
		openWindow("views/LojaAddProdutos.fxml", primaryStage, new LojaAddProdutosController(), "FastShopping", 926,
				471);
		primaryStage.show();
	}

	/**
	 * Metodo que serve para abrir a janela dos Comentarios da Loja.
	 */
	public static void openComentariosWindow() {
		openWindow("views/LojaComentariosScreen.fxml", primaryStage, new LojaComentariosScreenController(),
				"FastShopping", 926, 471);
		primaryStage.show();
	}

	/**
	 * Metodo que serve para abrir a janela do Produtos da Loja.
	 */
	public static void openProdutosWindow() {
		openWindow("views/LojaProdutos.fxml", primaryStage, new LojaProdutosController(), "FastShopping", 926, 471);
		primaryStage.show();
	}
	
	/**
	 * Metodo que serve para abrir a janela do SideBar.
	 */
	public static void openSidebarLojaWindow() {
		openWindow("views/SidebarLoja.fxml", primaryStage, new SidebarLojaController(), "FastShopping", 926, 471);
		primaryStage.show();
	}

	/**
	 * Metodo que serve para abrir as janelas e adicionar o controlador.
	 * @param viewPath local do fxml
	 * @param window stage
	 * @param controller controlador do fxml
	 * @param titulo titulo a jenela
	 * @param width largura da janela
	 * @param height altura da janela
	 */
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
	
	/**
	 * Metodo vai servir para carregar um fxml dentro de um fxml.
	 * @param viewPath local da janela
	 * @param controller conrolador da janela
	 * @param pane borderPane da janela
	 */
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
}
