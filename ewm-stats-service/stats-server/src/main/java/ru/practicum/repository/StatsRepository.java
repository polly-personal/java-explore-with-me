package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    /*uris, unique*/
    @Query(value = "select count(distinct eh.ip) as hits, " +
            "eh.uri, " +
            "eh.app " +
            "from endpoints_hits as eh " +
            "where (cast(eh.time_stamp as date)) between :start and :end " +
            "and eh.uri in :uris " +
            "group by eh.uri, eh.app " +
            "order by hits desc",
            nativeQuery = true)
    List<ViewStatsProjection> getEndpointsHitsByDatesAndUrisWhereUniqueIp(LocalDateTime start, LocalDateTime end, /*@Param("uris")*/ List<String> uris);


    /*uris, null*/
    @Query(value = "select count(eh.uri) as hits, " +
            "eh.uri, " +
            "eh.app " +
            "from endpoints_hits as eh " +
            "where (cast(eh.time_stamp as date)) between :start and :end " +
            "and eh.uri in :uris " +
            "group by eh.uri, eh.app " +
            "order by hits desc",
            nativeQuery = true)
    List<ViewStatsProjection> getEndpointsHitsByDatesAndUris(LocalDateTime start, LocalDateTime end, /*@Param("uris")
     */ List<String> uris);

    /*null, unique*/
    @Query(value = "select count(distinct eh.ip) as hits, " +
            "eh.uri, " +
            "eh.app " +
            "from endpoints_hits as eh " +
            "where (cast(eh.time_stamp as date)) between ?1 and ?2 " +
            "group by eh.uri, eh.app " +
            "order by hits desc",
            nativeQuery = true)
    List<ViewStatsProjection> getEndpointsHitsByDatesWhereUniqueIp(LocalDateTime start, LocalDateTime end);

    /*null, null*/
    @Query(value = "select count(eh.uri) as hits, " +
            "eh.uri, " +
            "eh.app " +
            "from endpoints_hits as eh " +
            "where (cast(eh.time_stamp as date)) between ?1 and ?2 " +
            "group by eh.uri, eh.app " +
            "order by hits desc",
            nativeQuery = true)
    List<ViewStatsProjection> getEndpointsHitsOnlyByDates(LocalDateTime start, LocalDateTime end);
}
