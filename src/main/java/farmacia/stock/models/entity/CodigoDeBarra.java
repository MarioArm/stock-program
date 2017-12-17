/**
 * 
 */
package farmacia.stock.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;

/**
 * @author Mario Armenta
 * BarCodes Class. This is an Entity class, it stores the information related to the bar code of
 * our products, a bar code can have several batches and a product can have several bar codes.
 *
 */

@Entity
@Table(name = "codigos_de_barra")
public final class CodigoDeBarra implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "bar_code")
	private String barCode;
	
	@NotEmpty
	@Column(name = "description")
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "barCode")
	private List<Lote> lotes;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clave_pbi")
	private Producto producto;
	
	public CodigoDeBarra() {
		lotes = new ArrayList<>();
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Lote> getLotes() {
		return lotes;
	}

	public void setLotes(List<Lote> lotes) {
		this.lotes = lotes;
	}
	
	public void addLote(Lote lote) {
		this.lotes.add(lote);
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	
	
}
