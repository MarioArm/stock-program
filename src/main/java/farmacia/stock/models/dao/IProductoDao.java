package farmacia.stock.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import farmacia.stock.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, String>{
	
}
