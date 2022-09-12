package ru.cbr.ilk.utils

import mu.KotlinLogging
import java.io.ByteArrayInputStream
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*

val x509CertificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")

private val logger = KotlinLogging.logger{}

fun base64toX509(encodedCert: String) = encodedCert
    .let(
        Base64.getDecoder()::decode)
//    .let {
//        try {
//            ::ByteArrayInputStream
//        } catch (e: Exception) {
//            null
//        }
//    }
    .let(::ByteArrayInputStream)
//    .let {
//        try {
//            x509CertificateFactory::generateCertificate
//        } catch (e: Exception) {
//            null
//        }
//    }
//    .takeIf { isNullOrEmpty(x509CertificateFactory::generateCertificate.toString()) }?:
//    .let({}
//        x509CertificateFactory::generateCertificate
////        logger.info{"ksdjskjdksjd "+ x509CertificateFactory::generateCertificate as X509Certificate}
//    ) as X509Certificate



//    .also{logger.info{"kansknas " + x509CertificateFactory.generateCertificate(it) as X509Certificate }}


    .also{try{
        x509CertificateFactory.generateCertificate(it) as X509Certificate
    }
    catch (e: CertificateException) {
        logger.error("Certificate format error")
    }}
//        logger.info{"kansknas " + x509CertificateFactory.generateCertificate(it) as X509Certificate }}

//    .let(x509CertificateFactory::generateCertificate) as X509Certificate
//        logger.warn { "Failed to retrieve valid database connection" }} as X509Certificate
//    .let(x509CertificateFactory::generateCertificate as X509Certificate)?:
//    let{
//        logger.warn { "Failed to retrieve valid database connection" }
//        x509CertificateFactory::generateCertificate as X509Certificate}

//    ?: let {
//        ru.cbr.ilk.service.logger.warn { "Failed to retrieve valid database connection" }
//        emptyList()
//    }
//.takeIf { it !is X509Certificate } ?: logger.info { "Not a person!"}


//    .takeIf { it !is X509Certificate } ?: logger.info { "Not a person!"}

//.takeIf { it !is  }?.apply {
//// Provides you list if not empty
//        x509CertificateFactory::generateCertificate as X509Certificate
//} ?: run {
//    logger.error { "Certificate has error!"}
//// Else condition here
//}
//        as X509Certificate

//.let {
//    try {
//        x509CertificateFactory::generateCertificate as X509Certificate
//    } catch (e: java.lang.Exception) {
//        logger.error("cert format error,cert content is [")
//    }
//}

//.takeIf { it!= null }
//as X509Certificate




val splitX500NameRegex = "(\\$+)=(,*?)(,|$)".toRegex(RegexOption.DOT_MATCHES_ALL)

fun parseX500Name(name: String) = name.let(splitX500NameRegex::findAll).map {it.groupValues.get(1) to it.groupValues.get(2)}.toMap()