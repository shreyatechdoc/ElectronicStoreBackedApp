package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/Products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    FileService fileService;

    @Value("${product.image.path}")
    private String imagepath;

    // create product API
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {
        ProductDto createProduct=productService.create(productDto);
        return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
    }

    // update product API
    @PutMapping("/{productId}")
    ResponseEntity<ProductDto> updateProduct( @PathVariable String productId,@RequestBody ProductDto productDto)
    {
        ProductDto productDto1=productService.update(productDto,productId);
        return new ResponseEntity<>(productDto1,HttpStatus.OK);
    }

    // Delete product API
    @DeleteMapping("/{productId}")
   public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId)
    {
        productService.deleteProduct(productId);
        ApiResponseMessage apiResponseMessage=ApiResponseMessage.builder()
                .message("Product Deleted Successfully !!").
                success(true).
                status(HttpStatus.OK).
                build();
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
    }

    // get Single Product API
    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId)
    {
       ProductDto getProduct= productService.getProduct(productId);
        return new ResponseEntity<>(getProduct,HttpStatus.OK);
    }

    // getAll Products API
    @GetMapping
    ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam (value = "pageNumber",defaultValue ="0",required = false ) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam (value = "sortDir",defaultValue = "asc",required = false) String sortDir,
            @RequestParam (value = "sortBy",defaultValue = "title",required = false) String sortBy
    )
    {
        PageableResponse<ProductDto> pageableResponse=productService.getAll(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    // get product by search
    @GetMapping("/search/{title}")
    ResponseEntity<PageableResponse<ProductDto>> getBySearch(@PathVariable String title, @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                           @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                           @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir,
                                           @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy)
    {
        PageableResponse<ProductDto> pageableResponse=productService.getProductBySearch(title,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    // get All Live Products
    @GetMapping("/live")
    ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy
    )
    {
        PageableResponse<ProductDto>pageableResponse=productService.getAllLive(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    // upload products images
    @PostMapping("image/{productId}")
    ResponseEntity<ImageResponse>uploadProductImage(
            @RequestParam ("productImage") MultipartFile image,
            @PathVariable String productId)
            throws IOException {

     String imageName=fileService.uploadFile(image,imagepath);
     ProductDto product=productService.getProduct(productId);
     product.setProductImageName(imageName);
     ProductDto updatedProduct=productService.update(product,productId);
     ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).message("Image updated successfully !!").build();
     return  new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    // serve product images
    @GetMapping(value = "/image/{productId}")
    public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto product=productService.getProduct(productId);
        InputStream resource=fileService.getResource( imagepath,product.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    // get All Products Of Same Category
//    @GetMapping("{categoryId}")
//    public  ResponseEntity<ProductDto> getProductsOfCategory(@PathVariable String categoryId)
//    {
//        ProductDto productDto=productService.getProductsOfSameCategory(categoryId);
//        return new ResponseEntity<>(productDto,HttpStatus.OK);
//    }




}
