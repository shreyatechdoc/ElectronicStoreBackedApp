package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    // create
    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        // randmonaly/Automatically generate categoryId through user manually
        String categoryId=UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category=modelMapper.map(categoryDto, Category.class);
        Category saveCategory=categoryRepository.save(category);
        return modelMapper.map(saveCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Resource not found !!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory=categoryRepository.save(category);
        return modelMapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
    Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Resource Not Found Exception !!"));
    categoryRepository.delete(category);
    }

    //getAll Categories
    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page=categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse=Helper.getPageableResponse(page,CategoryDto.class);
        return pageableResponse;
    }

    //get Single category detail
    @Override
    public CategoryDto get(String categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Resource Not Found !!"));
        return modelMapper.map(category,CategoryDto.class);
    }


}
