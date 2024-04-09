package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.EventTagRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.EventTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class EventTagServiceTest {

    @MockBean
    EventTagRepository repository;

    @MockBean
    EventRepository eventRepository;

    EventTagService service;
    @BeforeEach
    void setUp() {
        Event eventone = new Event(1001L, new HashSet<>(), "one", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Event eventtwo = new Event(1002L, new HashSet<>(), "two", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        Event eventthree = new Event(1003L, new HashSet<>(),"three", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());

        EventTag one   = new EventTag(1L, "one", new HashSet<>(), null, new HashSet<>());
        EventTag two   = new EventTag(2L, "two", new HashSet<>(List.of(eventone, eventtwo)), one, new HashSet<>());
        EventTag three = new EventTag(3L, "three", new HashSet<>(List.of(eventtwo, eventthree)), null, new HashSet<>(List.of(one, two)));



        service = new EventTagService(repository, eventRepository);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(one));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(two));
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(three));
        Mockito.when(repository.existsById(1L)).thenReturn(true);
        Mockito.when(repository.existsById(2L)).thenReturn(true);
        Mockito.when(repository.existsById(3L)).thenReturn(true);
        Mockito.when(repository.existsById(4L)).thenReturn(false).thenReturn(true);
        Mockito.when(repository.existsById(5L)).thenReturn(false).thenReturn(true);
        Mockito.when(repository.existsById(6L)).thenReturn(false).thenReturn(true);

        Mockito.when(repository.existsById(AdditionalMatchers.gt(6L))).thenReturn(false);



        Mockito.when(eventRepository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Mockito.when(eventRepository.findById(1001L)).thenReturn(Optional.of(eventone));
        Mockito.when(eventRepository.findById(1002L)).thenReturn(Optional.of(eventtwo));
        Mockito.when(eventRepository.findById(1003L)).thenReturn(Optional.of(eventthree));
        Mockito.when(eventRepository.existsById(1001L)).thenReturn(true);
        Mockito.when(eventRepository.existsById(1002L)).thenReturn(true);
        Mockito.when(eventRepository.existsById(1003L)).thenReturn(true);
        Mockito.when(eventRepository.existsById(AdditionalMatchers.gt(1003L))).thenReturn(false);
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        Mockito.verify(repository).deleteById(1L);
        Mockito.verifyNoInteractions(eventRepository);

        service.deleteById(3L);
        Mockito.verify(repository).deleteById(3L);
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1002L, new HashSet<>(), "two", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1003L, new HashSet<>(), "three", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));
    }

    @Test
    void create() {
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(new EventTag(4L, "four", new HashSet<>(), null, new HashSet<>())));
        Mockito.when(repository.findById(5L)).thenReturn(Optional.of(new EventTag(5L, "five", new HashSet<>(), null, new HashSet<>())));
        Mockito.when(repository.findById(6L)).thenReturn(Optional.of(new EventTag(6L, "six", new HashSet<>(), null, new HashSet<>())));

        Assertions.assertEquals(Optional.empty(), service.create(new EventTag(1L, "one", new HashSet<>(), null, new HashSet<>())));
        Mockito.verify(repository, Mockito.never()).save(new EventTag(1L, "one", new HashSet<>(), null, new HashSet<>()));
        Mockito.verifyNoInteractions(eventRepository);

        Assertions.assertTrue(service.create(new EventTag(4L, "four", new HashSet<>(), null, new HashSet<>())).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new EventTag(4L, "four", null, null, null));
        Mockito.verifyNoInteractions(eventRepository);

        Assertions.assertTrue(service.create(new EventTag(5L, "five", new HashSet<>(), null, new HashSet<>(List.of(new EventTag(1L, "one", null, null, new HashSet<>()), new EventTag(2L, "two", null, null, new HashSet<>()))))).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new EventTag(5L, "five", null, null, null));
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new EventTag(1L, "one", null, null, null));
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new EventTag(2L, "two", null, null, null));
        Mockito.verifyNoInteractions(eventRepository);


        Assertions.assertTrue(service.create(new EventTag(6L, "six", new HashSet<>(List.of(new Event(1001L, new HashSet<>(), "one", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()), new Event(1002L, new HashSet<>(), "two", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()))), null, new HashSet<>())).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new EventTag(6L, "six", null, null, null));
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1001L, new HashSet<>(), "one", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1002L, new HashSet<>(), "two", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));
    }

    @Test
    void update() {
        Mockito.when(repository.findById(4L)).thenReturn(Optional.empty());

        Assertions.assertTrue(service.update(new EventTag(4L, "four", Set.of(new Event(1007L, new HashSet<>(), "seven", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>())), null, new HashSet<>())).isEmpty());
        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verifyNoInteractions(eventRepository);

        Assertions.assertTrue(service.update(new EventTag(1L, "one", new HashSet<>(), null, new HashSet<>())).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new EventTag(1L, "one", new HashSet<>(), null, new HashSet<>()));
        Mockito.verifyNoInteractions(eventRepository);

        Assertions.assertTrue(service.update(new EventTag(1L, "one", new HashSet<>(), null, new HashSet<>(List.of(new EventTag(3L, "three", null, null, new HashSet<>()))))).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new EventTag(1L, "one", null, null, null));
        Mockito.verify(repository, Mockito.never())      .save(new EventTag(2L, "two", null, null, null));
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new EventTag(3L, "three", null, null, null));
        Mockito.verifyNoInteractions(eventRepository);


        Assertions.assertTrue(service.update(new EventTag(1L, "one", new HashSet<>(List.of(new Event(1001L, new HashSet<>(), "one", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()), new Event(1002L, new HashSet<>(), "two", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()))), null, new HashSet<>())).isPresent());
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1001L, new HashSet<>(), "one", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(new Event(1002L, new HashSet<>(), "two", null, null, null, null, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>()));
    }
}