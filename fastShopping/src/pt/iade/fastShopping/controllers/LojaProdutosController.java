package pt.iade.fastShopping.controllers;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import pt.iade.fastShopping.models.CarrinhoCompras;
import pt.iade.fastShopping.models.Popups;
import pt.iade.fastShopping.models.Produto;
import pt.iade.fastShopping.models.daos.EncomendaDAO;
import pt.iade.fastShopping.models.daos.ProdutoDAO;



/**
 * Nesta classe as funcionalidades relacionadas aos produtos da loja vão estar aqui.
 * Quando o utilizador clica numa certa loja que está no mapa vai conseguir ver todos os produtos
 * que a loja tem organizados por categorias com o respetivo preço.
 * O utilizador vai poder selecionar a quantidade do produto que deseja e ao clicar no botão adicionar 
 * vai colocar o produto na lista de compras, em baixo o utilizador vai conseguir ver o valor total.
 * O utilizador também poderá remover o produto da lista de compras basta clicar no botão remover, mas 
 * primeiro tem de ter um produto selecionado na lista de compras.
 */
public class LojaProdutosController {
	
	
	/**
	 * ListView de todos os produtos da loja
	 */
	@FXML
    private ListView<Produto> listViewProdutos;
	
	/**
	 * ListView do carrinho de compras da loja
	 */
	@FXML
    private ListView<CarrinhoCompras> listViewCompras;
	
	/**
	 * Selecionar a categoria para filtrar os produtos
	 */
	@FXML
    private ComboBox<String> cbCategoria;
	
	/**
	 * Soma dos preços no carrinho de compras
	 */
	@FXML
    private Label precoTotalCarrinho;
	
	/**
	 * ObservableList onde vai ficar guardado os produtos do carrinho de compras
	 */
	private ObservableList<CarrinhoCompras> carrinhoProdutos = FXCollections.observableArrayList();
	
	/**
	 * Index do produto que já existe na observableList do carrinho de compras
	 */
	private int indexProduto = 0;
	
	/**
	 * Formatar para duas casas decimais
	 */
	private DecimalFormat df2 = new DecimalFormat("#.##");
	
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
		//Produto.removeItem(listViewCompras, btnRemover, precoTotal);
		
		//ver os produtos que a loja tem para ver que categorias precisa de colocar na comboBox da loja
    	cbCategoria.getItems().addAll(ProdutoDAO.getCategoriasProdutosLoja(MapaScreenController.idLoja));
		
    	//Verificar se o ComboBox está vazio
    	if (!cbCategoria.getItems().isEmpty()) {
    		//Selecionar sempre o primeiro da lista 
    		cbCategoria.getSelectionModel().select(0);
    		//Se nao estiver vazio vai ver qual a categoria que está selecionada
    		String categoria = cbCategoria.getSelectionModel().getSelectedItem();
    		//E vai mostrar os produtos de acoordo com a categoria
    		listViewProdutos(categoria);
    	}
		
		//Vai verificar se foi alreado a categoria no ComboBox, se sim vai ter de atualizar a listView correspondente a categoria selecionada.
		cbCategoria.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				//Vai saber qual a categoria que o utilizador selecionou
				String categoria = cbCategoria.getSelectionModel().getSelectedItem();
				
				//Vai Adicionar os produtos na loja de uma certa categoria
				listViewProdutos(categoria);
			}
			
		});	
	}
	
	
	/**
	 * Metodo para colocar os produtos da loja na listview e colocar os produtos na listview 
	 * do carrinho de compras. 
	 * @param categoria categoria do produto
	 */
	private void listViewProdutos(String categoria) {
		
		listViewProdutos.getItems().clear();
		
		listViewProdutos.getItems().addAll(ProdutoDAO.loadProdutosCategoria(MapaScreenController.idLoja, categoria));
		
		listViewProdutos.setCellFactory(lv -> new ListCell<Produto>() {
			
		    @Override
		    public void updateItem(Produto produto, boolean empty) {
		        super.updateItem(produto, empty) ;
		       
		        Spinner<Integer> quantidade = new Spinner<Integer>(1, 10, 1);
		        
				Button actionBtn = new Button("adicionar");
		        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	
		            	listViewCompras.getItems().clear();
		            	
		            	double precoTotal = (quantidade.getValue()*produto.getPrecoProduto());
		            	if (!carrinhoProdutos.isEmpty()) {
		            		//Verificar se o produto ja existe no carrinho
		            		if (verificarProdutoCarrinho(produto.getIdProduto())) {
		            			double precoAntigo = carrinhoProdutos.get(indexProduto).getPrecoProduto();
		            			int quantidadeAntiga = carrinhoProdutos.get(indexProduto).getQuantidadeProduto();
		            			carrinhoProdutos.get(indexProduto).setPrecoProduto(precoAntigo+precoTotal);
		            			carrinhoProdutos.get(indexProduto).setQuantidadeProduto(quantidadeAntiga+quantidade.getValue());
		            		}
		            		else {
		            			carrinhoProdutos.add(new CarrinhoCompras(produto.getIdProduto(), produto.getNomeProduto(), quantidade.getValue(), precoTotal));
		            		}
		            	}
		            	else {
		            		carrinhoProdutos.add(new CarrinhoCompras(produto.getIdProduto(), produto.getNomeProduto(), quantidade.getValue(), precoTotal));
		            	}
		            	//configurar como a exibição em lista é exibida, ou seja vai aparecer a informação
            			//necessaria para cada produto comprado
		            	listViewCompras.setCellFactory(lv -> new ListCell<CarrinhoCompras>() {
            			    @Override
            			    public void updateItem(CarrinhoCompras compras, boolean empty) {
            			        super.updateItem(compras, empty) ;
            			        setText(empty ? null : compras.getQuantidadeProduto() + " x" + compras.getNomeProduto() + " ------------ " + df2.format(compras.getPrecoProduto()) + "€");
            			    }
            			});
		            	listViewCompras.getItems().addAll(carrinhoProdutos);
		            	//Atualizar valor total do carrinho
		            	precoTotalCarrinho.setText(df2.format(atualizarPrecoTotalCarrinho()) + "€");
		            }
		        });
		        
				if (produto != null) {
					Image img = new Image(new ByteArrayInputStream(produto.getImagemProduto()));
					ImageView imgview = new ImageView(img);
					imgview.setFitHeight(50);
					imgview.setFitWidth(50);
					
					quantidade.setMaxSize(50, 50);
					
					 // Create a HBox to hold our displayed value
                    HBox hBox = new HBox(5);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    // Add the values from our piece to the HBox
                    hBox.getChildren().addAll(
                    		quantidade,
                    		imgview,
                            new Label(produto.getNomeProduto() + "     Preço: " + produto.getPrecoProduto() + "€ /uni"), actionBtn);

                    // Set the HBox as the display
                    setGraphic(hBox);
					
				}
		    }
		});
	}
	
	/**
	 * Metodo para verificar se um certo produto ja existe na observableList do carrinho dos produtos
	 * @param produtoId id do produto
	 * @return true se o produto ja existe no carrinho, false se nao existe
	 */
	private boolean verificarProdutoCarrinho(int produtoId) {
		for (int i = 0; i < carrinhoProdutos.size(); i++) {
    		//Produto existe no carrinho
    		if (produtoId == carrinhoProdutos.get(i).getIdProduto()) {	
    			indexProduto = i;
    			return true;	
    		}
    	}
		return false;
	}
	
	/**
	 * Metodo para atualizar preço total do carrinho de compras
	 * @return double valor total
	 */
	private double atualizarPrecoTotalCarrinho() {
		double valorTotal = 0;
		for (int i = 0; i < carrinhoProdutos.size(); i++) {
			valorTotal += carrinhoProdutos.get(i).getPrecoProduto();
    	}
		return valorTotal;
	}
	
	/**
	 * Metodo de quando clica no botao para remover o produto da lista de compras
	 * Vai verificar se selecionou primeiro algum produto na listview para ser removido
	 * se o utilizador tiver selecionado algum produto, vai remover o produto da ObservableList e
	 * da listview.
	 * @param event quando clica no botao 
	 */
	@FXML
    void removerProdutoCarrinho(ActionEvent event) {

		final int selectedIdx = listViewCompras.getSelectionModel().getSelectedIndex();
		//Verificar se está selecionado algum item na lista de compras para ser removido	
	    if (selectedIdx != -1) {
	    
	    	//Remove o produto da ObservableList  
	    	carrinhoProdutos.remove(selectedIdx);
	    	
	    	//Remove o produto da lista de compras
	    	listViewCompras.getItems().remove(selectedIdx);
	    	
	    	//Atualiza o valor total do carrinho
	    	precoTotalCarrinho.setText(df2.format(atualizarPrecoTotalCarrinho()) + "€");
	    	
	    	Popups.dialogInformation("Produto removido", "Produto removido do carrinho de compras!");
	    }
	    else {
	    	Popups.dialogError("Error remover", "Selecione um produto do carrinho para ser removido!");
	    }
    }
	
	/**
	 * O utilizador ao clicar no botao de comprar vai verificar se a lista de compras não vazia, se não estiver
	 * vazia vai a base de dados ver qual o Ultimo valor de encomenda e vai adicionar mais um. 
	 * Vai enviar a informação de cada produto e vai limpar a observableList do carrinhoProdutos e da listview.
	 * @param event ao clicar no botao
	 */
	@FXML
    void comprar(ActionEvent event) {
		//Verifica se a lista de compras nao esta vazia
		if (!carrinhoProdutos.isEmpty()) {
			
			int maxNumEncomenda = EncomendaDAO.maxNumEncomenda();
			
			for (int i = 0; i < carrinhoProdutos.size(); i++) {
				
				int idProduto = carrinhoProdutos.get(i).getIdProduto();
				int quantidadeProduto = carrinhoProdutos.get(i).getQuantidadeProduto();
				EncomendaDAO.addEncomenda(maxNumEncomenda, idProduto, quantidadeProduto, MapaScreenController.idLoja, LoginUtilizadorController.IdUser);
				
			}
			carrinhoProdutos.clear();
			listViewCompras.getItems().clear();
			Popups.dialogInformation("Compra", "Compra efetuada com sucesso! \n" + "Numero encomenda: " + maxNumEncomenda + "\n" + "Data Entrega: " + EncomendaDAO.dataEntregaEncomenda(maxNumEncomenda));
			
			
		}
		else {
			Popups.dialogError("Erro Comprar", "Nao existe nenhum produto na lista de compras!");
		}
    }
}
