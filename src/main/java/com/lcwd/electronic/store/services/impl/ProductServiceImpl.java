package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto create(ProductDto productDto) {

        Product product=modelMapper.map(productDto,Product.class);
        String productId= UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());
        Product savedProduct=productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDto.class);
    }


    // update product details --- firstly fetch given id then send new details
    @Override
    public ProductDto update( ProductDto productDto,String productId) {

        Product product=productRepository.findById(productId).orElseThrow( ()-> new ResourceNotFoundException("Given Id is not found"));
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        product.setQuantity(productDto.getQuantity());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());

        Product updatedProduct=productRepository.save(product);
        return modelMapper.map(updatedProduct,ProductDto.class);

    }

    // delete product details
    @Override
    public void deleteProduct(String productId) {
        Product deletedProduct=productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Given Id is not found !!"));
        productRepository.delete(deletedProduct);
    }

    @Override
    public ProductDto getProduct(String productId) {
        Product getproduct=productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("given ID is not found !!"));
        return modelMapper.map(getproduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findAll(pageable);

        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc")) ?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product>page=productRepository.findByLiveTrue(pageable);
        // getPageableResponse is used to converting page data to ProductDto data
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getProductBySearch(String subTitle,int pageNumber, int pageSize,  String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc")) ?(Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByTitleContaining(subTitle, pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public ProductDto createProductWithCategory( String categoryId,ProductDto productDto) {
    // fetch category from DB.
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category not found !!"));
        Product product=modelMapper.map(productDto,Product.class);
        // product id
        String productId= UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setAddedDate(new Date());

        product.setCategory(category);
        Product savedProduct=productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDto.class);

    }

    @Override
    public ProductDto updateCategoryOfProduct(String productId, String categoryId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("given product Id is not found !!"));
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category given id is not found !!"));
        product.setCategory(category);
        Product savedProduct=productRepository.save(product);
        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getProductsOfSameCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category given id is not found !!"));
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page=productRepository.findByCategory(category,pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }


}
