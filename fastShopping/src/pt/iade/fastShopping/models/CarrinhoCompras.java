package pt.iade.fastShopping.models;

public class CarrinhoCompras {

	private int idProduto;
	private String nomeProduto;
	private int quantidadeProduto;
	private double precoProduto;
	
	public CarrinhoCompras(int idProduto, String nomeProduto, int quantidadeProduto, double precoProduto) {
		super();
		this.idProduto = idProduto;
		this.nomeProduto = nomeProduto;
		this.quantidadeProduto = quantidadeProduto;
		this.precoProduto = precoProduto;
	}

	public int getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(int quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}

	public double getPrecoProduto() {
		return precoProduto;
	}

	public void setPrecoProduto(double precoProduto) {
		this.precoProduto = precoProduto;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}
	
}
