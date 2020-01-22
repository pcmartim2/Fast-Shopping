package pt.iade.fastShopping.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import pt.iade.fastShopping.WindowManager;
import pt.iade.fastShopping.models.daos.FavoritoDAO;


public class SidebarLojaController {
	
	@FXML
    private ImageView unfav, fav, imagemLoja;

    @FXML
    private BorderPane borderPane;
	
    /**
     * Variavel onde vai ficar armazenado o id do utilizador.
     */
    private int id_User = LoginUtilizadorController.IdUser;
    /**
     * Variavel onde vai ficar armazenado o id da loja onde o utilizador cliclou.
     */
	private int id_Loja = MapaScreenController.idLoja;
	
	/**
	 * Quando o fxml que corresponde a este controlador vai executar este codigo quando incia.
	 * Vai color a imagem no sidebar da loja em que o utlizador clicou e no lado direito do
	 * sidebar vai abrir um novo scene que corresponde a listview dos produtos da loja.
	 * E vai verficar se o utlizador tem a loja como favorito.
	 * @throws IOException exception
	 */
	@FXML
	private void initialize() throws IOException {
	

		//Metodo que verifica se o utilizador tem a loja como favorito
		if (FavoritoDAO.getFavoritoLoja(id_Loja, id_User)) {
			fav.setVisible(true);
			unfav.setVisible(false);
		}
		else {
			
			unfav.setVisible(true);
			fav.setVisible(false);
		}
	}

	/**
	 * Ao clica no button que tem a estrela sem fundo vai adicionar o favorito na base de dados.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void addFavorito(MouseEvent event) {

		if (event.getButton() == MouseButton.PRIMARY) {
			
			FavoritoDAO.addFavoritoUtilizador(id_User, MapaScreenController.idLoja);
			unfav.setVisible(false);
			fav.setVisible(true);
		}
	}

	/**
	 * Ao clica no button que tem a estrela com fundo amarelo vai remover o favorito na base de dados.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void removeFavorito(MouseEvent event) {

		if (event.getButton() == MouseButton.PRIMARY) {

			FavoritoDAO.removeFavoritoUtilizador(id_User, MapaScreenController.idLoja);
			unfav.setVisible(true);
			fav.setVisible(false);
		}
	}

	/**
	 * Ao clicar no button vai voltar para o ecra inicial
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void voltar(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			WindowManager.openMainWindow();
		}
	}

	
	/**
	 * Metodo para adiconar um pop-up com um titilo e um header
	 * @param title do pop-up
	 * @param header do pop-up
	 */
	public static void warningDialog(String title, String header) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle(title);
    	alert.setHeaderText(header);
    	alert.showAndWait();
    }

}
