package farmacia.stock.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import farmacia.stock.models.dao.IProductoDao;

public interface IExcelStorageService {
	
	public void readExcelFile(IProductoDao producto) throws IOException;
	
	public void storeExcelFile(MultipartFile file);
	
	public void deleteExcelPath();
	
}
