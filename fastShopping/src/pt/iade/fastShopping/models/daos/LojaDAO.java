package pt.iade.fastShopping.models.daos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LojaDAO {

	/**
	 * Metodo que vai a tabela Loja na base de dados e vai buscar todas as coordenadas que
	 * estao e cada coordenada corresponde a uma loja que vai adicionar as lojas no mapa.
	 * @param root mapa
	 */
	public static void loadLojasMapa(AnchorPane root) {
		
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT Coordenadas FROM Loja");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String coordenadas = results.getString(1);
				//Vai tirar o # das coordenadas para sabermos a coordenada X e Y
				String[] split = coordenadas.split("#");
				double coordx = Double.parseDouble(split[0]);
				double coordy = Double.parseDouble(split[1]);
				
				//Desenhar as Lojas no mapa
				Circle circle = new Circle(1, 1, 7, Color.RED);
				circle.setStroke(Color.BLACK);
				circle.setStrokeWidth(1);
				circle.setLayoutX(coordx);
				circle.setLayoutY(coordy);
				root.getChildren().add(circle);
				
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}
	
	/**
	 * Metodo que vai adicionar uma nova loja na tabela Loja na base de dados com o respetivo
	 * nome, imagem, coordenadas, proprietario e estilo da loja.
	 * @param nome da  loja
	 * @param imagem da loja
	 * @param coord x e y da loja
	 * @param proprietario da loja
	 * @param estilo da loja
	 */
	public static void addLoja(String nome, byte[] imagem, String coord, String proprietario, String estilo) {
		InputStream image2 = new ByteArrayInputStream(imagem);
		try {
			//Vai verificar se o nome já existe na base de dados
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT * FROM Loja WHERE NomeLoja=?");
			statement.setString(1, nome);
			ResultSet results = statement.executeQuery();
			//se nao existir vai adiconar na base de dados
			if (!results.next()) {
				PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("INSERT INTO Loja (NomeLoja, Coordenadas, Imagem, Proprietario) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				statement2.setString(1, nome);
				statement2.setString(2, coord);
				statement2.setBinaryStream(3, image2);
				statement2.setString(4, proprietario);
				statement2.executeUpdate();
					
				ResultSet rs = statement2.getGeneratedKeys();
				if (rs.next()) {
					PreparedStatement statement3 = DBConnector.getConnection().prepareStatement("INSERT INTO Loja_has_Estilo (Loja_IdLoja, Estilo_IdEstilo) VALUES (?,?)");
					int idLoja = rs.getInt(1);
					statement3.setInt(1, idLoja);
					statement3.setInt(2, getIDEstilo(estilo));
					statement3.executeUpdate();
				}
				
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo para saber o id da loja atraves do nome da loja
	 * @param nomeLoja nome da Loja
	 * @return id da loja
	 */
	public static int getIdLoja(String nomeLoja) {
		int idLoja = 0;
		try {
			//Vai verificar se o nome já existe na base de dados
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT IdLoja FROM Loja WHERE NomeLoja = '"+nomeLoja+"'");
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				idLoja = results.getInt(1);
				
			}
			statement.close();
			results.close();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return idLoja;
	}

	/**
	 * Metodo para o id do estilo atraves do nome do estilo
	 * @param nomeEstilo nome do estilo
	 * @return id do estilo
	 */
	public static int getIDEstilo(String nomeEstilo) {	
		int idEstilo = 0;
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT IdEstilo FROM EstiloLoja WHERE NomeEstilo = '"+ nomeEstilo+"'");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				idEstilo = results.getInt(1);
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return idEstilo;
	}
	
	/**
	 * Metodos para adicionar os tipos de lojas na comboBox dos estilos
	 * @param estilo_Loja comboBox dos estilos
	 */
	public static void loadEstilosLojas(ChoiceBox<String> estilo_Loja) {
		try {
			Statement st = DBConnector.getConnection().createStatement();
			ResultSet res = st.executeQuery("SELECT NomeEstilo FROM EstiloLoja");
			while (res.next()) {
				String estilo = res.getString(1);
				estilo_Loja.getItems().add(estilo);
			}
			st.close();
			res.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 /**
	 * Metodo para ir buscar a imagem da loja atraves do id da loja
	 * @param idLoja id da loja
	 * @return iamgem em byte array
	 * @throws IOException exception
	 */
	public static byte[] getImagemLoja(int idLoja) throws IOException {
		byte[] bytesImagem = null;
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT Imagem FROM Loja");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				InputStream imagemLoja = results.getBinaryStream(1);
    			
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return bytesImagem;
	} 
	
	/**
	 * Metodo para saber o id da loja atraves do nome do proprietario
	 * @param proprietario nome do prorietario
	 * @return id da loja do proprietario
	 */
	public static int getLojaId(String proprietario) {
		int idLoja = 0;
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT IdLoja FROM Loja WHERE Proprietario = '"+proprietario+"'");
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				idLoja = results.getInt(1);
			}
			statement.close();
			results.close();
		
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return idLoja;
	}
	
	/**
	 * Metodo para verificar se o utilizador é proprietario de alguma loja
	 * @param proprietario nome do utilizador
	 * @return true ou false respetivamente se o utilizador tiver alguma loja
	 */
	public static boolean verificarDonoLoja(String proprietario) {
		boolean loja = false;
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT IdLoja FROM Loja WHERE Proprietario = '"+proprietario+"'");
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				loja = true;
			}
			statement.close();
			results.close();
			
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return loja;
	}
}
