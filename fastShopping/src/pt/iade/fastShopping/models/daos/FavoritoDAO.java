package pt.iade.fastShopping.models.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import pt.iade.fastShopping.models.Favoritos;

public class FavoritoDAO {

	/**
	 * Metodo para adicionar um novo favorito na base de dados correspondente a uma certa loja e utlizador.
	 * @param idUser id do utilizador
	 * @param idLoja id da loja
	 */
	public static void addFavoritoUtilizador(int idUser, int idLoja) {
		
		try {
			PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("INSERT INTO Favoritos (fav_IdUtilizador, fav_IdLoja) VALUES (?,?)");
			statement2.setInt(1, idUser);
			statement2.setInt(2, idLoja);
			statement2.executeUpdate();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}
	
	/**
	 * Metodo para remover um certo favorito da base de dados, de acordo com id do utilizador e id da loja
	 * @param idUser id do utilizador
	 * @param idLoja id da loja
	 */
	public static void removeFavoritoUtilizador(int idUser, int idLoja) {

		try {
			PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("DELETE FROM Favoritos WHERE fav_IdLoja = '"+idLoja+"' and fav_IdUtilizador = '"+idUser+"'");
			statement2.executeUpdate();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}
	
	/**
	 * Metodo para mostrar todos os favoritos de um certo utilizador na listview dos favoritos
	 * @param idUser id do utilizador
	 * @param viewFavoritos lista dos favoritos
	 */
	public static ObservableList<Favoritos> getFavoritosUtilizador(int idUser) {
		ObservableList<Favoritos> favoritosUtilizador = FXCollections.observableArrayList();
		//viewFavoritos.getItems().clear();
		
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT L.IdLoja, NomeLoja FROM Favoritos F, Loja L WHERE L.IdLoja = F.fav_IdLoja and fav_IdUtilizador = '"+idUser+"'");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int idLoja = results.getInt(1);
				String nomeLoja = results.getString(2);
				//viewFavoritos.getItems().add(nomeLoja);
				favoritosUtilizador.add(new Favoritos(idLoja, nomeLoja));
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return favoritosUtilizador;
	}
	
	/**
	 * Metodo para verificar se o utilizador é favorito numa certa loja
	 * @param idLoja id da loja
	 * @param idUser id do utilizador
	 * @return true ou false respetivamente se o utilizador for favorito da loja
	 */
	public static boolean getFavoritoLoja(int idLoja, int idUser) {
		boolean fav = false;
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT fav_IdLoja FROM Favoritos WHERE fav_IdUtilizador = '"+idUser+"' and fav_IdLoja = '"+idLoja+"'");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				fav = true;
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return fav;
	}
}
