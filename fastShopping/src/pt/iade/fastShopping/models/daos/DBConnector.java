package pt.iade.fastShopping.models.daos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


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
	
	/**
	 * Metodo para converter InPutStream para byte[]
	 * @param is imagem em InPutStream
 	 * @return da imagem em byte array
	 * @throws IOException exception
	 */
	public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[0xFFFF];
		for (int len = is.read(buffer); len != -1; len = is.read(buffer)) { 
			os.write(buffer, 0, len);
		}
		return os.toByteArray();
	}
}
