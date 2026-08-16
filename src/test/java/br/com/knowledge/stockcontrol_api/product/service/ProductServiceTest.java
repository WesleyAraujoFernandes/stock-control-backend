package br.com.knowledge.stockcontrol_api.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.knowledge.stockcontrol_api.product.dto.CreateProductRequest;
import br.com.knowledge.stockcontrol_api.product.dto.ProductResponse;
import br.com.knowledge.stockcontrol_api.product.dto.UpdateProductRequest;
import br.com.knowledge.stockcontrol_api.product.entity.ProductEntity;
import br.com.knowledge.stockcontrol_api.product.exception.ProductNotFoundException;
import br.com.knowledge.stockcontrol_api.product.exception.ProductSkuAlreadyExistsException;
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

        @Test
        void shouldUpdateProduct() {
                UUID id = UUID.randomUUID();
                ProductEntity product = new ProductEntity(
                                id,
                                "Notebook",
                                "NB001",
                                "Informática",
                                10,
                                5,
                                new BigDecimal("4200.00"),
                                true,
                                LocalDateTime.now(),
                                LocalDateTime.now());

                UpdateProductRequest request = new UpdateProductRequest(
                                "Notebook Dell",
                                "NB001",
                                "Informática",
                                20,
                                5,
                                new BigDecimal("4500.00"));

                when(repository.findById(id)).thenReturn(Optional.of(product));
                when(repository.existsBySku("NB001")).thenReturn(true);
                when(repository.save(any(ProductEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

                ProductResponse response = service.update(id, request);

                assertThat(response.name()).isEqualTo("Notebook Dell");
                assertThat(response.quantity()).isEqualTo(20);
                assertThat(response.unitPrice()).isEqualByComparingTo("4500.00");

                verify(repository).findById(id);
                verify(repository).existsBySku("NB001");
                verify(repository).save(product);
        }

        @Test
        void shouldNotUpdateWhenSkuBelongsToAnotherProduct() {
                UUID id = UUID.randomUUID();
                ProductEntity product = new ProductEntity(
                                id,
                                "Notebook",
                                "NB001",
                                "Informática",
                                10,
                                5,
                                new BigDecimal("4200.00"),
                                true,
                                LocalDateTime.now(),
                                LocalDateTime.now());

                UpdateProductRequest request = new UpdateProductRequest(
                                "Notebook Dell",
                                "MS001",
                                "Informática",
                                20,
                                5,
                                new BigDecimal("4500.00"));
                when(repository.findById(id)).thenReturn(Optional.of(product));
                when(repository.existsBySku("MS001")).thenReturn(true);
                assertThatThrownBy(() -> service.update(id, request))
                                .isInstanceOf(ProductSkuAlreadyExistsException.class);
                verify(repository, never()).save(any(ProductEntity.class));
        }

        @Test
        void shouldThrowExceptionWhenProductDoesNotExist() {
                UUID id = UUID.randomUUID();
                UpdateProductRequest request = new UpdateProductRequest(
                                "Notebook Dell",
                                "NB001",
                                "Informática",
                                20,
                                5,
                                new BigDecimal("4500.00"));

                when(repository.findById(id)).thenReturn(Optional.empty());
                assertThatThrownBy(() -> service.update(id, request)).isInstanceOf(ProductNotFoundException.class);
                verify(repository, never()).save(any(ProductEntity.class));
        }
}
