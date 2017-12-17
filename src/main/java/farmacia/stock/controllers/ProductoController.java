package farmacia.stock.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import farmacia.stock.models.entity.CodigoDeBarra;
import farmacia.stock.models.entity.Counter;
import farmacia.stock.models.entity.Lote;
import farmacia.stock.models.entity.Producto;
import farmacia.stock.models.entity.ResponseUpdateProduct;
import farmacia.stock.services.StockService;

@Controller
// @SessionAtributes("objeto") implementar para mantener en memoria los datos
// del objeto cargado.
@SessionAttributes("Counter")
public class ProductoController {
	
	
	@Autowired
	private StockService stockService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String rootPage(Model model) {

		model.addAttribute("Titulo", "Sistema de pedidos");

		return "index";
	}

	@RequestMapping(value = "/products/productos", method = RequestMethod.GET)
	public String inventoryPage(Model model) {
		model.addAttribute("Productos", stockService.getAllProducts());
		model.addAttribute("Titulo", "Sistema de pedidos");
		return "products/productos";
	}

	@RequestMapping(value = "/products/import", method = RequestMethod.POST)
	public String importExcel(@RequestParam("file") MultipartFile excel, RedirectAttributes flash) {
		try {
			stockService.importExcelFile(excel);
		} catch (IOException e) {
			flash.addFlashAttribute("Error", "Sucedió un error importando el archivo de excel");
		}

		return "redirect:/products/productos";
	}

	@RequestMapping(value = "/products/stock", method = RequestMethod.GET)
	public String captureStock(Model model) {
		model.addAttribute("Titulo", "Captura de stock");
		model.addAttribute("Productos", stockService.getAllProductsShortDescription());
		model.addAttribute("Counter", new Counter());
		
		return "products/stock";
	}

	@RequestMapping(value = "/products/stock/update", method = RequestMethod.POST)
	public String saveStock(@RequestBody String respuestas, Model model) {
		List<ResponseUpdateProduct> listaRespuestas = new ArrayList<ResponseUpdateProduct>();
		String respuesta[] = respuestas.split("&");
		for (int i = 0; i < respuesta.length; i++) {
			String datos[] = respuesta[i].split("=");
			listaRespuestas.add(new ResponseUpdateProduct(datos[0], Integer.parseInt(datos[1])));
		}
		Producto producto;
		for (ResponseUpdateProduct response : listaRespuestas) {
			System.out.println("ID: " + response.getCodigoDeBarras() + " - Cantidad: " + response.getStockMinimo());
			producto = stockService.findById(response.getCodigoDeBarras());
			producto.setStockMinimo(response.getStockMinimo());

			stockService.updateProduct(producto);
		}
		return "redirect:/products/stock";
	}

	@RequestMapping(value = "/products/generacionPedido", method = RequestMethod.GET)
	public String generateOrder(Model model) {
		
		model.addAttribute("Titulo", "Pedido");

		List<Producto> productosPorMostrar= new ArrayList<Producto>();		
		List<Producto> productos = stockService.getAllProductsShortDescription();
		
		for(Producto producto: productos) {
//			if(producto.isLowStock() && (producto.cantidadPorSurtir()>0)) {
//				productosPorMostrar.add(producto);
//			}
		}		
		
		model.addAttribute("Productos", productosPorMostrar);
		model.addAttribute("Counter", new Counter());

		//return "products/generacionPedido";
		return "redirect:/";
	}
	

}
