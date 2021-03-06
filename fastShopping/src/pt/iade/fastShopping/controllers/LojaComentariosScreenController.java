package pt.iade.fastShopping.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pt.iade.fastShopping.models.Comentarios;
import pt.iade.fastShopping.models.Popups;
import pt.iade.fastShopping.models.daos.ComentarioDAO;


/**
 * Nesta classe as funcionalidades relacionadas aos coment�rios da loja v�o estar aqui.
 * Assim que o utilizador clica para ver os coment�rios de uma certa loja, a aplica��o 
 * vai a base de dados ver todos os coment�rios da respetiva loja.
 * O utilizador tamb�m pode adicionar um novo coment�rio na loja clicando no bot�o mais, 
 * onde vai aparecer um campo para colocar o seu coment�rio, ao clicar no adiconar vai verificar
 * se o campo est� vazio, se n�o estiver vai adicionar um novo coment�rio na loja.
 */
public class LojaComentariosScreenController {
	
	/**
	 * Listview de todos os comentarios da loja
	 */
	@FXML
	private ListView<Comentarios> comentarioList;

	/**
	 * Zona para adicionar um novo comentario
	 */
	@FXML
	private AnchorPane backgroudEnviarComentario;

	/**
	 * Zona onde o utilizador coloca o comentario
	 */
	@FXML
	private TextArea textAreaComentario;

	
	/**
	 * Quando o fxml que corresponde a este controlador vai executar este codigo quando incia.
	 * Vai colocar os comentarios correspondentes a loja em que o utilizador est� que estao na base de dados 
	 * para a listview.
	 */
	@FXML
	private void initialize() {
		
		//Vai colocar os comentarios existentes da base de dados da loja que o utilizador estiver na listview
		listViewComentarios();
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
	 * Vai verificar se a area do comentario nao est� vazia, se nao estiver vazia
	 * vai enviar para a base de dados. 
	 * Vai atualizar a listview dos comentarios para aparecer o novo comentario adiconado e 
	 * vai aparecer uma mensagem que consegiu enviar o comentario com sucesso.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void enviarComentario(ActionEvent event) {

		// Se a area do comentario nao estiver vazia vai adicionar o coment�rio.
		if (!textAreaComentario.getText().isEmpty()) {

			//Envia o novo comentario para a base de dados
			ComentarioDAO.addComentario(textAreaComentario.getText(), MapaScreenController.idLoja, LoginUtilizadorController.IdUser);
			
			backgroudEnviarComentario.setVisible(false);
			
			//Atualizar listview comentarios
			listViewComentarios();
			
			
			//Mensagem que consegiu enviar o comentario
			Popups.dialogInformation("Comentario enviado!", "O comentario foi enviado com sucesso!");

		}
		// Se estiver vazio vai mostrar um erro!
		else {
			Popups.dialogError("Erro ao enviar comentario!", "Por preencha o texto!");
		}
	}
	
	/**
	 * Atualizar ListView com os comentarios
	 * Metodo criado para n�o haver codigo duplicado
	 */
	private void listViewComentarios() {
		comentarioList.getItems().clear();
		//Vai colocar os comentarios existentes da base de dados da loja que o utilizador estiver na listview
		comentarioList.getItems().addAll(ComentarioDAO.getComentariosLoja(MapaScreenController.idLoja));
		
		//configurar como a exibi��o em lista � exibida, ou seja vai s� aparecer o 
		//nome do utilizador e o respetivo comentario
		comentarioList.setCellFactory(lv -> new ListCell<Comentarios>() {
		    @Override
		    public void updateItem(Comentarios comentario, boolean empty) {
		        super.updateItem(comentario, empty) ;
		        setText(empty ? null : comentario.getNomeUtilizador() + "\n" + comentario.getComentario());
		    }
		});
	}

}
