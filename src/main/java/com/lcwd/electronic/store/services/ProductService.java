package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;

public interface ProductService {

    // create product details
    ProductDto create(ProductDto productDto);

    // update product details --- firstly fetch given id then send new details
     ProductDto update(ProductDto productDto,String productId);

    // delete product details
    void deleteProduct(String productId);

    // get single Product Detail
    ProductDto getProduct(String productId);

    // get All Product Details
    PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    // get Live Products
    PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);

    // get Product by Search
    PageableResponse<ProductDto> getProductBySearch(String subTitle,int pageNumber,int pageSize,String sortDir,String sortBy);

    // create product with Category
    ProductDto createProductWithCategory(String categoryId,ProductDto productDto);

    // update category of product
    ProductDto updateCategoryOfProduct(String productId,String categoryId);

    // get products of Same Category
     PageableResponse<ProductDto> getProductsOfSameCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
}
