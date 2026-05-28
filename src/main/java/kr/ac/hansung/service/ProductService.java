package kr.ac.hansung.service;

import kr.ac.hansung.dto.ProductDto;
import kr.ac.hansung.entity.Product;
import kr.ac.hansung.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // 기존 전체 조회 (테스트 호환용)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // 전체 상품 조회 (페이징)
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // 상품 검색 + 페이징
    public Page<Product> searchProducts(
            String keyword,
            Pageable pageable
    ) {

        return productRepository.findByNameContaining(
                keyword,
                pageable
        );
    }

    // 상품 상세 조회
    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "상품을 찾을 수 없습니다 : " + id
                        ));
    }

    // 상품 저장
    @Transactional
    public Product save(ProductDto dto) {

        Product product = new Product(
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getStock()
        );

        return productRepository.save(product);
    }

    // 상품 수정
    @Transactional
    public Product updateProduct(
            Long id,
            ProductDto dto
    ) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "상품을 찾을 수 없습니다 : " + id
                        ));

        // Dirty Checking
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());

        return product;
    }

    // 상품 삭제
    @Transactional
    public void deleteById(Long id) {

        productRepository.deleteById(id);
    }
}

