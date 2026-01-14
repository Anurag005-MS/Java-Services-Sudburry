package com.et.SudburyCityPlatform.repository.events;

import com.et.SudburyCityPlatform.models.events.Event;
import com.et.SudburyCityPlatform.models.events.EventRegistration;
import com.et.SudburyCityPlatform.models.events.User;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRegistrationRepository
        extends JpaRepository<EventRegistration, Long> {

    boolean existsByEventAndUser(Event event, User user);
}