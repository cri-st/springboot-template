package st.cri.app.controller.config

import org.apache.logging.log4j.LogManager
import org.springframework.boot.CommandLineRunner
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Component


@Component
class InitializeChecker(private val environment: Environment) : CommandLineRunner {
    private val logger = LogManager.getLogger(
        InitializeChecker::class.java
    )

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        logger.info("CHEQUEANDO PROPERTIES...")
        executeCheck("FRONT APPLICATION URL", "application.front.host")
        logger.info("PROPIEDADES OBLIGATORIAS CHECKEADAS")
        logger.info("------------------------------------")
    }

    private fun executeCheck(name: String, property: String) {
        try {
            val value = environment.getRequiredProperty(property)
            if (environment.acceptsProfiles(Profiles.of("!prod"))) {
                logger.info("$name : $value")
            }
        } catch (e: Exception) {
            logger.error("NO INICIO LA APLICACION POR AUSENCIA DE PROPERTY: $name")
            throw RuntimeException(e)
        }
    }
}
