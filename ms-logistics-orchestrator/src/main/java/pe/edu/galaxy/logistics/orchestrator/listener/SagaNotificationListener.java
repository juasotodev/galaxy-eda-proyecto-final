package pe.edu.galaxy.logistics.orchestrator.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pe.edu.galaxy.logistics.orchestrator.event.SagaStateChangedEvent;
import pe.edu.galaxy.logistics.orchestrator.service.notification.EmailService;
import pe.edu.galaxy.logistics.orchestrator.service.notification.SmsService;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class SagaNotificationListener {

        private final EmailService emailService;
        private final SmsService smsService;

        @org.springframework.beans.factory.annotation.Value("${spring.notification.recipient-email}")
        private String recipientEmail;

        @Async
        @EventListener
        public void handleSagaStateChanged(SagaStateChangedEvent event) {
                log.info("Procesando notificaciones para Saga: {} ({} -> {})",
                                event.getSagaId(), event.getPreviousStatus(), event.getNextStatus());

                String message = String.format("Pedido %s: %s",
                                event.getSagaId(),
                                getFriendlyStatus(event.getNextStatus()));

                // Enviar Email
                emailService.sendEmail(
                                recipientEmail,
                                "Actualización de tu pedido: " + event.getSagaId(),
                                message);

                // Enviar SMS
                smsService.sendSms("+51999888777", "[GALAXY] " + message);
        }

        private String getFriendlyStatus(SagaStatus status) {
                if (status == null)
                        return "Procesando...";
                return switch (status) {
                        case STARTED -> "Hemos recibido tu pedido y estamos iniciando las validaciones.";
                        case STOCK_VALIDATED -> "¡Excelente! Tenemos stock disponible para tu compra.";
                        case ORDER_COMPLETED -> "¡Todo listo! Tu pedido ha sido confirmado exitosamente.";
                        case ORDER_FAILED -> "Tu pedido no pudo ser procesado por falta de inventario.";
                        case COMPENSATING -> "Estamos gestionando la cancelación de tu orden.";
                        case COMPENSATED -> "Tu pedido ha sido cancelado correctamente.";
                        default -> "Tu pedido se encuentra en estado: " + status.name();
                };
        }
}
