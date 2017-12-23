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
import farmacia.stock.models.entity.OrderStockItem;
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
		List<Producto> allProducts = (List<Producto>) productoDao.findAll();
		List<Producto> productsToShow = new ArrayList<>();
		Producto product;
		for (Producto producto : allProducts) {
			product = new Producto();
			if (producto.getBarCodes().get(0).getDescription().length() > 40) {
				producto.getBarCodes().get(0).setDescription(
						producto.getBarCodes().get(0).getDescription().substring(0, 39));
			}
			product.setClavePBI(producto.getClavePBI());
			product.setStockMinimo(producto.getStockMinimo());
			product.addBarCode(producto.getBarCodes().get(0));
			
			productsToShow.add(product);
			
			product = null;
		}
		return productsToShow;
	}

	@Override
	@Transactional(readOnly=true)
	public Producto findById(String codigoDeBarras) {
		CodigoDeBarra codigoDelProducto = barCodeDao.findById(codigoDeBarras).get();
		
		return codigoDelProducto.getProducto();
	}

	@Override
	@Transactional
	public void generateOrderToStock() {
		List<Producto> allProducts = (List<Producto>) productoDao.findAll();
		
		//Set up a stockHeader with fixed data
		//TODO change fixed data
		OrderStockHeader stockHeader = new OrderStockHeader();
		stockHeader.setElaborationDate(Date.from(Instant.now()));
		stockHeader.setFromWarehouse("Cedis PBI");
		stockHeader.setToWarehouse("100 Metros");
		
		int stockFromDestination, stockFromOrigin;
		
		OrderStockItem stockItem;
		
		//check up all products for low stock ones
		for(Producto producto : allProducts) {
			stockFromDestination = 0;
			stockFromOrigin = 0;
			
			for(CodigoDeBarra barCode : producto.getBarCodes()) {
				for(Lote lote : barCode.getLotes()) {
					//TODO change fixed values from origin, destination
					if(lote.getAlmacen().trim().equalsIgnoreCase("100 Metros")) {
						stockFromDestination+= lote.getCantidad();						
					}else if(lote.getAlmacen().trim().equalsIgnoreCase("Cedis PBI")) {
						stockFromOrigin+=lote.getCantidad();
					}
				}
			}
			if(stockFromDestination < producto.getStockMinimo() && stockFromOrigin > 0) {
				stockItem = new OrderStockItem();
				stockItem.setItemClavePbi(producto.getClavePBI());
				stockItem.setQuantityInStock(stockFromDestination);
				
				int quantityToOrder;
				//if stockFromOrigin is less or equal than minimum stock minus stockfromDestination get all stockFromOrigin 
				if(stockFromOrigin <= (producto.getStockMinimo() - stockFromDestination)) {
					quantityToOrder = stockFromOrigin;
				}else {
					quantityToOrder = (producto.getStockMinimo() - stockFromDestination);
				}
				
				stockItem.setQuantityToOrder(quantityToOrder);
				stockItem.setItemDescription(producto.getBarCodes().get(0).getDescription());
				stockItem.setidOrderStockHeader(stockHeader);
				stockHeader.addItem(stockItem);
				
				stockItem = null;
			}
		}
		
		stockHeaderDao.save(stockHeader);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<OrderStockHeader> getAllHeaders() {
		List<OrderStockHeader> orderStockHeaders = new ArrayList<>();
		orderStockHeaders = (List<OrderStockHeader>) stockHeaderDao.findAll();
		return orderStockHeaders;
	}

	@Override
	public OrderStockHeader findHeaderById(String id) {
		OrderStockHeader header = stockHeaderDao.findById(Long.valueOf(id)).get();
		return header;
	}

}
