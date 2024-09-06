package com.example.demo.controller;

import com.example.demo.entity.ProductEntity;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {

        int realPage = 0;
        if (page >= 1) {
            realPage = page - 1;
        }

        Page<ProductEntity> products = this.productService.findAllWithPagination(realPage, 10);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", products.getTotalPages());

        return "list-product";
    }
}
