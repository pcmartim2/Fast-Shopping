package pt.iade.fastShopping.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pt.iade.fastShopping.models.daos.ComentarioDAO;
import pt.iade.fastShopping.models.daos.DBConnector;

public class LojaComentariosScreenController {

	@FXML
	private ListView<String> comentarioList;

	@FXML
	private AnchorPane backgroudEnviarComentario;

	@FXML
	private TextArea textAreaComentario;

	
	/**
	 * Quando o fxml que corresponde a este controlador vai executar este codigo quando incia.
	 * Vai colocar os comentarios correspondentes a loja em que o utilizador está que estao na base de dados 
	 * para a listview.
	 */
	@FXML
	private void initialize() {
		//Vai colocar os comentarios existentes da base de dados da loja que o utilizador estiver na listview
		ComentarioDAO.getComentariosLoja(MapaScreenController.idLoja, comentarioList);
		
	}

	/**
	 * Metodo de quando o utilizador clica no button de que tem um "+" 
	 * Vai abrir um menu para o utilizador preencher com o seu comentario.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void addComentario(MouseEvent event) {
		backgroudEnviarComentario.toFront();
		backgroudEnviarComentario.setVisible(true);
	}

	/**
	 * Metodo de quando o utlizador clica no button para fechar o menu dos comentarios
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void closeComentarios(MouseEvent event) {
		
		backgroudEnviarComentario.setVisible(false);
	}

	/**
	 * Metodo de quando o utilizador clica no button para adicionar um novo comentario na loja
	 * Vai verificar se a area do comentario nao está vazia, se nao estiver vazia
	 * vai enviar para a base de dados. 
	 * Vai atualizar a listview dos comentarios para aparecer o novo comentario adiconado e 
	 * vai aparecer uma mensagem que consegiu enviar o comentario com sucesso.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void enviarComentario(ActionEvent event) {

		// Se a area do comentario nao estiver vazia vai adicionar o comentário.
		if (!textAreaComentario.getText().isEmpty()) {

			//Envia o novo comentario para a base de dados
			ComentarioDAO.addComentario(textAreaComentario.getText(), MapaScreenController.idLoja, LoginUtilizadorController.IdUser);
			
			backgroudEnviarComentario.setVisible(false);
			
			//Vai atualizar a listview com o comentario
			ComentarioDAO.getComentariosLoja(MapaScreenController.idLoja, comentarioList);
			//Mensagem que consegiu enviar o comentario
			SidebarLojaController.warningDialog("Comentario enviado!", "O comentario foi enviado com sucesso!");

		}
		// Se estiver vazio vai mostrar um erro!
		else {
			SidebarLojaController.warningDialog("Erro ao enviar comentario!", "Por preencha o texto!");
		}
	}

}
