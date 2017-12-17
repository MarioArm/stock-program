package farmacia.stock.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author Mario Armenta
 *
 * Product class, this class holds the information about the product, it has several bar codes for each product.
 */
@Entity
@Table(name = "productos")
public class Producto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "clave_pbi")
	private String clavePBI;

	@Column(name = "stock_minimo")
	private int stockMinimo;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "producto")
	private List<CodigoDeBarra> barCodes;

	public Producto() {
		barCodes = new ArrayList<>();
	}

	public String getClavePBI() {
		return clavePBI;
	}

	public void setClavePBI(String clavePBI) {
		this.clavePBI = clavePBI;
	}

	public int getStockMinimo() {
		return stockMinimo;
	}

	public void setStockMinimo(int stockMinimo) {
		this.stockMinimo = stockMinimo;
	}

	public List<CodigoDeBarra> getBarCodes() {
		return barCodes;
	}

	public void setBarCodes(List<CodigoDeBarra> barCodes) {
		this.barCodes = barCodes;
	}
	
	public void addBarCode(CodigoDeBarra barCode) {
		this.barCodes.add(barCode);
	}
	
	
}
