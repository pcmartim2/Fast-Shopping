package pt.iade.fastShopping.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import pt.iade.fastShopping.WindowManager;
import pt.iade.fastShopping.models.daos.FavoritoDAO;
import pt.iade.fastShopping.models.daos.LojaDAO;


/**
 * Nesta classe as funcionalidades relacionadas ao SideBar vão estar aqui.
 * Quando um utilizador entra numa das lojas vai reparar que no lado esquerdo 
 * da aplicação vai ter um sidebar com as sequintes funcionalidades:
 * adicionar e remover favorito da loja e adicionar um novo comentário da loja.
 */
public class SidebarLojaController {
	
	
	/**
	 * Imagens das estrelas favoritos e da loja
	 */
	@FXML
    private ImageView unfav, fav, imagemLoja;

	/**
	 * borderPane
	 */
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
		
		//Vai colocar a imagem da Loja
		Image img = new Image(new ByteArrayInputStream(LojaDAO.getImagemLoja(MapaScreenController.idLoja)));
		imagemLoja.setImage(img);
		imagemLoja.setFitHeight(197);
		imagemLoja.setFitWidth(263);
		
		WindowManager.loadUI("views/LojaProdutos.fxml", new LojaProdutosController(), borderPane);

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
	 * Ao clicar no button dos comentarios no lado direito da sidebar vai abrir uma nova 
	 * pagina dos comentarios.
	 * @param event quando o utilizador clica no button
	 * @throws IOException exception
	 */
	@FXML
	void verComentarios(MouseEvent event) throws IOException {

		if (event.getButton() == MouseButton.PRIMARY) {
			WindowManager.loadUI("views/LojaComentariosScreen.fxml", new LojaComentariosScreenController(), borderPane);
		}
	}

}
