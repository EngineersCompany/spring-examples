package com.example.shopping.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shopping.entity.Product;
import com.example.shopping.input.ProductMaintenanceInput;
import com.example.shopping.service.ProductMaintenanceService;

@Controller
@RequestMapping("/maintenance/product")
public class ProductMaintenanceController {
    private static final String BASE_VIEW_NAME = "maintenance/product/";
    private final ProductMaintenanceService productMaintenanceService;

    public ProductMaintenanceController(
            ProductMaintenanceService productMaintenanceService
    ) {
        this.productMaintenanceService = productMaintenanceService;
    }

    @GetMapping("/display-list")
    public String displayList(Model model) {
        List<Product> products = productMaintenanceService.findAll();
        model.addAttribute("productList", products);
        return BASE_VIEW_NAME + "productList";
    }

    @GetMapping("/display-update-form")
    public String displayUpdateForm(@RequestParam String productId, Model model) {
        Product product = productMaintenanceService.findById(productId);
        ProductMaintenanceInput productMaintenanceInput = new ProductMaintenanceInput();
        productMaintenanceInput.setId(product.getId());
        productMaintenanceInput.setName(product.getName());
        productMaintenanceInput.setPrice(product.getPrice());
        productMaintenanceInput.setStock(product.getStock());
        model.addAttribute("productMaintenanceInput", productMaintenanceInput);
        return BASE_VIEW_NAME + "updateForm";
    }

    @PostMapping(value = "/validate-update-input")
    public String validateUpdateInput(
        @Validated ProductMaintenanceInput productMaintenanceInput, BindingResult bindResult) {
        if (bindResult.hasErrors()) {
            return BASE_VIEW_NAME + "updateForm";
        }
        return BASE_VIEW_NAME + "updateConfirmation";
    }

    @PostMapping(value = "/update", params = "correct")
    public String correct(@Validated ProductMaintenanceInput productMaintenanceInput) {
        return BASE_VIEW_NAME + "updateForm";
    }

    @PostMapping(value = "/update", params = "update")
    public String doUpdate(@Validated ProductMaintenanceInput productMaintenanceInput) {
        productMaintenanceService.update(productMaintenanceInput);
        return BASE_VIEW_NAME + "updateCompletion";
    }

//    @PutMapping(value = "/put")
//    public String put(@Validated ProductMaintenanceInput productMaintenanceInput) {
//        productMaintenanceService.update(productMaintenanceInput);
//        return BASE_VIEW_NAME + "updateCompletion";
//    }
    @PutMapping("/{id}") // params="put" は削除し、IDをパス変数に含める
    public ResponseEntity<String> updateProduct(
            @PathVariable("id") String id, // URLからIDを取得
            @RequestBody @Validated ProductMaintenanceInput productMaintenanceInput, // @RequestBody を追加
            BindingResult bindingResult) { // バリデーション結果を受け取る

        if (bindingResult.hasErrors()) {
            // バリデーションエラーがある場合
            // エラーメッセージをJSON形式で返すなどの処理を行う
            return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
        }

        // パス変数IDとリクエストボディ内のIDの整合性チェック（任意だが推奨）
        if (productMaintenanceInput.getId() != null && !id.equals(productMaintenanceInput.getId())) {
             return ResponseEntity.badRequest().body("ID mismatch between path and request body.");
        }
        
        // サービス層の更新処理
        productMaintenanceService.update(productMaintenanceInput);

        // 成功時のレスポンス
        return ResponseEntity.ok("Product with ID " + id + " updated successfully!");
    }

    

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public String displayUpdateFailure() {
        return BASE_VIEW_NAME + "updateFailure";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String displayDeleteFailure() {
        return BASE_VIEW_NAME + "deleteFailure";
    }
}
