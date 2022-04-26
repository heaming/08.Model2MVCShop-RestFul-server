package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {

	// field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;	
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	
	// ctor
	public PurchaseRestController() {
		System.out.println(this.getClass());
	}
	
	
	// method
	@RequestMapping(value="json/getPurchase/{tranNo}", method=RequestMethod.GET)
	public Purchase getPurchase(@PathVariable int tranNo) throws Exception {
		
		System.out.println("/json/getPurchase : GET");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		Product product = productService.getProduct(purchase.getPurchaseProd().getProdNo());
		
		purchase.setPurchaseProd(product);
		
		return purchase;		
	}
	
	
	@RequestMapping(value="json/updatePurchase/{tranNo}", method=RequestMethod.GET)
	public Purchase updatePurchase(@PathVariable int tranNo) throws Exception {
		
		System.out.println("/json/updatePurchase : GET");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		Product product = productService.getProduct(purchase.getPurchaseProd().getProdNo());
		
		purchase.setPurchaseProd(product);
		
		return purchase;
	}
	

	@RequestMapping(value="json/updatePurchase/{tranNo}", method=RequestMethod.POST)
	public Purchase updatePurchase(@RequestBody Purchase purchase) throws Exception {
		
		System.out.println("/json/updatePurchase : POST");
		
		Product product = productService.getProduct(purchase.getPurchaseProd().getProdNo());
		
		System.out.println(product);
		purchase.setPurchaseProd(product);

		purchaseService.updatePurchase(purchase);
		
		return purchaseService.getPurchase(purchase.getTranNo());
		
	}
	
	
	
	@RequestMapping(value="json/addPurchase/{prodNo}", method=RequestMethod.POST)
	public Purchase addPurchase(@RequestBody Purchase purchase, @PathVariable int prodNo) throws Exception {
		
		System.out.println("/json/addPruchase : POST");
		
		Product product = productService.getProduct(prodNo);
		//TODO
		//User user = (User) session.getAttribute("user"); //session 인자로 받기 
		
		User user = userService.getUser("admin");
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		purchaseService.addPurchase(purchase);
		
		System.out.println("왜 안나옴?" +purchase);
		
		return purchase;		
	}
	
	
	@RequestMapping(value="json/listPurchase")
	public Map<String, Object> listPurchase(@RequestBody Search search, HttpSession session) throws Exception {
		
		System.out.println("/json/listPurchase");
		
		User user = (User) session.getAttribute("user");
		
		user = userService.getUser("user02"); // for debug
		System.out.println("listPurchase"+user);
		
		return purchaseService.getPurchaseList(search, user.getUserId());	
	}
	
	
	@RequestMapping(value="json/listSale")
	public Map<String, Object> listSale(@RequestBody Search search, HttpSession session) throws Exception {
		
		System.out.println("/json/listSale");
		
		User user = (User) session.getAttribute("user");
		user = userService.getUser("user01"); // for debug
		
		if(user.getUserId().equals("admin")) {
			return purchaseService.getSaleList(search);
		}
		
		return purchaseService.getUserSaleList(search, user.getUserId());	
	}
	
	
	@RequestMapping(value="json/updateTranCodeByProd/{tranNo}/{tranCode}")
	public Purchase updateTranCodeByProd(@PathVariable int tranNo, @PathVariable String tranCode) throws Exception {
		
		System.out.println("/json/updateTranCodeByProd");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setTranCode(tranCode);
		
		purchaseService.updateTranCode(purchase);
		
		return purchaseService.getPurchase(purchase.getTranNo());
	}

}
