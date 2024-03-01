package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking as b join fetch b.booker as u " +
            "join fetch b.item as i " +
            "WHERE i.id=?1 AND u.id=?2 AND b.end<?3 ORDER BY b.start ")
    List<Booking> findByItemAndBooker(Long itemId, Long bookerId, LocalDateTime now);

    @Override
    @EntityGraph("Bookings.bookerAndItem")
    Optional<Booking> findById(Long aLong);


    @Query("select b from Booking as b join fetch b.booker as u " +
            "join fetch b.item as i " +
            "WHERE i.id=?1 AND b.start>?2 AND b.status=?3 ORDER BY b.start ")
    List<Booking> findNextByItem(Long aLong, LocalDateTime now, Status status);

    @Query("select b from Booking as b join fetch b.booker as u " +
            "join fetch b.item as i " +
            "WHERE i.id=?1 AND b.start<?2 AND b.status=?3 ORDER BY b.start DESC ")
    List<Booking> findLastByItem(Long aLong, LocalDateTime now, Status status);

    //методы получения бронирований от букера
    @Query("select b from Booking as b join fetch b.booker as u " +
            " WHERE u.id=?1 ")
    List<Booking> getAllBookingOfBooker(Long bookerId, Pageable pageable);

    @Query("select b from Booking as b join fetch b.booker as u " +
            " WHERE u.id=?1 and b.end<?2 ")
    List<Booking> getPastBookingOfBooker(Long bookerId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking as b join fetch b.booker as u " +
            " WHERE u.id=?1 and b.start>?2 ")
    List<Booking> getFutureBookingOfBooker(Long bookerId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking as b join fetch b.booker as u " +
            " WHERE u.id=?1 and b.start<?2 AND b.end>?2 ")
    List<Booking> getCurrentBookingOfBooker(Long bookerId, LocalDateTime now, Pageable pageable);


    @Query("select b from Booking as b join fetch b.booker as u " +
            " WHERE u.id=?1 and b.status=?2 ")
    List<Booking> getRejectedOrWaitingBookingOfBooker(Long bookerId, Status status, Pageable pageable);

    //методы получения бронирований от хозяина
    @Query("select b from Booking as b join fetch b.booker as u " +
            "join fetch b.item as i " +
            "join fetch i.user as ow " +
            "WHERE ow.id=?1 ")
    List<Booking> getAllBookingOfOwner(Long bookerId, Pageable pageable);

    @Query("select b from Booking as b join fetch b.booker as u " +
            "join fetch b.item as i " +
            "join fetch i.user as ow " +
            "WHERE ow.id=?1 AND b.end<?2 ")
    List<Booking> getPastBookingOfOwner(Long bookerId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking as b join fetch b.booker as u " +
            "join fetch b.item as i " +
            "join fetch i.user as ow " +
            "WHERE ow.id=?1 AND b.start>?2 ")
    List<Booking> getFutureBookingOfOwner(Long bookerId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking as b join fetch b.booker as u " +
            "join fetch b.item as i " +
            "join fetch i.user as ow " +
            "WHERE ow.id=?1 AND b.start<?2 AND b.end>?2 ")
    List<Booking> getCurrentBookingOfOwner(Long bookerId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking as b join fetch b.booker as u " +
            "join fetch b.item as i " +
            "join fetch i.user as ow " +
            "WHERE ow.id=?1 AND b.status=?2 ")
    List<Booking> getRejectedOrWaitingBookingOfOwner(Long bookerId, Status status, Pageable pageable);


}
