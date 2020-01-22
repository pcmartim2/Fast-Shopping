package pt.iade.fastShopping.controllers;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pt.iade.fastShopping.models.Produto;
import pt.iade.fastShopping.models.daos.DBConnector;
import pt.iade.fastShopping.models.daos.ProdutoDAO;

public class LojaProdutosController {
	
	
	@FXML
    private ListView<Produto> listViewProdutos;
	
	@FXML
    private ListView<String> listViewCompras;
	
	@FXML
    private ComboBox<String> cbCategoria;
	
	@FXML
    private Label precoTotal;
	
	@FXML
    private Button btnRemover;
	
	
	/**
	 * Array onde vai ficar armazenado as categorias da loja
	 */
	private ArrayList<String> categorias = new ArrayList<String>();
	
	
	/**
	 * Quando o fxml que corresponde a este controlador vai executar este codigo quando incia.
	 * De acordo que a loja onde o utilizador estiver vai verificar que produtos a loja tem e com os produtos
	 * que a loja estiver vai adiconar essas categorias dos produtos na comboBox para ser possivel pesquisar 
	 * os produtos da loja de uma categoria em especifico.
	 * Vai verificar se a categoria da comboBox foi alterado para outra, se sim vai atualizar a listview dos produtos para 
	 * a categoria selecionada.
	 */
	@FXML
	private void initialize() {
		
		//Metodo para remover item do carrinho de compras e subtrair o valor do produto no total
		Produto.removeItem(listViewCompras, btnRemover, precoTotal);
		
		//Vai verificar se foi alreado a categoria no ComboBox, se sim vai ter de atualizar a listView correspondente a categoria selecionada.
		cbCategoria.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				//Vai saber qual a categoria que o utilizador selecionou
				String categoria = cbCategoria.getSelectionModel().getSelectedItem();
				
				//Vai Adicionar os produtos na loja de uma certa categoria
				Produto.getProdutos(MapaScreenController.idLoja, listViewProdutos, listViewCompras, categoria, precoTotal);
	    		
				
			}
			
		});
		
		//ver os produtos que a loja tem para ver que categorias precisa de colocar na loja
		ProdutoDAO.getCategoriasProdutosLoja(MapaScreenController.idLoja, categorias);
		
		//Adicionar as Categorias na comboBox da Categoria
    	for (String categoria : categorias) {
    		if (categorias.contains(categoria)) {
    			cbCategoria.getItems().add(categoria);
    		}
    	}
		
    	//Verificar se o ComboBox está vazio
    	if (!cbCategoria.getItems().isEmpty()) {
    		//Selecionar sempre o primeiro da lista 
    		cbCategoria.getSelectionModel().select(0);
    		//Se nao estiver vazio vai ver qual a categoria que está selecionada
    		String categoria = cbCategoria.getSelectionModel().getSelectedItem();
    		//E vai mostrar os produtos de acoordo com a categoria
    		Produto.getProdutos(MapaScreenController.idLoja, listViewProdutos, listViewCompras, categoria, precoTotal);
    	}
		
	}
}
