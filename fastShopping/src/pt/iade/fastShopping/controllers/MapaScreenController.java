package pt.iade.fastShopping.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pt.iade.fastShopping.WindowManager;
import pt.iade.fastShopping.models.daos.DBConnector;
import pt.iade.fastShopping.models.daos.FavoritoDAO;
import pt.iade.fastShopping.models.daos.LojaDAO;

public class MapaScreenController {

	/**
	 * Variavel onde vai ficar armazenado o id da loja em que o utilizador clicou
	 */
	public static int idLoja;

	/**
	 * Verifica os favoritos
	 */
	private boolean visivel = false;
	private boolean disable = true;

	/**
	 * Onde vai adicionar as lojas
	 */
	@FXML
	private AnchorPane root;

	/**
	 * Nome do utilizador que entrou
	 */
	@FXML
	private Label nome_UtilizadorView;
	
	/**
	 * Onde vai ficar a listview dos favoritos
	 */
	@FXML
	private AnchorPane backgroundFavoritos;

	/**
	 * ListView de todos os favoritos de um certo utilizador
	 */
	@FXML
	private ListView<String> viewFavoritos;

	/**
	 * Quando o fxml que corresponde a este controlador vai executar este codigo quando incia.
	 * Vai desenhar todas as lojas que estiverem na base de dados no mapa.
	 * Adicona o nome do utilizador no canto superior direito
	 * Quando o utilizador clica numa das lojas que estao no mapa vai comparar as coordenadas onde clicou com 
	 * as coordenadas que estao na base de dados, para saber em qual loja o utilizador clicou. Depois disso 
	 * vai entrar dentro da loja, ou seja, vai conseguir ver todos os produtos que essa loja tem.
	 */
	@FXML
	private void initialize() {
		// nome utilizador label mapa screen
		nome_UtilizadorView.setText(LoginUtilizadorController.nome_UtilizadorLabel);
		// Desenha as lojas que estao na base de dados no mapa
		LojaDAO.loadLojasMapa(root);
		// verifica se as coordenadas carregadas pelo rato estão dentro das coordenadas
		// da loja e abre a janela de categorias da loja
		root.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double x = event.getX();
				double y = event.getY();
				//Metodo para verficar se o utilizador clicou num dos circlos no mapa
				//Se sim vai aparecer a pagina da loja
				try {
					PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT IdLoja, Coordenadas FROM Loja");
					ResultSet results = statement.executeQuery();
					while (results.next()) {
						String coordenadas = results.getString(2);
						//Vai tirar o # das coordenadas para sabermos a coordenada X e Y
						String[] split = coordenadas.split("#");
						double coordxLoja = Double.parseDouble(split[0]);
						double coordyLoja = Double.parseDouble(split[1]);
						
						//Vai comparar se as coordenadas onde o utilizador clicou com o rato coincidem com a da loja
						if (x > coordxLoja - 7 / 2 && x < coordxLoja + 7 / 2 && y > coordyLoja - 7 / 2 && y < coordyLoja + 7 / 2) {
							int LojaId = results.getInt(1);
							idLoja = LojaId;
							WindowManager.openSidebarLojaWindow();
						}
						
					}
					statement.close();
					results.close();
				}
				catch (SQLException ev) {
					ev.printStackTrace();
				}
			}
		});
		
		//Ao clicar no item na listview dos favoritos
		viewFavoritos.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	            idLoja = LojaDAO.getIdLoja(viewFavoritos.getSelectionModel().getSelectedItem());
				WindowManager.openSidebarLojaWindow();
	        }
	    });
	}

	/**
	 * Ao clicar no button vai voltar para o ecra inicial
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void Logout(MouseEvent event) {

		if (event.getButton() == MouseButton.PRIMARY) {
			WindowManager.openLoginWindow();
		}
	}

	/**
	 * Ao clicar no button que tem a estrela vai abrir uma pagina com todos
	 * os favoritos que o ulizador tem.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void clicar(MouseEvent event) {

		if (event.getButton() == MouseButton.PRIMARY) {

			visivel = !visivel;
			disable = !disable;

			backgroundFavoritos.setVisible(visivel);
			backgroundFavoritos.setDisable(disable);

			viewFavoritos.setVisible(visivel);
			viewFavoritos.setDisable(disable);
			//Mostrar todos os favoritos do utilizador
			FavoritoDAO.getFavoritosUtilizador(LoginUtilizadorController.IdUser, viewFavoritos);

		}
	}

	/**
	 * Ao clicar no button vai fechar a pagina com todos os favoritos
	 * do utilizador.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void closeFavoritos(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			backgroundFavoritos.setVisible(false);
		}
	}
}
