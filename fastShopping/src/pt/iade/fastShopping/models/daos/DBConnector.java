package pt.iade.fastShopping.models.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Nesta classe vão estar todo de necessário para fazer a ligação com a respetiva base de dados.
 * 
 */
public class DBConnector {

	
	private static Connection connection = null;
	static Statement statement;
	
	public String host, database, username, password;
	public int port;
	
	/**
	 * Metodo para fazer conexao com a base de dados
	 */
	public static void abrirConexao() {
		String host = "remotemysql.com";
		String database = "EPbc5S4VLS";
		String username = "EPbc5S4VLS";
		String password = "Xc3q9KZz8D";
		Integer port = 3306;
		
		String URL = "jdbc:mysql://" + host + ":" + port + "/" + database;
		
		try {
			//Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
}
