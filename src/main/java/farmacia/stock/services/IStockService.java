package farmacia.stock.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import farmacia.stock.models.entity.Producto;

public interface IStockService{

	public void createProduct(Producto producto);
	
	public boolean isRegistered(String clavePBI);
	
	public void addListOfProducts(List<Producto> listaProductos);	
	
	public void updateProduct(Producto producto);
	
	public List<Producto> getAllProducts();
	
	public void importExcelFile(MultipartFile file) throws IOException;
	
	public List<Producto> getAllProductsShortDescription();
	
	public Producto findById(String codigoDeBarras);
}
