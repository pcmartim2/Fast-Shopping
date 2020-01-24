package pt.iade.fastShopping.models.daos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.iade.fastShopping.models.Produto;

public class ProdutoDAO {

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
				
			PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("INSERT INTO Produto (NomeProduto, Imagem, Preco) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			statement2.setString(1, nome);
			statement2.setBinaryStream(2, image2);
			statement2.setString(3, preco);
			statement2.executeUpdate();
				
			ResultSet rs = statement2.getGeneratedKeys();
			if (rs.next()) {
					
				PreparedStatement statement5 = DBConnector.getConnection().prepareStatement("SELECT IdCategoria FROM Categoria WHERE nomeCategoria = ?");
				statement5.setString(1, categoria);
				ResultSet results = statement5.executeQuery();
				if (results.next()) {
					int idCategoria = results.getInt(1);
					PreparedStatement statement3 = DBConnector.getConnection().prepareStatement("INSERT INTO Produto_has_Categoria (Categoria_IdCategoria, Produto_IdProduto) VALUES (?,?)");
					int idProduto = rs.getInt(1);
					statement3.setInt(1, idCategoria);
					statement3.setInt(2, idProduto);
					statement3.executeUpdate();
						
					PreparedStatement statement4 = DBConnector.getConnection().prepareStatement("INSERT INTO Loja_has_Produto (Loja_IdLoja, Produto_IdProduto) VALUES (?,?)");
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
	 * Metodo que vai a base de dados ver todos os produtos de uma certa loja e categoria 
	 * e vai colocar os produtos numa observableList
	 * @param idLoja id da loja
	 * @param categoria categoria do produto
	 * @return observableList de Produtos
	 */
	public static ObservableList<Produto> loadProdutosCategoria(int idLoja, String categoria) {	
		ObservableList<Produto> produtos = FXCollections.observableArrayList();
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("select IdProduto, NomeProduto, P.Imagem, C.nomeCategoria, Preco from Produto P, Produto_has_Categoria H, Loja L, Loja_has_Produto A, Categoria C where L.IdLoja = ? and A.Loja_IdLoja = ? and P.IdProduto = H.Produto_IdProduto and P.IdProduto = A.Produto_IdProduto and C.nomeCategoria = ? and H.Categoria_IdCategoria = C.IDCategoria");
			statement.setInt(1, idLoja);
			statement.setInt(2, idLoja);
			statement.setString(3, categoria);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int idProduto = results.getInt(1);
				String nomeProduto = results.getString(2);
				InputStream imagem = results.getBinaryStream(3);
				String categoriaProduto = results.getString(4);
				Double precoProduto = results.getDouble(5);

				byte[] bytesImagem = ImagemDAO.getBytesFromInputStream(imagem);

				produtos.add(new Produto(idProduto, bytesImagem, nomeProduto, categoriaProduto, precoProduto));
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
		return produtos;
	}
	
	/**
	 * Metodo que vai a base de dados ver todos os produtos de uma certa loja
	 * e vai colocar os produtos numa observableList
	 * @param idLoja id da loja
	 * @return observableList de produtos
	 */
	public static ObservableList<Produto> loadProdutosLoja(int idLoja) {	
		ObservableList<Produto> produtos = FXCollections.observableArrayList();
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("select IdProduto, NomeProduto, P.Imagem, C.nomeCategoria, Preco from Produto P, Produto_has_Categoria H, Loja L, Loja_has_Produto A, Categoria C where L.IdLoja = ? and A.Loja_IdLoja = ? and P.IdProduto = H.Produto_IdProduto and P.IdProduto = A.Produto_IdProduto and H.Categoria_IdCategoria = C.IDCategoria");
			statement.setInt(1, idLoja);
			statement.setInt(2, idLoja);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int idProduto = results.getInt(1);
				String nomeProduto = results.getString(2);
				InputStream imagem = results.getBinaryStream(3);
				String categoriaProduto = results.getString(4);
				Double precoProduto = results.getDouble(5);

				byte[] bytesImagem = ImagemDAO.getBytesFromInputStream(imagem);

				produtos.add(new Produto(idProduto, bytesImagem, nomeProduto, categoriaProduto, precoProduto));
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
		return produtos;
	}
	
	public static void deleteProdutoLoja(int idLoja, int idProduto) {
		
		try {
			PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("DELETE FROM Loja_has_Produto WHERE Loja_IdLoja = ? and Produto_IdProduto = ?");
			statement2.setInt(1, idLoja);
			statement2.setInt(2, idProduto);
			statement2.executeUpdate();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
	}

	/**
	 * Metodo para ver quais os produtos que a loja tem e ver as categorias dos produtos
	 * e vai adicionar numa arraylist todos as categorias sem estarem repetidas.
	 * @param idLoja id da loja
	 * @return ArrayList de categorias de produtos
	 */
	public static ArrayList<String> getCategoriasProdutosLoja(int idLoja) {	
		ArrayList<String> cateogirasProdutos = new ArrayList<String>();
		try {

			PreparedStatement statement = DBConnector.getConnection().prepareStatement("select distinct nomeCategoria from Categoria C, Loja L, Loja_has_Produto P, Produto_has_Categoria A, Produto K where L.IdLoja = ? and P.Loja_IdLoja = ? and C.IdCategoria = A.Categoria_IdCategoria and K.IdProduto = P.Produto_IdProduto and K.IdProduto = A.Produto_IdProduto");
			statement.setInt(1, idLoja);
			statement.setInt(2, idLoja);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String categoria = results.getString(1);
				cateogirasProdutos.add(categoria);
			}
			statement.close();
			results.close();

		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return cateogirasProdutos;
	}
	
	/**
	 * Metodo que vai a base de dados ver todos as categorias que estão na base de dados 
	 * e vai colocar numa observableList.
	 * @return ObservableList de categorias
	 */
	public static ObservableList<String> getCategoriasProdutos() {
		ObservableList<String> categoriasProdutos = FXCollections.observableArrayList();
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT nomeCategoria FROM Categoria");
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String nomeCategoria = results.getString(1);
				categoriasProdutos.add(nomeCategoria);
			}
			statement.close();
			results.close();
		}
		catch (SQLException ev) {
			ev.printStackTrace();
		}
		return categoriasProdutos;
	}
}
