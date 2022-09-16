package ru.cbr.ilk.config

import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import mu.KotlinLogging
import org.postgresql.ds.common.BaseDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.cbr.ilk.let
import ru.cbr.ilk.service.CertificateDecodeConsumer
import ru.cbr.ilk.service.StateCertificateProvider
import java.sql.Connection
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


private val logger = KotlinLogging.logger{}

@Service
class ConfigurerImpl: Configurer {
//    private val dsServerNames: Array<String> = getRequiredParameter("DS_SERVER_NAMES").split(',').toTypedArray()
//    private val dsPortNumbers: IntArray = getRequiredParameter("DS_PORT_NUMBERS").split(',').map(String::toInt).toTypedArray().toIntArray()
//    private val dsDatabaseName: String = getRequiredParameter("DS_DATABASE_NAME")
//    private val dsUser: String = getRequiredParameter("DS_USER")
//    private val dsPassword: String = getRequiredParameter("DS_PASSWORD")
//
//    private val dsBatchSize: Int = getOptionalParameter("DS_BATCH_SIZE")?.toInt()?.takeIf { it > 1 } ?:1
//    private val dsValidityTimeout: Int = getRequiredParameter("DS_VALIDITY_TIMEOUT").toInt().takeIf{ it > 10 } ?: 10
//    private val dsPoolingInterval: Long = getRequiredParameter("DS_POOLING_INTERVAL").toLong().takeIf{ it > 10 } ?: 10

    private val dsServerNames: Array<String> = arrayOf("localhost")
    private val dsPortNumbers: IntArray = intArrayOf(5432)
    private val dsDatabaseName: String = "test"
    private val dsUser: String = "postgres"
    private val dsPassword: String = "admin"

    private val dsBatchSize: Int = 10
    private val dsValidityTimeout: Int = 10
    private val dsPoolingInterval: Long = 10

    @Suppress("SameParameterValue")
    private fun getOptionalParameter(name: String): String? = name.let(System::getenv)
        .takeUnless { it.isNullOrBlank() }

    private fun getRequiredParameter(name: String): String = name.let(::getOptionalParameter)
        .let(::requireNotNull) {"Failed to read parameter $name" }
         ?: "UNSET"

    override fun configure(ds: BaseDataSource) =ds.apply {
        serverNames = dsServerNames
        portNumbers = dsPortNumbers
        databaseName = dsDatabaseName
        user = dsUser
        password = dsPassword
    }

    override fun hikariConfigure(ds: HikariDataSource) =ds.apply {
//        jdbcUrl = "jdbc:postgresql://$dsServerNames:$dsPortNumbers.to/$dsDatabaseName"
//        jdbcUrl = "jdbc:postgresql://\"+dsServerNames+\":5432/test"
        jdbcUrl = "jdbc:postgresql://localhost:5432/test"
        username = dsUser
        password = dsPassword
        driverClassName = "org.postgresql.Driver"
        dataSourceProperties = mapOf("cachePrepStmts" to "true").toProperties()
        dataSourceProperties = mapOf("prepStmtCacheSize" to "250").toProperties()
        dataSourceProperties = mapOf("prepStmtCacheSqlLimit" to "2048").toProperties()
    }

    override fun getBatchSize(): Int = dsBatchSize
    override fun getTimeout(): Int = dsValidityTimeout
    override fun getInterval(): Long = dsPoolingInterval

    override fun getNumberOfCertificateProcessingErrorsInTheLastHour(): Long {
        TODO("Not yet implemented")
    }


    override fun getNumberOfProcessedCertificatesInTheLastHour(): Long {
        TODO("Not yet implemented")
    }



    override fun getTotalNumberOfErrorsWhileProcessingCertificates(): Long {
        TODO("Not yet implemented")
    }



    private lateinit var certificateCounter: Counter

    private lateinit var operationCounter: Counter

    private lateinit var lastCertificateLength: AtomicInteger

    private lateinit var certificateCount: AtomicInteger

    private lateinit var certificates: MutableList<String>

    private lateinit var timer: Timer
    val price = AtomicInteger()

    override fun getTotalNumberOfProcessedCertificates(count: Int): Int {
////        System.out.println("dfdfdf $count")
//        val config: Configurer = ConfigurerImpl()
//        val hikariConnectionProvider: () -> Connection = HikariDataSource().apply(config::hikariConfigure)::getConnection
        val meterRegistry = SimpleMeterRegistry()
//        val consumer = CertificateDecodeConsumer(config, hikariConnectionProvider, meterRegistry)


        price.set(count)
        logger.info{"lkhk,hkash "+ price}

//        lastCertificateLength = meterRegistry.gauge("service.certificate.last.cert.sdsfdsdsdsd", AtomicInteger())!!
//        logger.info{"lsmdms "+consumer.count}
        certificateCounter = meterRegistry.counter("service.certificate.countersdsds")

        certificateCounter.increment()
        logger.info{ "certificateCounter = $certificateCounter" }
        logger.info{"lsmdms "+ count}
        lastCertificateLength = price
        logger.info { "lastCertificateLength = $lastCertificateLength" }

        return count
    }

    private val responses = listOf(
        "Hello World",
        "Ala ma kota",
        "Hello Spring World",
        "Secret message for your eyes only",
    )

//    @Autowired
    fun setCounter(meterRegistry: MeterRegistry) {
        //counters -> increment value (счетчики -> значение приращения)
        certificateCounter = meterRegistry.counter("service.certificate.countersdsd")
        operationCounter = meterRegistry.counter("service.certificate.long.operation.counter")

        //gauges -> shows the current value of a meter. (датчики -> показывает текущее значение meter.)
        lastCertificateLength = meterRegistry.gauge("service.certificate.last.message.skdksdkskdksds", price)!!
        //shows collection size (queue message, cache size etc...). In real app the collection implementation used should be thread safe.
        //показывает размер коллекции (сообщение в очереди, размер кеша и т. д.). В реальном приложении используемая реализация коллекции должна быть потокобезопасной.
        certificates = meterRegistry.gaugeCollectionSize("service.certificate.message.size", emptyList(), mutableListOf())!!
        certificates.addAll(responses)

        //timer -> measures the time taken for short tasks and the count of these tasks.
        //таймер -> измеряет время, затрачиваемое на короткие задачи, и количество этих задач.
        timer = meterRegistry.timer("service.certificate.long.operation.run.timer")

    }

    override fun getMessage(): String {
        certificateCounter.increment()

        val certificate = getRandomCertificate()

        lastCertificateLength.set(certificate.length)

        return certificate
    }

    @Scheduled(fixedDelay = 3000)
    @Throws(InterruptedException::class)
    fun sampleLongOperation() {
        operationCounter.increment()

        val startTime = System.nanoTime()

        val randomLongOperation = Random().nextInt(7000).toLong()

        Thread.sleep(randomLongOperation)

        timer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
    }

    private fun getRandomCertificate(): String {
        return responses[Random().nextInt(responses.size)]
    }
}