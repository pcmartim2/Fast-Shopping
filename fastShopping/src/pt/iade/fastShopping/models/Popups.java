package pt.iade.fastShopping.models;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Popups {

	/**
	 * Metodo para criar um popup de Informação
	 * @param titulo titulo da Informação
	 * @param texto texto da Informação
	 */
	public static void dialogInformation(String titulo, String texto) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(texto);

		alert.showAndWait();
	}
	
	/**
	 * Metodo para criar um popup de Aviso
	 * @param titulo titulo do Aviso
	 * @param texto texto do Aviso 
	 */
	public static void dialogWarning(String titulo, String texto) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(texto);

		alert.showAndWait();
	}
	
	/**
	 * Metodo para criar um popup de Erro
	 * @param titulo titulo do Erro
	 * @param texto texto do Erro
	 */
	public static void dialogError(String titulo, String texto) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(texto);

		alert.showAndWait();
	}
	
}
