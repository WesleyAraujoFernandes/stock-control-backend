package br.com.knowledge.stockcontrol_api.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.knowledge.stockcontrol_api.product.entity.ProductEntity;

@SpringBootTest
@Transactional
class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;

    @Test
    void shouldSaveAndFindProduct() {
        UUID id = UUID.randomUUID();

        ProductEntity product = new ProductEntity(
                id,
                "Produto de teste",
                "TEST-001",
                "testes",
                10,
                2,
                new BigDecimal("10.99"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now());
        repository.save(product);
        var result = repository.findById(id);
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Produto de teste");
        assertThat(result.get().getSku()).isEqualTo("TEST-001");

        repository.deleteById(id);
    }
}
