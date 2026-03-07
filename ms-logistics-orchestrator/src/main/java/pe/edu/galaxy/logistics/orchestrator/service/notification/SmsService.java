package pe.edu.galaxy.logistics.orchestrator.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    public void sendSms(String phoneNumber, String message) {
        log.info("[SMS SIMULADO] Enviando a {} -> {}", phoneNumber, message);
        // Aquí se integraría con Twilio, AWS SNS, etc.
    }
}
