package vn.iotstar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({ "/", "/index", "/home" })
    public String index() {
        return "index";
    }

    @GetMapping({ "/category", "/admin/category", "/ajax/category" })
    public String categoryAjax() {
        return "category-ajax";
    }

    @GetMapping({ "/product", "/admin/product", "/ajax/product" })
    public String productAjax() {
        return "product-ajax";
    }
}
