package pt.iade.fastShopping.models.daos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import pt.iade.fastShopping.models.Produto;

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
	 * Metodo que vai a tabela Loja na base de dados e vai buscar todas as coordenadas que
	 * estao e cada coordenada corresponde a uma loja que vai adicionar as lojas no mapa.
	 * @param root mapa
	 */
	public static void loadLojasMapa(AnchorPane root) {
		
		try {
			PreparedStatement statement = getConnection().prepareStatement("SELECT Coordenadas FROM Loja");
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
			PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM Loja WHERE NomeLoja=?");
			statement.setString(1, nome);
			ResultSet results = statement.executeQuery();
			//se nao existir vai adiconar na base de dados
			if (!results.next()) {
				PreparedStatement statement2 = getConnection().prepareStatement("INSERT INTO Loja (NomeLoja, Coordenadas, Imagem, Proprietario) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				statement2.setString(1, nome);
				statement2.setString(2, coord);
				statement2.setBinaryStream(3, image2);
				statement2.setString(4, proprietario);
				statement2.executeUpdate();
					
				ResultSet rs = statement2.getGeneratedKeys();
				if (rs.next()) {
					PreparedStatement statement3 = getConnection().prepareStatement("INSERT INTO Loja_has_Estilo (Loja_IdLoja, Estilo_IdEstilo) VALUES (?,?)");
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
	 * Metodo que vai adicionar um novo produto na tabela Produto na base de dados com o respetiva
	 * imagem, nome, categoria, preco e lojaid.
	 * @param imagem do produto
	 * @param nome do produto
	 * @param categoria do produto
	 * @param preco do produto
	 * @param lojaId onde o produto vai ser adiconado
	 */
	public static void addProduto(byte[] imagem, String nome, String categoria, String preco, int lojaId) {
		InputStream image2 = new ByteArrayInputStream(imagem);
		try {
				
			PreparedStatement statement2 = getConnection().prepareStatement("INSERT INTO Produto (NomeProduto, Imagem, Preco) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			statement2.setString(1, nome);
			statement2.setBinaryStream(2, image2);
			statement2.setString(3, preco);
			statement2.executeUpdate();
				
			ResultSet rs = statement2.getGeneratedKeys();
			if (rs.next()) {
					
				PreparedStatement statement5 = getConnection().prepareStatement("SELECT IdCategoria FROM Categoria WHERE nomeCategoria = '"+categoria+"'");
				ResultSet results = statement5.executeQuery();
				if (results.next()) {
					int idCategoria = results.getInt(1);
					PreparedStatement statement3 = getConnection().prepareStatement("INSERT INTO Produto_has_Categoria (Categoria_IdCategoria, Produto_IdProduto) VALUES (?,?)");
					int idProduto = rs.getInt(1);
					statement3.setInt(1, idCategoria);
					statement3.setInt(2, idProduto);
					statement3.executeUpdate();
						
					PreparedStatement statement4 = getConnection().prepareStatement("INSERT INTO Loja_has_Produto (Loja_IdLoja, Produto_IdProduto) VALUES (?,?)");
					statement4.setInt(1, lojaId);
					statement4.setInt(2, idProduto);
					statement4.executeUpdate();
				}
			}
				

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo para guardar os produtos de uma certa loja e categoria no array dos produtos
	 * @param idLoja id da loja
	 * @param categoria categoria do produto
	 */
	public static void loadProdutosCategoria(int idLoja, String categoria) {	
		try {
			PreparedStatement statement = getConnection().prepareStatement("select IdProduto, NomeProduto, P.Imagem, C.nomeCategoria, Preco from Produto P, Produto_has_Categoria H, Loja L, Loja_has_Produto A, Categoria C where L.IdLoja = '"+idLoja+"' and A.Loja_IdLoja = '"+idLoja+"' and P.IdProduto = H.Produto_IdProduto and P.IdProduto = A.Produto_IdProduto and C.nomeCategoria = '"+categoria+"' and H.Categoria_IdCategoria = C.IDCategoria");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int idProduto = results.getInt(1);
				String nomeProduto = results.getString(2);
				InputStream imagem = results.getBinaryStream(3);
				String categoriaProduto = results.getString(4);
				Double precoProduto = results.getDouble(5);

				byte[] bytesImagem = getBytesFromInputStream(imagem);

				Produto.produtoCache.add(new Produto(idProduto, bytesImagem, nomeProduto, categoriaProduto, precoProduto));
			}
			statement.close();
			results.close();

		}
		catch (SQLException ev) {
			ev.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para ver quais os produtos que a loja tem e ver as categorias dos produtos para adicionar na comboBox
	 * @param idLoja id da loja
	 * @param categorias comboBox onde vai ser adicionado as categorias que a loja tem
	 */
	public static void getCategoriasProdutosLoja(int idLoja, ArrayList<String> categorias) {	
		try {

			PreparedStatement statement = getConnection().prepareStatement("select distinct nomeCategoria from Categoria C, Loja L, Loja_has_Produto P, Produto_has_Categoria A, Produto K where L.IdLoja = '"+idLoja+"' and P.Loja_IdLoja = '"+idLoja+"' and C.IdCategoria = A.Categoria_IdCategoria and K.IdProduto = P.Produto_IdProduto and K.IdProduto = A.Produto_IdProduto");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String categoria = results.getString(1);
				categorias.add(categoria);
			}
			statement.close();
			results.close();

		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}

	/**
	 * Metodo para o id do estilo atraves do nome do estilo
	 * @param nomeEstilo nome do estilo
	 * @return id do estilo
	 */
	public static int getIDEstilo(String nomeEstilo) {	
		int idEstilo = 0;
		try {
			PreparedStatement statement = getConnection().prepareStatement("SELECT IdEstilo FROM EstiloLoja WHERE NomeEstilo = '"+ nomeEstilo+"'");
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
	 * Metodo para saber o id da loja atraves do nome do proprietario
	 * @param proprietario nome do prorietario
	 * @return id da loja do proprietario
	 */
	public static int getLojaId(String proprietario) {
		int idLoja = 0;
		try {
			PreparedStatement statement = getConnection().prepareStatement("SELECT IdLoja FROM Loja WHERE Proprietario = '"+proprietario+"'");
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
			PreparedStatement statement = getConnection().prepareStatement("SELECT IdLoja FROM Loja WHERE Proprietario = '"+proprietario+"'");
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
		
	/**
	 * Metodo para verificar se o utilziador existe na base de dados
	 * @param utilizador nome do utilizador
	 * @return true ou false respetivamente se o o nome de utlizador ja estiver registado na nossa aplicacao
	 */
	public static boolean verificarUtilizador(String utilizador) {
		boolean verificar = false;
		try {
			//Vai verificar se o nome já existe na base de dados
			PreparedStatement statement = getConnection().prepareStatement("SELECT IdUtilizador FROM Utilizador WHERE NomeUtilizador = '"+utilizador+"'");
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
			PreparedStatement statement2 = getConnection().prepareStatement("INSERT INTO Utilizador (NomeUtilizador) VALUES (?)");
			statement2.setString(1, utilizador);
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
			PreparedStatement statement = getConnection().prepareStatement("SELECT IdUtilizador FROM Utilizador WHERE NomeUtilizador = '"+nomeUser+"'");
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
	 * Metodo para adicionar as categorias dos produtos na comboBox
	 * @param categoriaProduto comboBox das categorias do produto
	 */
	public static void getCategoriasProdutos(ChoiceBox<String> categoriaProduto) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("SELECT nomeCategoria FROM Categoria");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String nomeCategoria = results.getString(1);
				categoriaProduto.getItems().add(nomeCategoria);
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}
	 
	/**
	 * Metodo para adicionar um novo favorito na base de dados correspondente a uma certa loja e utlizador.
	 * @param idUser id do utilizador
	 * @param idLoja id da loja
	 */
	public static void addFavoritoUtilizador(int idUser, int idLoja) {
		
		try {
			PreparedStatement statement2 = getConnection().prepareStatement("INSERT INTO Favoritos (fav_IdUtilizador, fav_IdLoja) VALUES (?,?)");
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
			PreparedStatement statement2 = getConnection().prepareStatement("DELETE FROM Favoritos WHERE fav_IdLoja = '"+idLoja+"' and fav_IdUtilizador = '"+idUser+"'");
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
	public static void getFavoritosUtilizador(int idUser, ListView<String> viewFavoritos) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("SELECT NomeLoja FROM Favoritos F, Loja L WHERE L.IdLoja = F.fav_IdLoja and fav_IdUtilizador = '"+idUser+"'");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String nomeLoja = results.getString(1);
				viewFavoritos.getItems().add(nomeLoja);
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
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
			PreparedStatement statement = getConnection().prepareStatement("SELECT fav_IdLoja FROM Favoritos WHERE fav_IdUtilizador = '"+idUser+"' and fav_IdLoja = '"+idLoja+"'");
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
	
	/**
	 * Metodo para adicionar um novo comentario na base de dados
	 * @param texto comentario
	 * @param idLoja id da loja
	 * @param idUtilizador id do utilizador
	 */
	public static void addComentario(String texto, int idLoja, int idUtilizador) {
		try {
			PreparedStatement statement2 = getConnection().prepareStatement("INSERT INTO Comentario (Texto, Loja_IdLoja, IdUtilizador) VALUES (?,?,?)");
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
			PreparedStatement statement = getConnection().prepareStatement("SELECT C.Texto, U.NomeUtilizador FROM Comentario C, Utilizador U WHERE Loja_IdLoja = '"+idLoja+"' and C.IdUtilizador = U.IdUtilizador");
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
			PreparedStatement statement = getConnection().prepareStatement("SELECT Imagem FROM Loja");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				InputStream imagemLoja = results.getBinaryStream(1);
    			bytesImagem = getBytesFromInputStream(imagemLoja);
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
