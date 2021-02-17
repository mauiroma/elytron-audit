# Elytron Audit

This EAP module is useful if you want customize the audit log output for elytron.

The application [servlet-security](servlet-security/README.adoc) has been took from [Jboss-Quickstart](https://github.com/jboss-developer/jboss-eap-quickstarts)   
The module [elytron-audit-module](elytron-audit-module) is the module that implements the [custom message](#Custom)

## Standard
Standard way to enable elytron audit log
```
/subsystem=elytron/file-audit-log=elytron_audit:add(path="my_audit.log",relative-to="jboss.server.log.dir",format=SIMPLE,synchronized=true)
/subsystem=elytron/security-domain=servlet-security-quickstart-sd:write-attribute(name=security-event-listener, value=elytron_audit)
```

## Custom
Custom way to enable elytron audit log 
If you don't define the two System Properties the default will be:
- ELYTRON_FORMAT_MESSAGE = Date:{DATE} - User:{USER} - Outcome:{OUTCOME}
- ELYTRON_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss"
```
/system-property=ELYTRON_FORMAT_MESSAGE:add(value="|Red Hat JBoss EAP|jboss-instance|jboss-ver|1|authentication|low|rt={DATE} duser={USER} ec.activity=login ec.outcome={OUTCOME}")
/system-property=ELYTRON_FORMAT_DATE:add(value="yyyy-MM-dd HH:mm:ss")

module add --name=it.mauiroma --resources=target/elytron-audit-module-1.0-SNAPSHOT.jar --dependencies=org.wildfly.security.elytron-private,org.jboss.logging

/subsystem=logging/file-handler=elytron_audit:add(file={path=elytron.log,relative-to=jboss.server.log.dir},formatter=%s%n)
/subsystem=logging/logger=ELYTRON:add(category=ELYTRON, handlers=[elytron_audit], use-parent-handlers=false)
/subsystem=elytron/custom-security-event-listener=elytron_audit:add(module=it.mauiroma, class-name=it.mauiroma.ElytronAudit)
/subsystem=elytron/security-domain=servlet-security-quickstart-sd:write-attribute(name=security-event-listener, value=elytron_audit)
```