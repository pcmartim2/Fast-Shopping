package pt.iade.fastShopping.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import pt.iade.fastShopping.WindowManager;
import pt.iade.fastShopping.models.daos.DBConnector;
import pt.iade.fastShopping.models.daos.LojaDAO;

public class MapaAdminScreenController {

	private double coordX;
	private double coordY;
	private boolean selecionado = false;

	@FXML
	private Label nome_UtilizadorView, info_nomeLoja, info_proprietarioLoja, info_estiloLoja;

	@FXML
	private TextField nome_Loja, proprietario_Loja;

	@FXML
	private Circle localSelecionado;

	@FXML
	private AnchorPane root, info_Loja;

	@FXML
	private ChoiceBox<String> estilo_Loja;
	
	@FXML
    private ImageView imagemLoja;

	
	/**
	 * Variavel onde vai ficar armazenado a imagem antes de ser convertida para byte array
	 */
	private BufferedImage bufferedImage;


	/**
	 * Quando o fxml que corresponde a este controlador vai executar este codigo quando incia.
	 * Vai desenhar todas as lojas que estiverem na base de dados no mapa.
	 * Vai adicionar todos os tipos de loja existentes na base de dados na comboBox, para
	 * ser possivel o administrador adicionar uma nova loja com um tipo de loja especifico.
	 * Quando o administrador clica numa das lojas que estao no mapa vai aparecer um pop-up
	 * com alguma informacao da loja selecionada, o nome da loja, tipo da loja e proprietario. 
	 */
	@FXML
	private void initialize() {
		// Metodo para desenhar as lojas no mapa
		LojaDAO.loadLojasMapa(root);
		
		
		// Vai adicionar o tipo de lojas no comboBox
		LojaDAO.loadEstilosLojas(estilo_Loja);
		
		
		
		// quando clica numa das lojas no mapa vai aparecer a respetiva informacao sobre a loja
		root.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double x = event.getX();
				double y = event.getY();
			
				try {
					PreparedStatement statement = DBConnector.getConnection().prepareStatement("select NomeEstilo, NomeLoja, IdLoja , Coordenadas, Proprietario from EstiloLoja E, Loja_has_Estilo H, Loja L where L.IdLoja = H.Loja_IdLoja and H.Estilo_IdEstilo = E.IdEstilo");
					ResultSet results = statement.executeQuery();
					while (results.next()) {
						String coordenadas = results.getString(4);
						//Vai tirar o # das coordenadas para sabermos a coordenada X e Y
						String[] split = coordenadas.split("#");
						double coordxLoja = Double.parseDouble(split[0]);
						double coordyLoja = Double.parseDouble(split[1]);

						//Vai comparar se as coordenadas onde o utilizador clicou com o rato coincidem com a da loja
						if (x > coordxLoja - 7 / 2 && x < coordxLoja + 7 / 2 && y > coordyLoja - 7 / 2 && y < coordyLoja + 7 / 2) {
							String estiloLoja = results.getString(1);
							String nomeLoja = results.getString(2);
							String proprietarioLoja = results.getString(5);
							
							//Vai aparecer a informacao da loja, como o nome, proprietario e estilo
							info_Loja.setVisible(true);
							info_nomeLoja.setText(nomeLoja);
							info_proprietarioLoja.setText(proprietarioLoja);
							info_estiloLoja.setText(estiloLoja);
						}
						
					}
				}
				catch (SQLException ev) {
					ev.printStackTrace();
				}
		
			}
		});
	}

	/**
	 * Metodo de quando o administrado clica no button para adicionar uma nova loja na base de dados.
	 * Vai verificar se os campos estao todos preenchidos e se selecionou no mapa a localizacao,
	 * se sim vai adiconar a nova loja. Vai aparecer uma mensagem que o administrador consegiu adicionar 
	 * uma nova loja.
	 * 
	 * @param event quando o utilizador clica no button
	 */
	@FXML
	void addLoja(ActionEvent event) {
		// verifica se os campos estão preenchidos, se sim, ao clicar no Botao vai
		// adicionar a loja
		if (imagemLoja.getImage() != null && !nome_Loja.getText().isEmpty() && !proprietario_Loja.getText().isEmpty()
				&& estilo_Loja.getValue() != null) {
			
			//Se a loja estiver selecionada no mapa
			if (selecionado) {
				
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					ImageIO.write(bufferedImage, "jpg", baos);
					ImageIO.write(bufferedImage, "png", baos);
					
					baos.flush();
					byte[] imageInByte = baos.toByteArray();
					baos.close();
					LojaDAO.addLoja(nome_Loja.getText(), imageInByte, coordX+"#"+coordY, proprietario_Loja.getText(), estilo_Loja.getValue());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				selecionado = false;
				localSelecionado.setVisible(false);
				LojaDAO.loadLojasMapa(root);
				SidebarLojaController.warningDialog("Loja adicionada!", nome_Loja.getText() + "adicionada com sucesso!");
			}
		} else {
			// Se nao preencheu os campos para adicionar uma nova loja
			// vai dar uma mensagem de erro.
			SidebarLojaController.warningDialog("Erro adicionar Loja!", "Por favor preencha os campos!");
		}
	}

	/**
	 * Quando o administrador clica em algum sitio no mapa, se estiver aberto algum pop-up
	 * de informacao de alguma loja vai fechar e vai colocar um novo circulo que corresponde a
	 * localizacao da nova loja que o administrador quer adicionar na base de dados.
	 * @param event quando o utilizador clica no mapa
	 */
	@FXML
	void aoClicarMapa(MouseEvent event) {

		coordX = event.getX();
		coordY = event.getY();

		// Ao clicar num sitio do mapa vai fechar a janela
		// de info da loja se estiver aberta
		if (info_Loja.isVisible()) {
			info_Loja.setVisible(false);
		}

		// vai colocar circulo no respetivo x e y
		localSelecionado.setVisible(true);
		localSelecionado.setLayoutX(coordX);
		localSelecionado.setLayoutY(coordY);
		selecionado = true;

	}
	
	/**
	 * Ao clica no button "Carregar Imagem" vai abrir o file explorer do computador
	 * e vai ser possivel fazer o upload da imagem correspondente a loja.
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
                imagemLoja.setImage(image);
            } catch (IOException ex) {
            	
            }
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
}
