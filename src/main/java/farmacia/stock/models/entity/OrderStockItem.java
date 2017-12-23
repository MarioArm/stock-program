package farmacia.stock.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_stock_items")
public class OrderStockItem implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(name="quantity_in_stock")
	private int quantityInStock;
	
	@NotNull
	@Column(name="quantity_to_order")
	private int quantityToOrder;
	
	@NotEmpty
	@NotNull
	@Column(name="item_description")
	private String itemDescription;
	
	@NotEmpty
	@NotNull
	@Column(name="item_clave_pbi")
	private String itemClavePbi;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transactionId")
	private OrderStockHeader idOrderStockHeader;
	
	public OrderStockItem() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(int quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public int getQuantityToOrder() {
		return quantityToOrder;
	}

	public void setQuantityToOrder(int quantityToOrder) {
		this.quantityToOrder = quantityToOrder;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemClavePbi() {
		return itemClavePbi;
	}

	public void setItemClavePbi(String itemClavePbi) {
		this.itemClavePbi = itemClavePbi;
	}

	public OrderStockHeader getidOrderStockHeader() {
		return idOrderStockHeader;
	}

	public void setidOrderStockHeader(OrderStockHeader orderStockHeader) {
		this.idOrderStockHeader = orderStockHeader;
	}
	
}
