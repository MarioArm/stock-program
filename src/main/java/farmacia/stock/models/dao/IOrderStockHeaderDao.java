/**
 * 
 */
package farmacia.stock.models.dao;

import org.springframework.data.repository.CrudRepository;

import farmacia.stock.models.entity.OrderStockHeader;

/**
 * @author Mario Armenta
 *
 */
public interface IOrderStockHeaderDao extends CrudRepository<OrderStockHeader, Long> {

}
