package farmacia.stock.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import farmacia.stock.models.dao.ILoteDao;
import farmacia.stock.models.dao.IProductoDao;
import farmacia.stock.models.entity.Producto;

@Service
public class StockService implements IStockService {

	@Autowired
	private IProductoDao productoDao;
	@Autowired
	private ILoteDao loteDao;
	@Autowired
	private IExcelStorageService excelStorage;

	@Override
	@Transactional
	public void createProduct(Producto producto) {
		productoDao.save(producto);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isRegistered(String clavePBI) {
		return productoDao.existsById(clavePBI);
	}

	@Override
	@Transactional
	public void addListOfProducts(List<Producto> listaProductos) {
		for (Producto producto : listaProductos) {
			if (!productoDao.existsById(producto.getClavePBI())) {
				productoDao.save(producto);
			}
		}

	}

	@Override
	@Transactional
	public void updateProduct(Producto producto) {
		if (!productoDao.existsById(producto.getClavePBI())) {
			productoDao.save(producto);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> getAllProducts() {

		return (List<Producto>) productoDao.findAll();
	}

	@Override
	@Transactional
	public void importExcelFile(MultipartFile file) throws IOException {
		excelStorage.storeExcelFile(file);
		loteDao.deleteAll();
		excelStorage.readExcelFile(productoDao);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> getAllProductsShortDescription() {
		List<Producto> productos = (List<Producto>) productoDao.findAll();
		for (Producto producto : productos) {
			if (producto.getDescripcion().length() > 40) {
				producto.setDescripcion(producto.getDescripcion().substring(0, 39));
			}
		}
		return productos;
	}

	@Override
	public Producto findById(String codigoDeBarras) {
		return productoDao.findById(codigoDeBarras).get();
	}

}
