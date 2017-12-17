/**
 * 
 */
package farmacia.stock.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Mario Armenta
 * Entity that handles the header of an order for re-stock inventory.
 */
@Entity
@Table(name="order_stock_headers")
public class OrderStockHeader implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="transaction_id")
	private long transactionId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Column(name="elaboration_date")
	private Date elaborationDate;
	
	@NotNull
	@NotEmpty
	@Column(name="from_warehouse")
	private String fromWarehouse;
	
	@NotNull
	@NotEmpty
	@Column(name="to_warehouse")
	private String toWarehouse;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order_stock_header")
	private List<OrderStockItem> items;
	
	public OrderStockHeader(){
		items = new ArrayList<>();
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public Date getElaborationDate() {
		return elaborationDate;
	}

	public void setElaborationDate(Date elaborationDate) {
		this.elaborationDate = elaborationDate;
	}

	public String getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(String fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public String getToWarehouse() {
		return toWarehouse;
	}

	public void setToWarehouse(String toWarehouse) {
		this.toWarehouse = toWarehouse;
	}

	public List<OrderStockItem> getItems() {
		return items;
	}

	public void setItems(List<OrderStockItem> items) {
		this.items = items;
	}
	
	public void addItem(OrderStockItem item) {
		this.items.add(item);
	}

}
