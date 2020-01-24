package pt.iade.fastShopping.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pt.iade.fastShopping.WindowManager;
import pt.iade.fastShopping.models.daos.LoginRegistrarDAO;
import pt.iade.fastShopping.models.daos.LojaDAO;

public class LoginUtilizadorController {

	/**
	 * Variavel onde vai ficar armazenado o nome do utilizador
	 */
	public static String nome_UtilizadorLabel = null;
	/**
	 * Variavel onde vai ficar armazenado o id do utilizador
	 */
	public static int IdUser = 0;
	
	/**
	 * Variavel onde vai ficar armazenado o id da loja em que o proprietario e dono.
	 */
	public static int lojaID = 0;

	@FXML
	private TextField nome_Utilizador;

	
	/**
	 * Metodo quando o utilizador clicar no button vai fazer o login na sua conta.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void aoEntrar(ActionEvent event) {
		// login ao carregar botão
		Login();
	}
	
	/**
	 * Metodo quando o utilizador clica numa tecla no teclado e vai 
	 * verificar se é tecla Enter se for, vai fazer login na sua conta.
	 * @param event quando o utilizador clica numa tecla do teclado
	 */
	@FXML
	void teclaEntrar(KeyEvent event) {
		// login ao pressionar enter
		if (event.getCode() == KeyCode.ENTER) {
			Login();
		}
	}
	
	/**
	 * Metodo onde vai verificar se o utilizador é um utilizador normal, admin ou proprietario de alguma loja.
	 */
	private void Login() {
		// login admin, dá acesso a criação de lojas
		if (!nome_Utilizador.getText().isEmpty()) {
			
			//Vai verificar se o utilizador já existe na base de dados
			if (LoginRegistrarDAO.verificarUtilizador(nome_Utilizador.getText())) {
				nome_UtilizadorLabel = nome_Utilizador.getText();
				//Vai converter o nome do utilizador em id
				IdUser = LoginRegistrarDAO.getIdUtilizador(nome_Utilizador.getText());
				if (LoginRegistrarDAO.verificarAdmin(nome_Utilizador.getText())) {
					WindowManager.openAdminWindow();
				}
				// login do proprietario da loja
				else if (LojaDAO.verificarDonoLoja(nome_Utilizador.getText())) {
					lojaID = LojaDAO.getLojaId(nome_Utilizador.getText());
					WindowManager.openLojaAddProdutosWindow();
				}
				
				
				// login normal
				else {
					WindowManager.openMainWindow();
				}
			}
			else {
				//Se nao existir vai criar um novo utilizador na base de dados
				LoginRegistrarDAO.addUtilizador(nome_Utilizador.getText());
				Login();
			}
		}
	}

}
