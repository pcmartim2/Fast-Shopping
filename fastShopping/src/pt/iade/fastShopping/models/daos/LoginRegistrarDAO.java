package pt.iade.fastShopping.models.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Nesta classe vão estar todos os metodos(queries) relacionados com o Login e Registo.
 * Tem os seguintes metodos:
 * Verificar se o utilizador já existe na base de dados
 * adicionar um novo utilizador na base de dados
 * ver o id de um respetivo utilizador
 * verificar se o utilizador é admin
 * 
 */
public class LoginRegistrarDAO {

	/**
	 * Metodo para verificar se o utilziador existe na base de dados
	 * @param utilizador nome do utilizador
	 * @return true ou false respetivamente se o o nome de utlizador ja estiver registado na nossa aplicacao
	 */
	public static boolean verificarUtilizador(String utilizador) {
		boolean verificar = false;
		try {
			//Vai verificar se o nome já existe na base de dados
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT IdUtilizador FROM Utilizador WHERE NomeUtilizador = ?");
			statement.setString(1, utilizador);
			ResultSet results = statement.executeQuery();
			//se o utilizador existir na base dedados 
			if (results.next()) {

				verificar = true;
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return verificar;
	}
	
	/**
	 * Vai registrar o nome utilizador na nossa aplicacao, ou seja vai adicionar o novo utilizador
	 * na base de dados.
	 * @param utilizador nome do utilizador
	 */
	public static void addUtilizador(String utilizador) {
		
		try {
			PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("INSERT INTO Utilizador (NomeUtilizador, isAdmin) VALUES (?,?)");
			statement2.setString(1, utilizador);
			statement2.setBoolean(2, false);
			statement2.executeUpdate();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}
	
	/**
	 * Metodo para saber o id do utilizador atraves do nome
	 * @param nomeUser nome do utilizador
	 * @return id do utilizador
	 */
	public static int getIdUtilizador(String nomeUser) {
		int idUtilizador = 0;
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT IdUtilizador FROM Utilizador WHERE NomeUtilizador = ?");
			statement.setString(1, nomeUser);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				idUtilizador = results.getInt(1);
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return idUtilizador;
	}
	
	/**
	 * Metodo para verificar se um dado nome de utilizador é admin
	 * @param nomeUtilizador nome do utilizador
	 * @return se é admin ou nao
	 */
	public static boolean verificarAdmin(String nomeUtilizador) {
		
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT isAdmin FROM Utilizador WHERE NomeUtilizador = ?");
			statement.setString(1, nomeUtilizador);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				return results.getBoolean(1);
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return false;
		
	}
	
}
