package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.model.IdAble;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class InternalServiceTest {
    record IDAbleLong(Long val) implements IdAble<Long> {

        @Override
        public Long getId() {
            return val;
        }

    }
    class TestService extends InternalService<IDAbleLong, Long> {
        public TestService(CrudRepository<IDAbleLong, Long> repository) {
            super(repository);
        }
    }

    @MockBean
    CrudRepository<IDAbleLong, Long> repository;

    TestService testService;

    @BeforeEach
    void setUp() {
        testService = new TestService(repository);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Mockito.when(repository.findAll()).thenReturn(List.of(new IDAbleLong(2L), new IDAbleLong(3L)));
        Mockito.when(repository.count()).thenReturn(2L);
        Mockito.doNothing().when(repository).delete(ArgumentMatchers.any());
        Mockito.doNothing().when(repository).deleteById(ArgumentMatchers.anyLong());


        Mockito.when(repository.existsById(0L)).thenReturn(false);
        Mockito.when(repository.existsById(1L)).thenReturn(false);

        Mockito.when(repository.existsById(2L)).thenReturn(true);
        Mockito.when(repository.existsById(3L)).thenReturn(true);

        Mockito.when(repository.findById(0L)).thenReturn(Optional.empty());
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(new IDAbleLong(2L)));
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(new IDAbleLong(3L)));

    }

    @Test
    void count() {

        Assertions.assertEquals(2, testService.count());
        Mockito.verify(repository).count();

        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById() {
        testService.deleteById(2L);
        Mockito.verify(repository).deleteById(2L);
    }

    @Test
    void existsById() {
        Assertions.assertFalse(testService.existsById(1L));
        Assertions.assertTrue(testService.existsById(2L));

        Mockito.verify(repository, Mockito.times(1)).existsById(1L);
        Mockito.verify(repository, Mockito.times(1)).existsById(2L);

        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any());


    }

    @Test
    void findById() {
        Assertions.assertEquals(Optional.empty(), testService.findById(1L));
        Assertions.assertEquals(Optional.of(new IDAbleLong(2L)), testService.findById(2L));

        Mockito.verify(repository).findById(1L);
        Mockito.verify(repository).findById(2L);

        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any());

    }

    @Test
    void findAll() {
        Iterable<IDAbleLong> all = testService.findAll();

        Assertions.assertEquals(2, all.spliterator().getExactSizeIfKnown());

        Mockito.verify(repository).findAll();
        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any());

    }

    @Test
    void create() {
        Assertions.assertEquals(Optional.empty(), testService.create(new IDAbleLong(2L)));
        Assertions.assertEquals(Optional.of(new IDAbleLong(1L)), testService.create(new IDAbleLong(1L)));

        Mockito.verify(repository, Mockito.never()).save(new IDAbleLong(2L));
        Mockito.verify(repository).save(new IDAbleLong(1L));

    }

    @Test
    void update() {
        Assertions.assertEquals(Optional.empty(), testService.update(new IDAbleLong(1L)));
        Assertions.assertEquals(Optional.of(new IDAbleLong(2L)), testService.update(new IDAbleLong(2L)));

        Mockito.verify(repository, Mockito.never()).save(new IDAbleLong(1L));
        Mockito.verify(repository).save(new IDAbleLong(2L));
    }
}