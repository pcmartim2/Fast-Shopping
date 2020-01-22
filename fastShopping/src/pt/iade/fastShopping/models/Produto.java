package pt.iade.fastShopping.models;

import java.io.ByteArrayInputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import pt.iade.fastShopping.controllers.LoginUtilizadorController;
import pt.iade.fastShopping.controllers.MapaScreenController;
import pt.iade.fastShopping.controllers.SidebarLojaController;
import pt.iade.fastShopping.models.daos.DBConnector;
import pt.iade.fastShopping.models.daos.ProdutoDAO;


public class Produto {

	/**
	 * Array onde vai ficar armazenado os produtos que estao na base de dados para conseguir
	 * colocar na listview
	 */
	public static ArrayList<Produto> produtoCache = new ArrayList<Produto>();
	
	private int idProduto;
	private byte[] imagemProduto;
	private String nomeProduto;
	private String categoria;
	private double precoProduto;
	
	public Produto(int idProduto, byte[] imagemProduto, String nomeProduto, String categoria, double precoProduto) {
		this.idProduto = idProduto;
		this.imagemProduto = imagemProduto;
		this.nomeProduto = nomeProduto;
		this.categoria = categoria;
		this.precoProduto = precoProduto;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public byte[] getImagemProduto() {
		return imagemProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public String getCategoria() {
		return categoria;
	}

	public double getPrecoProduto() {
		return precoProduto;
	}	
	

	/**
	 * Metodo para listar os produtos na listview correspondente a uma certa loja
	 * @param lojaid id da loja
	 * @param listViewProdutos lista dos produtos
	 * @param listViewCompras lista do carrinho de compras
	 * @param categoria do produto
	 * @param precoTotal dos produtos todos
	 */
	public static void getProdutos(int lojaid, ListView<Produto> listViewProdutos, ListView<String> listViewCompras, String categoria, Label precoTotal) {
		
		listViewProdutos.getItems().clear();
		Produto.produtoCache.clear();
		
		ProdutoDAO.loadProdutosCategoria(MapaScreenController.idLoja, categoria);
		
		ObservableList<Produto> oListStavaka = FXCollections.observableArrayList(Produto.produtoCache);
		
		listViewProdutos.setCellFactory(new Callback<ListView<Produto>,ListCell<Produto>>() {

			@Override
			public ListCell<Produto> call(ListView<Produto> arg0) {
				ListCell<Produto> cell = new ListCell<Produto>() {
					@Override
					protected void updateItem(Produto produto, boolean btl) {
						super.updateItem(produto, btl);
						Spinner<Integer> quantidade = new Spinner<Integer>(1, 10, 1);
						Button actionBtn = new Button("adicionar");
				        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
				            @Override
				            public void handle(ActionEvent event) {
				            	
				            	//Formatar para duas casas decimais
				            	DecimalFormat df2 = new DecimalFormat("#.##");
				            	
				            	//Vai adicionar o produto na lista no carrinho de compras
				                listViewCompras.getItems().add(String.valueOf(quantidade.getValue() + "x ") + produto.getNomeProduto() + " ------------ " + String.valueOf(df2.format(quantidade.getValue() * produto.getPrecoProduto())) + "€");
				                double valorTotal = Double.valueOf(precoTotal.getText().replace("€", ""));
				                valorTotal += (quantidade.getValue() * produto.getPrecoProduto());
				                //Vai atualizar o preco total do carrinho de compras
				                precoTotal.setText(String.valueOf(df2.format(valorTotal)) + "€");
				                
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
				};
				return cell;
			}
			
		});
		listViewProdutos.setItems(oListStavaka);
	}
	
	/**
	 * Metodo para listar na listview todos os produtos correspondente a respetiva loja do proprietario.
	 * @param lojaid id da loja
	 * @param listViewProdutos lista dos produtos
	 */
	public static void getProdutosProprietario(int lojaid, ListView<Produto> listViewProdutos) {
		
		ProdutoDAO.loadProdutosLoja(lojaid);
		
		ObservableList<Produto> oListStavaka = FXCollections.observableArrayList(Produto.produtoCache);

		listViewProdutos.setCellFactory(new Callback<ListView<Produto>,ListCell<Produto>>() {

			@Override
			public ListCell<Produto> call(ListView<Produto> arg0) {
				ListCell<Produto> cell = new ListCell<Produto>() {
					@Override
					protected void updateItem(Produto produto, boolean btl) {
						super.updateItem(produto, btl);

						Button actionBtn = new Button("Remover");
						actionBtn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {

								removeItemLoja(listViewProdutos, actionBtn, produto.getIdProduto());

							}
						});


						if (produto != null) {
							Image img = new Image(new ByteArrayInputStream(produto.getImagemProduto()));
							ImageView imgview = new ImageView(img);
							imgview.setFitHeight(50);
							imgview.setFitWidth(50);

							// Create a HBox to hold our displayed value
							HBox hBox = new HBox(5);
							hBox.setAlignment(Pos.CENTER_LEFT);

							// Add the values from our piece to the HBox
							hBox.getChildren().addAll(
									imgview,
									new Label("ID:" + produto.getIdProduto() + " Nome: " + produto.getNomeProduto() + " Categoria: " + produto.getCategoria() + " Preço: " + produto.getPrecoProduto() + "€ /uni "), actionBtn);

							// Set the HBox as the display
							setGraphic(hBox);

						}
					}
				};
				return cell;
			}

		});
		listViewProdutos.setItems(oListStavaka);
	}
	
	//Metodo para remover item do carrinho de compras
	/**
	 * Metodo para remover algum produto que o utilizador adiconou no carrinho de compras.
	 * @param listViewCompras lista do carrinho de compras
	 * @param remover button de remover
	 * @param precoTotal dos produtos todos
	 */
	public static void removeItem(ListView<String> listViewCompras, Button remover, Label precoTotal) {
		
		remover.setOnAction(new EventHandler<ActionEvent>() {
		      @Override public void handle(ActionEvent event) {
		      final int selectedIdx = listViewCompras.getSelectionModel().getSelectedIndex();
		      if (selectedIdx != -1) {
		 
		    	  //Saber o valor do produto removido para subtrair do total
		    	  String value = listViewCompras.getSelectionModel().getSelectedItem().intern();
		    	  String[] parts = value.split(" ------------ ");
		    	  double valorFinal = Double.valueOf(parts[1].replace("€", ""));
		    	  
		    	  //Formatar para duas casas decimais
		    	  DecimalFormat df2 = new DecimalFormat("#.##");
		    	  
		    	  //Alterar valor do total
		    	  double valorAtual = Double.valueOf(precoTotal.getText().replace("€", ""));
		    	  double total = valorAtual - valorFinal;
		    	  precoTotal.setText(String.valueOf(df2.format(total)) + "€");
		    	  
		        	
		    	  listViewCompras.getItems().remove(selectedIdx);
		      }
		}
	});
		    
	}
	
	/**
	 * Metodo para remover o produto da loja do proprietario
	 * @param listViewProdutos lista de produtos
	 * @param remover button de remover
	 * @param produtoId id do produto selecionado
	 */
	public static void removeItemLoja(ListView<Produto> listViewProdutos, Button remover, int produtoId) {
		
		remover.setOnAction(new EventHandler<ActionEvent>() {
		      @Override public void handle(ActionEvent event) {
		      final int selectedIdx = listViewProdutos.getSelectionModel().getSelectedIndex();
		      if (selectedIdx != -1) {
		
		    	  //Saber o ID do produto
		    	  int value = listViewProdutos.getSelectionModel().getSelectedItem().getIdProduto();
		    	  
		    	  //Remover produto do Array
		    	  int id = value-1;
		    	  produtoCache.remove(id);
		    	  
		    	  //Remover o produto da base de dados
		    	  ProdutoDAO.deleteProdutoLoja(LoginUtilizadorController.lojaID, produtoId);
		    	  
		    	  //Remove item da listview
		    	  listViewProdutos.getItems().remove(selectedIdx);
		    	  
		    	  SidebarLojaController.warningDialog("Produto removido!", "Produto removido da loja com sucesso!");
		      }
		}
	});
	}
	
}
