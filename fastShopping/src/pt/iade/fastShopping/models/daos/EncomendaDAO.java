package pt.iade.fastShopping.models.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EncomendaDAO {
	
	/**
	 * Este metodo vai servir para saber o ultimo numero de encomenda que está na base de dados.
	 * @return vai devolver o ultimo numero de encomenda
	 */
	public static int maxNumEncomenda() {
		int maxNumEncomenda = 0;
		try {
			PreparedStatement statement = DBConnector.getConnection().prepareStatement("SELECT max(NumEncomenda) from Encomenda");
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				
				maxNumEncomenda = (rs.getInt(1) + 1);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return maxNumEncomenda;
		
	}
	
	/**
	 * Este metodo vai adicionar os novos produtos da nova encomenda na base de dados.
	 * @param maxNumEncomenda numero Encomendo
	 * @param idProduto id do produto
	 * @param quantidadeProduto quantidade do produto
	 * @param idLoja id da loja
	 * @param idUtilizador id do utilizador
	 */
	public static void addEncomenda(int maxNumEncomenda, int idProduto, int quantidadeProduto, int idLoja, int idUtilizador) {
		try {
			
			PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("INSERT INTO Encomenda (NumEncomenda, Produto_IdProduto, QuantidadeProduto, Loja_IdLoja, Utilizador_IdUtilizador, DataEncomenda, DataEntrega) VALUES (?,?,?,?,?,now(),date_add(now(), interval 7 day))");
			statement2.setInt(1, maxNumEncomenda);
			statement2.setInt(2, idProduto);
			statement2.setInt(3, quantidadeProduto);
			statement2.setInt(4, idLoja);
			statement2.setInt(5, idUtilizador);
			statement2.executeUpdate();	
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo vai devolver a data de entrega de um respetivo numero de encomenda.
	 * @param numEncomenda
	 * @return vai devolver a data de entrega da encomenda
	 */
	public static String dataEntregaEncomenda(int numEncomenda) {
		String dataEntrega = "";
		try {
			
			PreparedStatement statement2 = DBConnector.getConnection().prepareStatement("select distinct DataEntrega from Encomenda where NumEncomenda = ?");
			statement2.setInt(1, numEncomenda);
			ResultSet results = statement2.executeQuery();
			if (results.next()) {
				dataEntrega = results.getString(1);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return dataEntrega;
		
	}
}
