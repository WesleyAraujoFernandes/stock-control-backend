package br.com.knowledge.stockcontrol_api.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.knowledge.stockcontrol_api.product.dto.CreateProductRequest;
import br.com.knowledge.stockcontrol_api.product.dto.ProductResponse;
import br.com.knowledge.stockcontrol_api.product.entity.ProductEntity;
import br.com.knowledge.stockcontrol_api.product.repository.ProductRepository;
import br.com.knowledge.stockcontrol_api.product.serivce.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldCreateProduct() {
        CreateProductRequest request = new CreateProductRequest(
                "Notebook",
                "NB001",
                "Informática",
                10,
                5,
                new BigDecimal("4200.00"));
        when(repository.existsBySku("NB001"))
                .thenReturn(false);
        when(repository.save(any(ProductEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = service.create(request);

        assertThat(response.name()).isEqualTo("Notebook");
        assertThat(response.sku()).isEqualTo("NB001");
        assertThat(response.active()).isTrue();

        verify(repository).existsBySku("NB001");
        verify(repository).save(any(ProductEntity.class));
    }
}
