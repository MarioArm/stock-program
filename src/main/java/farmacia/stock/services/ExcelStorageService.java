package farmacia.stock.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import farmacia.stock.exceptions.storage.StorageException;
import farmacia.stock.models.dao.ILoteDao;
import farmacia.stock.models.dao.IProductoDao;
import farmacia.stock.models.entity.Lote;
import farmacia.stock.models.entity.Producto;

@Service
public class ExcelStorageService implements IExcelStorageService {

	private final Path localPath;
	private String fileName;

	ExcelStorageService() {
		this.localPath = Paths.get("tempExcel");
	}

	@Override
	public void readExcelFile(IProductoDao productoDao) throws IOException {
		try(Workbook wb = WorkbookFactory.create(new File(localPath + "/" + fileName));) {
			
			Sheet sheet = wb.getSheetAt(0);
			Row row;
			Cell cell;
			Producto producto;
			Lote lote;
		        for (int i  = 4; i < sheet.getLastRowNum();i++) {
		            producto = new Producto();
		            lote = new Lote();
		            
		        	row = sheet.getRow(i);
		            
		            cell = row.getCell(1); //Codigo de barras
		            
		            if(!productoDao.existsById(cell.getStringCellValue())) {
		            	
		            	producto.setCodigoDeBarras(cell.getStringCellValue().trim());
		            	
		            	cell = row.getCell(0); //Clave PBI
		            	producto.setClavePBI(cell.getStringCellValue().trim());
		            	
		            	cell = row.getCell(2); //Descripcion
		            	producto.setDescripcion(cell.getStringCellValue().trim());
		            	
		            	
		            }else {
		            	//Obtiene el producto (hay que usar .get dado que es una clase Optional)
		            	producto = productoDao.findById(cell.getStringCellValue()).get();
		            }
		            
		            cell = row.getCell(3); //Lote
		            lote.setLote(cell.getStringCellValue().trim());
		            
		            cell = row.getCell(4); //Caducidad
		            lote.setFechaCaducidad(Date.valueOf(cell.getStringCellValue().trim()));
		            
		            cell = row.getCell(5); //Ubicacion
		            lote.setUbicacion(cell.getStringCellValue().trim());
		            
		            cell = row.getCell(6); //Cantidad
		            lote.setCantidad(Integer.parseInt(new DataFormatter().formatCellValue(cell)));
		            
		            cell = row.getCell(7); //Almacen
		            lote.setAlmacen(cell.getStringCellValue().trim());
		            
		            lote.setProducto(producto);
		            
		            producto.addLote(lote);
		            productoDao.save(producto);
		            
		        }
		    
			
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
			throw new IOException("Error al abrir el archivo Excel para leer");
		}
	}

	@Override
	public void storeExcelFile(MultipartFile file) {
		this.fileName = file.getOriginalFilename(); // Para hacer una lectura posterior
		try {
			Files.createDirectories(localPath);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		// TODO IMPLEMENTAR EL TIPO DE CONTENIDO tambien podemos usar un controlador de
		// poi de acuerdo
		System.out.println("El tipo de contenido del archivo cargado es:" + file.getContentType());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}
			Files.copy(file.getInputStream(), this.localPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public void deleteExcelPath() {
		FileSystemUtils.deleteRecursively(localPath.toFile());
	}
}
