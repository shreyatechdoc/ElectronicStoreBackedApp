package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    //create category details
    @PostMapping
    ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        CategoryDto response=categoryService.create(categoryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //update Category Details
    @PutMapping("/{categoryId}")
    ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto)
    {
        CategoryDto updatedCategory=categoryService.update(categoryDto,categoryId);
    return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    // delete Category details
    @DeleteMapping("/{categoryId}")
    ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId)
    {
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage=ApiResponseMessage.builder().message("Category is Deleted Successfully !!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    // get Single Category Detail
    @GetMapping("/{categoryId}")
    ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId)
    {
        CategoryDto getCategory=categoryService.get(categoryId);
        return new ResponseEntity<>(getCategory,HttpStatus.OK);
    }

    // get all Category Details
    @GetMapping
    ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber" , defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize" ,defaultValue = "2" , required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
    )
    {
        PageableResponse<CategoryDto> pageableResponse=categoryService.getAll(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //get product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,@RequestBody ProductDto productDto)
    {
        ProductDto productDto1=productService.createProductWithCategory(categoryId, productDto);
        return new ResponseEntity<>(productDto1,HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryWithProduct(@PathVariable String categoryId,@PathVariable String productId)
    {
        ProductDto updateCategoryOfProduct=productService.updateCategoryOfProduct(productId,categoryId);
        return new ResponseEntity<>(updateCategoryOfProduct,HttpStatus.OK);
    }


    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductsByCategory(@PathVariable String categoryId,
                                                                                 @RequestParam(value = "pageNumber" , defaultValue = "0",required = false) int pageNumber,
                                                                                 @RequestParam(value = "pageSize" ,defaultValue = "10" , required = false) int pageSize,
                                                                                 @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                                 @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir)
    {
        PageableResponse<ProductDto> pageableResponse=productService.getProductsOfSameCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


}
