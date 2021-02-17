package it.mauiroma;

import org.jboss.logging.Logger;
import org.wildfly.security.auth.server.SecurityIdentity;
import org.wildfly.security.auth.server.event.SecurityAuthenticationFailedEvent;
import org.wildfly.security.auth.server.event.SecurityAuthenticationSuccessfulEvent;
import org.wildfly.security.auth.server.event.SecurityEvent;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;


public class ElytronAudit implements Consumer<SecurityEvent> {
    private static final Logger log = Logger.getLogger("ELYTRON");

    public ElytronAudit(){
        System.out.println("ElytronAudit");
    }

    public void accept(SecurityEvent securityEvent) {
        String ELYTRON_FORMAT_MESSAGE = System.getProperty("ELYTRON_FORMAT_MESSAGE","Date:{DATE} - User:{USER} - Outcome:{OUTCOME}");
        String ELYTRON_FORMAT_DATE = System.getProperty("ELYTRON_FORMAT_DATE","yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat dateFormat = new SimpleDateFormat(ELYTRON_FORMAT_DATE);
        String date = dateFormat.format(new Date());
        if (securityEvent instanceof SecurityAuthenticationSuccessfulEvent) {
            SecurityIdentity secId = securityEvent.getSecurityIdentity();
            Principal principal = secId.getPrincipal();
            log.info(ELYTRON_FORMAT_MESSAGE.replace("{DATE}", date).replace("{USER}", principal.getName()).replace("{OUTCOME}", "success"));
        } else if (securityEvent instanceof SecurityAuthenticationFailedEvent) {
            Principal principal = ((SecurityAuthenticationFailedEvent) securityEvent).getPrincipal();
            log.info(ELYTRON_FORMAT_MESSAGE.replace("{DATE}", date).replace("{USER}", principal.getName()).replace("{OUTCOME}", "refused"));
        }
    }
}