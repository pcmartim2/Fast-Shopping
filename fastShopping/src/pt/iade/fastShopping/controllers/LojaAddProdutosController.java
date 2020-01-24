package pt.iade.fastShopping.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import pt.iade.fastShopping.WindowManager;
import pt.iade.fastShopping.models.Popups;
import pt.iade.fastShopping.models.Produto;
import pt.iade.fastShopping.models.daos.ProdutoDAO;


/**
 * Nesta classe as funcionalidades relacionadas ao Painel do proprietário da loja vão estar aqui.
 * Quando clica no botão de adicionar vai verificar se o utilizador preencheu os requisitos necessários
 * para adicionar um novo produto na sua loja, o proprietario da loja vai precisar de colocar
 * a imagem do produto, o respetivo preço unitário e a categoria do produto para conseguir
 * adicionar um novo produto.
 */
public class LojaAddProdutosController {
	
	
	/**
	 * Imagem do produto
	 */
	@FXML
    private ImageView imagemProduto;
	
	/**
	 * Filtrar as categorias do produto
	 */
	@FXML
    private ChoiceBox<String> categoriaProduto;
	
	/**
	 * Nome do Produto quando adicionado na loja
	 */
	@FXML
	private TextField nome_Produto;
	
	/**
	 * Preço do produto quando adicionado na loja
	 */
	@FXML
    private Spinner<Double> preco_Produto;
	
	/**
	 * Listview dos produtos já adicionados na loja
	 */
	@FXML
    private ListView<Produto> listViewProdutos;

	/**
	 * Variavel onde vai ficar armazenado a imagem ate ser convertida para byte array
	 */
	private BufferedImage bufferedImage;
	
	/**
	 * Quando o fxml que corresponde a este controlador vai executar este codigo quando incia.
	 * Vai adicionar as categorias na comboBox da categoria, na janela do proprietario
	 * da loja quando for adicionar um novo produto na sua loja.
	 * Vai adicionar um Spinner Value com o valor inicial, valor maximo e valor minimo.
	 */
	@FXML
	private void initialize() {
		
		SpinnerValueFactory<Double> ValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10000.0, 0.0);
		preco_Produto.setValueFactory(ValueFactory);
		preco_Produto.setEditable(true);
	
		//Adicionar as Categorias na comboBox da Categoria
		categoriaProduto.setItems(ProdutoDAO.getCategoriasProdutos());
		
		//Adiciona todos os produtos que a loja tem na listview
		produtosLoja(LoginUtilizadorController.lojaID);
		
	}

	/**
	 * Ao clica no button "Carregar Imagme" vai abrir o file explorer do computador
	 * e vai ser possivel fazer o upload da imagem correspondente ao produto.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
    void carregarImagem(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilterPNG = 
                new FileChooser.ExtensionFilter("PNG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterpng = 
                new FileChooser.ExtensionFilter("png files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters()
                .addAll(extFilterPNG, extFilterpng);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        
        if (file != null) {
        	try {
            	bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imagemProduto.setImage(image);
            } catch (IOException ex) {
            	
            }
        }
	
    }
	
	/**
	 * Quando o proprietario clica no button para adicionar vai adicionar um novo produto na sua loja
	 * Vai verificar se preencheu todos os campos, se sim vai converter a imagem para byte array e vai adiconar o produto
	 * na base de dados e vai atualizar a listview para aparecer o novo produto da loja.
	 * @param event quando o utilizador clica no button
	 */
	@FXML
    void addProduto(ActionEvent event) {
		if (imagemProduto.getImage() != null && !nome_Produto.getText().isEmpty() && preco_Produto.getValue() != null && !categoriaProduto.getSelectionModel().isEmpty() ) {
			
			
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				ImageIO.write(bufferedImage, "jpg", baos);
				ImageIO.write(bufferedImage, "png", baos);
				
				baos.flush();
				byte[] imageInByte = baos.toByteArray();
				baos.close();
				ProdutoDAO.addProduto(imageInByte, nome_Produto.getText(), categoriaProduto.getSelectionModel().getSelectedItem(), String.valueOf(preco_Produto.getValue().floatValue()), LoginUtilizadorController.lojaID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Limpar todos os parametros
			imagemProduto.setImage(null);
			nome_Produto.clear();
			
			//Atualizar ListView
			listViewProdutos.getItems().clear();
			produtosLoja(LoginUtilizadorController.lojaID);
			
			Popups.dialogInformation("Produto adicionado", "Produto adicionado com sucesso na Loja!");
		}
		else {
			Popups.dialogError("Erro adicionar", "Antes de adiconar o produto na loja verifique se já preencheu tudo!");
		}
    }

	/**
	 * Ao clicar no button vai voltar para o ecra inicial
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void Logout(MouseEvent event) {
		// Ao clicar no botão volta para o menu inicial
		if (event.getButton() == MouseButton.PRIMARY) {
			WindowManager.openLoginWindow();
		}
	}
	
	/**
	 * Metodo para listar na listview todos os produtos correspondente a respetiva loja do proprietario.
	 * @param lojaid id da loja
	 */
	private void produtosLoja(int lojaid) {
		
		listViewProdutos.getItems().addAll(ProdutoDAO.loadProdutosLoja(lojaid));

		listViewProdutos.setCellFactory(lv -> new ListCell<Produto>() {
		    @Override
		    public void updateItem(Produto produto, boolean empty) {
		        super.updateItem(produto, empty) ;
		       
		        Button actionBtn = new Button("Remover");
				actionBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						removeItemLoja(actionBtn, produto.getIdProduto());

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
		});
	}
	
	/**
	 * Metodo para remover o produto da loja do proprietario
	 * @param remover button de remover
	 * @param produtoId id do produto selecionado
	 */
	private void removeItemLoja(Button remover, int produtoId) {
		
		remover.setOnAction(new EventHandler<ActionEvent>() {
		      @Override public void handle(ActionEvent event) {
		      final int selectedIdx = listViewProdutos.getSelectionModel().getSelectedIndex();
		      if (selectedIdx != -1) {
		    	  
		    	  //Remover o produto da base de dados
		    	  ProdutoDAO.deleteProdutoLoja(LoginUtilizadorController.lojaID, produtoId);
		    	  
		    	  //Remove item da listview
		    	  listViewProdutos.getItems().remove(selectedIdx);
		    	 
		    	  Popups.dialogInformation("Produto removido!", "Produto removido da loja com sucesso!");
		      }
		}
	});
	}
}
