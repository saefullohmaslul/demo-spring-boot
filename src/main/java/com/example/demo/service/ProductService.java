package com.example.demo.service;

import com.example.demo.entity.ProductEntity;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductEntity> findAll() {
        return this.productRepository.findAll();
    }

    public Page<ProductEntity> findAllWithPagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return this.productRepository.findAll(pageRequest);
    }
}
