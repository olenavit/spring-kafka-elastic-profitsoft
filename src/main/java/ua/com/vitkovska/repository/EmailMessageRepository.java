package ua.com.vitkovska.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.vitkovska.data.EmailStatus;
import ua.com.vitkovska.data.EmailData;

import java.util.List;

/**
 * Repository interface for managing {@link EmailData} entities.
 * This interface extends {@link CrudRepository} to provide CRUD operations for EmailData entities.
 */
@Repository
public interface EmailMessageRepository extends CrudRepository<EmailData,String> {

    /**
     * Finds all {@link EmailData} entities with the specified status.
     *
     * @param status the status of the emails to find
     * @return a list of {@link EmailData} entities with the specified status
     */
    List<EmailData> findByStatus(EmailStatus status);
}
