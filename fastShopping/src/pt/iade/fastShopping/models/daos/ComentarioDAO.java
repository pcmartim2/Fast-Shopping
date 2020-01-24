package pt.iade.fastShopping.models.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.iade.fastShopping.models.Comentarios;


/**
 * Nesta classe vão estar todos os metodos(queries) relacionados com os comentários.
 * Tem os seguintes metodos:
 * adicionar um novo comentario na base de dados
 * Vai a base de dados ver todos os comentarios de uma respetiva loja
 * 
 */
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
	 * Metodo que vai a base de dados ver todos os comentarios que uma certa loja tem.
	 * @param idLoja id da loja
	 * @return observableList de comentarios
	 */
	public static ObservableList<Comentarios> getComentariosLoja(int idLoja) {
		ObservableList<Comentarios> comentariosLoja = FXCollections.observableArrayList();
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT C.Texto, U.NomeUtilizador FROM Comentario C, Utilizador U WHERE Loja_IdLoja = ? and C.Utilizador_IdUtilizador = U.IdUtilizador");
			statement.setInt(1, idLoja);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String texto = results.getString(1);
				String nomeUtilizador = results.getString(2);
				comentariosLoja.add(new Comentarios(nomeUtilizador, texto));
				
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return comentariosLoja;
	}
}
