package pt.iade.fastShopping;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import pt.iade.fastShopping.models.daos.DBConnector;

/**
 * A nossa aplicacao consiste em um utilizador ter um mapa da sua zona disponibilizado, mostrando as lojas registada na aplica��o 
 * e ao clicar numa certa loja o utilizador ir� conseguir ver todos os produtos que a loja tem escolhendo a respetiva quantidade que o utilizador 
 * ir� quer comprar e ver o preco de cada produto.
 * Vai conseguir adicionar os produtos no carrinho, onde ir� aparecer o preco total que ir� ter de pagar para efetuar a compra.
 * e os seus produtos incluindo os seus pre�os e talvez informa��o adicional caso fosse necess�rio.
 * @author Miguel
 * @author Martim
 *
 */

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		//Conectar com a base de dados
		DBConnector.abrirConexao();
	
		WindowManager.setPrimaryStage(primaryStage);
		WindowManager.openLoginWindow();
	
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
