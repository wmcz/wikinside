package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.UserRepository;
import cz.wikimedia.stats.dao.UserTagRepository;
import cz.wikimedia.stats.model.User;
import cz.wikimedia.stats.model.UserTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class UserTagServiceTest {

    @MockBean
    UserTagRepository repository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    EventRepository eventRepository;

    UserTagService service;

    @BeforeEach
    void setUp() {
        User userone = new User(1001L, "one");
        User usertwo = new User(1002L, "two");
        User userthree = new User(1003L, "three");

        UserTag one   = new UserTag(1L, "one", new HashSet<>(), new HashSet<>(), null, new HashSet<>());
        UserTag two   = new UserTag(2L, "two", new HashSet<>(List.of(userone, usertwo)), new HashSet<>(), one, new HashSet<>());
        UserTag three = new UserTag(3L, "three", new HashSet<>(List.of(usertwo, userthree)), new HashSet<>(), null, new HashSet<>(List.of(one, two)));


        service = new UserTagService(repository, userRepository, eventRepository);

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



        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Mockito.when(userRepository.findById(1001L)).thenReturn(Optional.of(userone));
        Mockito.when(userRepository.findById(1002L)).thenReturn(Optional.of(usertwo));
        Mockito.when(userRepository.findById(1003L)).thenReturn(Optional.of(userthree));
        Mockito.when(userRepository.existsById(1001L)).thenReturn(true);
        Mockito.when(userRepository.existsById(1002L)).thenReturn(true);
        Mockito.when(userRepository.existsById(1003L)).thenReturn(true);
        Mockito.when(userRepository.existsById(AdditionalMatchers.gt(1003L))).thenReturn(false);
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        Mockito.verify(repository).deleteById(1L);
        Mockito.verifyNoInteractions(userRepository);

        service.deleteById(3L);
        Mockito.verify(repository).deleteById(3L);
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(new User(1002L, "two"));
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(new User(1003L, "three"));
    }

    @Test
    void create() {
        Mockito.when(repository.findById(4L)).thenReturn(Optional.of(new UserTag(4L, "four", new HashSet<>(), new HashSet<>(), null, new HashSet<>())));
        Mockito.when(repository.findById(5L)).thenReturn(Optional.of(new UserTag(5L, "five", new HashSet<>(), new HashSet<>(), null, new HashSet<>())));
        Mockito.when(repository.findById(6L)).thenReturn(Optional.of(new UserTag(6L, "six", new HashSet<>(), new HashSet<>(), null, new HashSet<>())));

        Assertions.assertEquals(Optional.empty(), service.create(new UserTag(1L, "one", new HashSet<>(), new HashSet<>(), null, new HashSet<>())));
        Mockito.verify(repository, Mockito.never()).save(new UserTag(1L, "one", new HashSet<>(), new HashSet<>(), null, new HashSet<>()));
        Mockito.verifyNoInteractions(userRepository);

        Assertions.assertTrue(service.create(new UserTag(4L, "four", new HashSet<>(), new HashSet<>(), null, new HashSet<>())).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new UserTag(4L, "four", new HashSet<>(), null, null, null));
        Mockito.verifyNoInteractions(userRepository);

        Assertions.assertTrue(service.create(new UserTag(5L, "five", new HashSet<>(), new HashSet<>(), null, new HashSet<>(List.of(new UserTag(1L, "one", null, null, null, new HashSet<>()), new UserTag(2L, "two", null, null, null, new HashSet<>()))))).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new UserTag(5L, "five", null, null, null, null));
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new UserTag(1L, "one", null, null, null, null));
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new UserTag(2L, "two", null, null, null, null));
        Mockito.verifyNoInteractions(userRepository);


        Assertions.assertTrue(service.create(new UserTag(6L, "six", new HashSet<>(List.of(new User(1001L, "one"), new User(1002L, "two"))), new HashSet<>(), null, new HashSet<>())).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new UserTag(6L, "six", null, null, null, null));
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(new User(1001L, "one"));
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(new User(1002L, "two"));
    }

    @Test
    void update() {
        Mockito.when(repository.findById(4L)).thenReturn(Optional.empty());

        Assertions.assertTrue(service.update(new UserTag(4L, "four", Set.of(new User(1007L, "name")), new HashSet<>(), null, new HashSet<>())).isEmpty());
        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verifyNoInteractions(userRepository);

        Assertions.assertTrue(service.update(new UserTag(1L, "one", new HashSet<>(), new HashSet<>(), null, new HashSet<>())).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new UserTag(1L, "one", new HashSet<>(), new HashSet<>(), null, new HashSet<>()));
        Mockito.verifyNoInteractions(userRepository);

        Assertions.assertTrue(service.update(new UserTag(1L, "one", new HashSet<>(), new HashSet<>(), null, new HashSet<>(List.of(new UserTag(3L, "three", null, null, null, new HashSet<>()))))).isPresent());
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new UserTag(1L, "one", null, null, null, null));
        Mockito.verify(repository, Mockito.never())      .save(new UserTag(2L, "two", null, null, null, null));
        Mockito.verify(repository, Mockito.atLeastOnce()).save(new UserTag(3L, "three", null, null, null, null));
        Mockito.verifyNoInteractions(userRepository);


        Assertions.assertTrue(service.update(new UserTag(1L, "one", new HashSet<>(List.of(new User(1001L, "one"), new User(1002L, "two"))), new HashSet<>(), null, new HashSet<>())).isPresent());
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(new User(1001L, "one"));
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(new User(1002L, "two"));
    }
}