package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

public interface CategoryService {

    // create single category detail
    CategoryDto create(CategoryDto categoryDto);

    //update category details
    CategoryDto update (CategoryDto categoryDto,String categoryId);

    //delete category details
    void delete(String categoryId);

    //get all categories details
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single category detail
    CategoryDto get(String categoryId);

}
