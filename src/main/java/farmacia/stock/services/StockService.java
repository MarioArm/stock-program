package farmacia.stock.services;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import farmacia.stock.models.dao.ICodigoDeBarraDao;
import farmacia.stock.models.dao.ILoteDao;
import farmacia.stock.models.dao.IOrderStockHeaderDao;
import farmacia.stock.models.dao.IProductoDao;
import farmacia.stock.models.entity.CodigoDeBarra;
import farmacia.stock.models.entity.Lote;
import farmacia.stock.models.entity.OrderStockHeader;
import farmacia.stock.models.entity.Producto;

@Service
public class StockService implements IStockService {

	@Autowired
	private IProductoDao productoDao;
	@Autowired
	private ILoteDao loteDao;
	@Autowired
	private ICodigoDeBarraDao barCodeDao;
	@Autowired
	private IExcelStorageService excelStorage;
	@Autowired
	private IOrderStockHeaderDao stockHeaderDao;

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
		barCodeDao.deleteAll();
		loteDao.deleteAll();
		excelStorage.readExcelFile(productoDao,barCodeDao);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> getAllProductsShortDescription() {
		List<Producto> productos = (List<Producto>) productoDao.findAll();
		for (Producto producto : productos) {
			for(CodigoDeBarra barCode: producto.getBarCodes()) {
				if (barCode.getDescription().length() > 40) {
					barCode.setDescription(barCode.getDescription().substring(0, 39));
				}				
			}
		}
		return productos;
	}

	@Override
	public Producto findById(String codigoDeBarras) {
		CodigoDeBarra codigoDelProducto = barCodeDao.findById(codigoDeBarras).get();
		
		return codigoDelProducto.getProducto();
	}

	@Override
	public OrderStockHeader generateOrderToStock() {
		List<Producto> allProducts = (List<Producto>) productoDao.findAll();
		List<Producto> lowStockProducts = new ArrayList<>();
		
		OrderStockHeader stockHeader = new OrderStockHeader();
		stockHeader.setElaborationDate(Date.from(Instant.now()));
		
		for(Producto producto : allProducts) {
			int quantityInStock = 0;
			for(CodigoDeBarra barCode : producto.getBarCodes()) {
				for(Lote lote : barCode.getLotes()) {
					quantityInStock+= lote.getCantidad();
				}
			}
			if(quantityInStock < producto.getStockMinimo()) {
				lowStockProducts.add(producto);
			}
		}		
		return null;
	}

}
