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

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "codigo_de_barras")
	private String codigoDeBarras;

	@Column(name = "clave_pbi")
	private String clavePBI;

	@NotEmpty
	private String descripcion;

	@Column(name = "stock_minimo")
	private int stockMinimo;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "producto")
	private List<Lote> lotes;

	public Producto() {
		lotes = new ArrayList<Lote>();
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public String getClavePBI() {
		return clavePBI;
	}

	public void setClavePBI(String clavePBI) {
		this.clavePBI = clavePBI;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getStockMinimo() {
		return stockMinimo;
	}

	public void setStockMinimo(int stockMinimo) {
		this.stockMinimo = stockMinimo;
	}

	public List<Lote> getLotes() {
		return lotes;
	}

	public void addLote(Lote lote) {
		this.lotes.add(lote);
	}

	public void setLotes(List<Lote> lotes) {
		this.lotes = lotes;
	}

	public int contarExistenciasSucursal() {
		int existencias = 0;
		for (Lote lote : this.lotes) {
			if (lote.getAlmacen().equalsIgnoreCase("100 Metros")) {
				existencias += lote.getCantidad();
			}
		}
		return existencias;
	}
	
	public int contarExistenciasExterna() {
		int existencias = 0;
		for (Lote lote : this.lotes) {
			if (lote.getAlmacen().equalsIgnoreCase("Cedis PBI")) {
				existencias += lote.getCantidad();
			}
		}
		return existencias;
	}

	public boolean isLowStock() {
		if (this.stockMinimo > contarExistenciasSucursal()) {
			return true;
		} else {
			return false;
		}
	}

	public Date getCaducidad() {
		Date fechaProxima = null;
		if (!lotes.isEmpty()) {
			if (lotes.size() == 1 && lotes.get(0).getAlmacen().equalsIgnoreCase("Cedis PBI")) {
				return lotes.get(0).getFechaCaducidad();
			} else {
				for (Lote lote : this.lotes) {
					if (lote.getAlmacen().equalsIgnoreCase("Cedis PBI")) {
						if (fechaProxima == null) {
							fechaProxima = lote.getFechaCaducidad();
							continue;
						} else {
							if (fechaProxima.after(lote.getFechaCaducidad())) {
								fechaProxima = lote.getFechaCaducidad();
							}
						}
					}
				}

			}
		}
		return fechaProxima;
	}

	public int cantidadPorSurtir() {
		int cantidadFaltante = this.stockMinimo - this.contarExistenciasSucursal();
		if(this.contarExistenciasExterna() < cantidadFaltante) {
			return contarExistenciasExterna();
		}else {
			return cantidadFaltante;
		}
	}
	
}
