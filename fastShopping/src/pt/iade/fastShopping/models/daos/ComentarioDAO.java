package pt.iade.fastShopping.models.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.ListView;

public class ComentarioDAO {

	/**
	 * Metodo para adicionar um novo comentario na base de dados
	 * @param texto comentario
	 * @param idLoja id da loja
	 * @param idUtilizador id do utilizador
	 */
	public static void addComentario(String texto, int idLoja, int idUtilizador) {
		try {
			PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("INSERT INTO Comentario (Texto, Loja_IdLoja, Utilizador_IdUtilizador) VALUES (?,?,?)");
			statement2.setString(1, texto);
			statement2.setInt(2, idLoja);
			statement2.setInt(3, idUtilizador);
			statement2.executeUpdate();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}
	
	/**
	 * Metodo para colocar todos os comentarios na listview de uma certa loja
	 * @param idLoja id da loja
	 * @param listaComentarios lista dos comentarios
	 */
	public static void getComentariosLoja(int idLoja, ListView<String> listaComentarios) {
		listaComentarios.getItems().clear();
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT C.Texto, U.NomeUtilizador FROM Comentario C, Utilizador U WHERE Loja_IdLoja = '"+idLoja+"' and C.Utilizador_IdUtilizador = U.IdUtilizador");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String texto = results.getString(1);
				String nomeUtilizador = results.getString(2);
				listaComentarios.getItems().add(nomeUtilizador + "\n" + texto);
				
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}
}
