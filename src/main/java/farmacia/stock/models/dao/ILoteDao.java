package farmacia.stock.models.dao;

import org.springframework.data.repository.CrudRepository;

import farmacia.stock.models.entity.Lote;

public interface ILoteDao extends CrudRepository<Lote, Long>{

}
