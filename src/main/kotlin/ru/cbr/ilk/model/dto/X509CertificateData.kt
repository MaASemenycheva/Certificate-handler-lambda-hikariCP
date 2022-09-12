@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package ru.cbr.ilk.model.dto

import jdk.internal.joptsimple.internal.Strings
import ru.cbr.ilk.utils.base64toX509
import ru.cbr.ilk.utils.parseX500Name
import sun.security.x509.PrivateKeyUsageExtension
import sun.security.x509.X509CertImpl
import java.util.*

data class X509CertificateData (
            val certBeginDate: Date,
            val certExpiredDate: Date,
            val keyBeginDate: Date,
            val keyExpiredDate: Date,
            val orgO: String,
            val nameCn: String,
            val departmentOu: String,
            val oidValue: String,
            val dnCnCert: String
        ) {
    companion object {
        fun base64toX509CertificateData (base64string: String) = (base64string.let(::base64toX509) as X509CertImpl)
            .let {cert ->
                cert.subjectDN
                    .toString()
                    .let { subjectDn ->
                        subjectDn
                            .let(::parseX500Name)
                            .let { parts ->
                                X509CertificateData (
                                    cert.notBefore,
                                    cert.notAfter,
                                    cert.privateKeyUsageExtension.get(PrivateKeyUsageExtension.NOT_BEFORE),
                                    cert.privateKeyUsageExtension.get(PrivateKeyUsageExtension.NOT_AFTER),
                                    parts.getValue("O"),
                                    parts.getValue("CN"),
                                    parts.getValue("OU"),
                                    cert.sigAlgOID,
                                    subjectDn,
                                )
                            }
                    }
            }
    }
}


//data class X509CertificateData (
//    val certBeginDate: Date,
//    val certExpiredDate: Date,
//    val keyBeginDate: Date,
//    val keyExpiredDate: Date,
//    val orgO: String,
//    val nameCn: String,
//    val departmentOu: String,
//    val oidValue: String,
//    val dnCnCert: String
//) {
//    companion object {
//        fun base64toX509CertificateData (base64string: String) = base64string
//            ?.takeIf { !Strings.isNullOrEmpty(it) }?.apply {logger.info {"sdsds"}
//            } ?: let {  (::base64toX509 as X509CertImpl).let{cert ->
//            cert.subjectDN
//                .toString()
//                .let { subjectDn ->
//                    subjectDn
//                        .let(::parseX500Name)
//                        .let { parts ->
//                            X509CertificateData (
//                                cert.notBefore,
//                                cert.notAfter,
//                                cert.privateKeyUsageExtension.get(PrivateKeyUsageExtension.NOT_BEFORE),
//                                cert.privateKeyUsageExtension.get(PrivateKeyUsageExtension.NOT_AFTER),
//                                parts.getValue("O"),
//                                parts.getValue("CN"),
//                                parts.getValue("OU"),
//                                cert.sigAlgOID,
//                                subjectDn,
//                            )
//                        }
//                }}}as X509CertImpl}}
//
//
////            (::base64toX509 as X509CertImpl)            .let {cert ->
////                cert.subjectDN
////                    .toString()
////                    .let { subjectDn ->
////                        subjectDn
////                            .let(::parseX500Name)
////                            .let { parts ->
////                                X509CertificateData (
////                                    cert.notBefore,
////                                    cert.notAfter,
////                                    cert.privateKeyUsageExtension.get(PrivateKeyUsageExtension.NOT_BEFORE),
////                                    cert.privateKeyUsageExtension.get(PrivateKeyUsageExtension.NOT_AFTER),
////                                    parts.getValue("O"),
////                                    parts.getValue("CN"),
////                                    parts.getValue("OU"),
////                                    cert.sigAlgOID,
////                                    subjectDn,
////                                )
////                            }
////                    }
////            }
////// Else condition here
////        }
//
//
////        takeIf { !isNullOrEmpty(it) }.let(::base64toX509) as X509CertImpl)
//
////    }
////}