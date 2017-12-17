
package farmacia.stock.models.dao;

import org.springframework.data.repository.CrudRepository;

import farmacia.stock.models.entity.CodigoDeBarra;

/**
 * @author Mario Armenta
 *
 */
public interface ICodigoDeBarraDao extends CrudRepository<CodigoDeBarra, String> {

}
