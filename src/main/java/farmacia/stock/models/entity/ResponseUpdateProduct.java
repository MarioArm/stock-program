package farmacia.stock.models.entity;

public class ResponseUpdateProduct {

	private String codigoDeBarras;
	private int stockMinimo;

	public ResponseUpdateProduct(String codigoDeBarras, int stockMinimo) {		
		this.codigoDeBarras = codigoDeBarras;
		this.stockMinimo = stockMinimo;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public int getStockMinimo() {
		return stockMinimo;
	}

	public void setStockMinimo(int stockMinimo) {
		this.stockMinimo = stockMinimo;
	}

}
