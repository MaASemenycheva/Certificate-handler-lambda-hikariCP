package ru.cbr.ilk

//import io.micrometer.core.instrument.Counter
import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.cbr.ilk.config.Configurer
import ru.cbr.ilk.config.ConfigurerImpl
import ru.cbr.ilk.model.converter.stateCertificateToCertificateDecode
import ru.cbr.ilk.service.CertificateDecodeConsumer
import ru.cbr.ilk.service.StateCertificateProvider
import java.sql.Connection
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess


private val logger = KotlinLogging.logger{}

@SpringBootApplication
class IlkApplication




@Timed
fun main(args: Array<String>) {
	runApplication<IlkApplication>(*args)
	try {
		val config: Configurer = ConfigurerImpl()
//		val connectionProvider: () -> Connection = PGSimpleDataSource().apply(config::configure)::getConnection
		val hikariConnectionProvider: () -> Connection = HikariDataSource().apply(config::hikariConfigure)::getConnection
		val meterRegistry = SimpleMeterRegistry()
		val countOfCert = 0
		val provider = StateCertificateProvider(config, hikariConnectionProvider, meterRegistry)
		val consumer = CertificateDecodeConsumer(config, hikariConnectionProvider, meterRegistry)
//		10.apply (config::getTotalNumberOfProcessedCertificates)
		Executors.newSingleThreadScheduledExecutor()
			.scheduleWithFixedDelay({

				try {
					val jsdj = provider.get()
					logger.info { "sdlmsldms" + jsdj[0].payload}
					logger.info {"lmmmmmmmmmmmmmmmmmmmmmmmmmm" + provider.get()
						.takeUnless(List<*>::isEmpty)
						?.map (::stateCertificateToCertificateDecode)
					}
					provider.get()
						.takeUnless(List<*>::isEmpty)
						?.map (::stateCertificateToCertificateDecode)
						?.let(consumer::put)
				}  catch (c: org.postgresql.util.PSQLException) {
					when {
						c.cause is java.net.ConnectException -> logger.warn{"Connection lost: ${c.message}"}
						// table doesn't exist
						c.sqlState == "42P01" -> logger.error { "Fatal PSQLException ${c.sqlState}, invalid structure: ${c.message}"}.also{ exitProcess(2) }
						// Database
						c.sqlState == "3D000" -> logger.error { "Fatal PSQLException ${c.sqlState}, invalid database: ${c.message}"}.also{ exitProcess(3) }

						c.sqlState == "42P01" -> logger.error { "Fatal PSQLException ${c.sqlState}, invalid credentails: ${c.message}"}.also{ exitProcess(4) }

						else -> logger.error{ "PSQLException ${c.sqlState} while polling database: ${c.message}"}
					}
				} catch (e: Exception){
					logger.error(e) {"Unhandled exception while pooling database"}
				}
			}, config.getInterval(), config.getInterval(), TimeUnit.MILLISECONDS)
	} catch (e: Exception) {
		logger.error { "Failed to start service, reason: ${e.message}" }
	}
}
