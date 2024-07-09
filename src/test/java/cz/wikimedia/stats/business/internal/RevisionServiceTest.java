package cz.wikimedia.stats.business.internal;

import cz.wikimedia.stats.business.external.HashtagsService;
import cz.wikimedia.stats.business.external.WmCommonsService;
import cz.wikimedia.stats.business.external.WmRevisionService;
import cz.wikimedia.stats.business.external.WmUserService;
import cz.wikimedia.stats.dao.EventRepository;
import cz.wikimedia.stats.dao.RevisionRepository;
import cz.wikimedia.stats.model.Event;
import cz.wikimedia.stats.model.Project;
import cz.wikimedia.stats.model.Revision;
import cz.wikimedia.stats.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
class RevisionServiceTest {

    @MockBean
    WmRevisionService wmRevisionService;

    @MockBean
    WmUserService wmUserService;

    @MockBean
    RevisionRepository revisionRepository;

    @MockBean
    HashtagsService hashtagsService;

    @MockBean
    EventRepository eventRepository;

    @MockBean
    EventService eventService;

    @MockBean
    WmCommonsService wmCommonsService;

    RevisionService service;
    Event event;
    Collection<Revision> revs;
    @BeforeEach
    void setUp() {
        User user1 = new User(1001L, "user1");
        User user2 = new User(1002L, "user2");
        User user3 = new User(1003L, "user3");

        Project project = new Project(2001L, "project", "path");
        event = new Event(2002L, Collections.emptySet(), null, "event", Event.DataCollectionStrategy.MANUAL, null, LocalDate.EPOCH, LocalDate.EPOCH, Set.of(user1, user2, user3), Set.of(project), new HashSet<>(), new HashSet<>());

        Revision rev0 = new Revision(0L, 420045L, -112L, 255L, 700500L, user1, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev1 = new Revision(1L, 253565L, 144L,  256L, 0L,      user2, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev2 = new Revision(2L, 968344L, 2033L, 998L, 0L,      user1, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev3 = new Revision(3L, 770110L, 0L,    323L, 885211L, user2, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev4 = new Revision(4L, 456988L, -3L,   345L, 112553L, user3, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev5 = new Revision(5L, 336054L, 2L,    820L, 512395L, user2, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev6 = new Revision(6L, 131521L, 0L,    165L, 441123L, user1, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev7 = new Revision(7L, 700500L, 2566L, 255L, 0L,      user2, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev8 = new Revision(8L, 152785L, 1441L, 144L, 0L,      user3, Set.of(event), project, Instant.EPOCH, "summary");
        Revision rev9 = new Revision(9L, 960044L, 7L,    924L, 123456L, user3, Set.of(event), project, Instant.EPOCH, "summary");

        revs = List.of(rev0, rev1, rev2, rev3, rev4, rev5, rev6, rev7, rev8, rev9);
        revs.forEach(event::addRevision);

        service = new RevisionService(revisionRepository, eventRepository);

        Mockito.when(wmRevisionService.getUserContribs(ArgumentMatchers.eq(event), ArgumentMatchers.any())).thenReturn(revs);
        Mockito.when(wmUserService.updateNames(List.of(user1, user2, user3))).thenReturn(List.of(user1, user2, user3));
        Mockito.when(hashtagsService.getRevisions(ArgumentMatchers.eq(event), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(revs);
        Mockito.when(eventRepository.existsById(2002L)).thenReturn(true);
        Mockito.when(eventRepository.findById(2002L)).thenReturn(Optional.of(event));
        Mockito.when(eventRepository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(420045L, project)).thenReturn(Optional.of(rev0));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(253565L, project)).thenReturn(Optional.of(rev1));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(968344L, project)).thenReturn(Optional.of(rev2));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(770110L, project)).thenReturn(Optional.of(rev3));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(456988L, project)).thenReturn(Optional.of(rev4));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(336054L, project)).thenReturn(Optional.of(rev5));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(131521L, project)).thenReturn(Optional.of(rev6));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(700500L, project)).thenReturn(Optional.of(rev7));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(152785L, project)).thenReturn(Optional.of(rev8));
        Mockito.when(revisionRepository.findRevisionByRevIdAndProject(960044L, project)).thenReturn(Optional.of(rev9));

        Mockito.when(revisionRepository.existsById(AdditionalMatchers.lt(10L))).thenReturn(true);
        Mockito.when(revisionRepository.existsById(AdditionalMatchers.gt(10L))).thenReturn(true);
        Mockito.when(revisionRepository.findById(0L)).thenReturn(Optional.of(rev0));
        Mockito.when(revisionRepository.findById(1L)).thenReturn(Optional.of(rev1));
        Mockito.when(revisionRepository.findById(2L)).thenReturn(Optional.of(rev2));
        Mockito.when(revisionRepository.findById(3L)).thenReturn(Optional.of(rev3));
        Mockito.when(revisionRepository.findById(4L)).thenReturn(Optional.of(rev4));
        Mockito.when(revisionRepository.findById(5L)).thenReturn(Optional.of(rev5));
        Mockito.when(revisionRepository.findById(6L)).thenReturn(Optional.of(rev6));
        Mockito.when(revisionRepository.findById(7L)).thenReturn(Optional.of(rev7));
        Mockito.when(revisionRepository.findById(8L)).thenReturn(Optional.of(rev8));
        Mockito.when(revisionRepository.findById(9L)).thenReturn(Optional.of(rev9));
        Mockito.when(revisionRepository.findById(AdditionalMatchers.gt(9L))).thenReturn(Optional.empty());

        Mockito.when(revisionRepository.save(ArgumentMatchers.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        Mockito.when(eventService.findById(2002L)).thenReturn(Optional.of(event));
    }

    @Test
    void create() {
        Mockito.when(revisionRepository.existsById(10L)).thenReturn(false).thenReturn(true);
        Mockito.when(revisionRepository.findById(10L)).thenReturn(Optional.of(new Revision(10L, 0L, 0L, 0L, 0L, new User(4L, "4"), Set.of(event), new Project(2001L, "project", "path"), Instant.EPOCH, "summary")));

        Assertions.assertTrue(service.create(revs.stream().findFirst().get()).isEmpty());
        Mockito.verifyNoInteractions(eventRepository);
        Mockito.verify(revisionRepository, Mockito.never()).save(ArgumentMatchers.any());

        Assertions.assertTrue(service.create(new Revision(10L, 0L, 0L, 0L, 0L, new User(4L, "4"), Set.of(event), new Project(2001L, "project", "path"), Instant.EPOCH, "summary")).isPresent());
        Mockito.verify(eventRepository, Mockito.atLeastOnce()).save(event);
    }

    @Test
    void update() {
        Revision rev11 = new Revision(11L, null, null, null, null, null, Collections.emptySet(), null, null, null);
        Mockito.when(revisionRepository.existsById(10L)).thenReturn(false);
        Mockito.when(revisionRepository.findById(11L)).thenReturn(Optional.of(rev11));
        Assertions.assertTrue(service.update(new Revision(10L, 0L, 0L, 0L, 0L, new User(4L, "4"), Set.of(event), new Project(2001L, "project", "path"), Instant.EPOCH, "summary")).isEmpty());
        Mockito.verifyNoInteractions(eventRepository);
        Mockito.verify(revisionRepository, Mockito.never()).save(ArgumentMatchers.any());

        Assertions.assertTrue(service.update(revs.stream().findFirst().get()).isPresent());
        Mockito.verify(revisionRepository, Mockito.atLeastOnce()).save(ArgumentMatchers.any());

        service.update(rev11);
        Mockito.verify(revisionRepository).deleteById(11L);
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        Mockito.verify(revisionRepository).deleteById(1L);
        Mockito.verify(eventRepository).save(event);
    }
}