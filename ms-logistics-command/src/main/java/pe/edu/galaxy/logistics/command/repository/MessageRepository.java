package pe.edu.galaxy.logistics.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.galaxy.logistics.command.entity.MessageEntity;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m WHERE m.status = 'PENDING'")
    List<MessageEntity> findPendingMessages();

    @Modifying
    @Query("UPDATE MessageEntity m SET m.status = 'PROCESSED' WHERE m.id = :id")
    void markAsProcessed(@Param("id") Long id);
}
