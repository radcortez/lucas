package com.radcortez.personal;

import com.basistech.rosette.api.RosetteAPI;
import com.basistech.rosette.apimodel.NameTranslationRequest;
import com.basistech.rosette.apimodel.NameTranslationResponse;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.radcortez.personal.integration.BehindTheName;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages.LanguageSet;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.PhoneticEngine;
import org.apache.commons.codec.language.bm.RuleType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Collections.singleton;

/**
 * @author Roberto Cortez
 */
public class NameUtils {
    private static final PhoneticEngine PHONETIC_ENGINE = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, true);
    private static final Lang LANG = Lang.instance(NameType.GENERIC);

    private static HttpClient CLIENT;
    private static ObjectMapper MAPPER;

    static {
        try {
            final JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            MAPPER = new XmlMapper(module);
            MAPPER.registerModule(new JaxbAnnotationModule());

            CLIENT = HttpClients.custom()
                                .setDefaultRequestConfig(
                                        RequestConfig.custom()
                                                     .setConnectTimeout(5000)
                                                     .setConnectionRequestTimeout(5000)
                                                     .build())
                                .setSSLSocketFactory(new SSLConnectionSocketFactory(
                                        SSLContexts.custom()
                                                   .loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true)
                                                   .build()))
                                .build();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isInLanguage(final String name, final String language) {
        return LANG.guessLanguages(name).contains(language);
    }

    public static boolean soundsLikeLanguage(final String name, final String language) {
        final String languageEncode = PHONETIC_ENGINE.encode(name, LanguageSet.from(singleton(language)));
        final String portugueseEncode = PHONETIC_ENGINE.encode(name, LanguageSet.from(singleton("portuguese")));

        for (String portuguese : portugueseEncode.split("\\|")) {
            if (languageEncode.contains(portuguese.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean sameTranslationAsLanguage(final String name, final LanguageCode language) {
        try {
            final NameTranslationRequest request = new NameTranslationRequest.Builder(name, language)
                    .entityType("PERSON")
                    .build();

            final RosetteAPI rosetteApi = new RosetteAPI.Builder()
                    .apiKey("ba4ce67775e318ebfa303d0773ccd7c3")
                    .build();

            final NameTranslationResponse response = rosetteApi.getNameTranslation(request);
            return StringUtils.equalsAnyIgnoreCase(name, response.getTranslation()) || response.getConfidence() < 0.5;
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean existsInLanguages(final String name, final String... languages) {
        HttpGet request = null;
        try {
            final URIBuilder uriBuilder = new URIBuilder("https://www.behindthename.com/api/lookup.php")
                    .addParameter("key", "ra947047")
                    .addParameter("name", name);

            request = new HttpGet(uriBuilder.build());
            final HttpResponse response = CLIENT.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                final BehindTheName behindTheName =
                        MAPPER.readValue(response.getEntity().getContent(), BehindTheName.class);

                if (behindTheName.getError() == null) {
                    return behindTheName.getNameDetails().stream()
                                        .filter(n -> n.getGender().equals("m"))
                                        .filter(n -> StringUtils.equalsIgnoreCase(n.getName(), name))
                                        .anyMatch(n -> n.getUsages()
                                                        .stream()
                                                        .filter(usage -> stream(languages).anyMatch(
                                                                l -> l.equals(usage.getCode())))
                                                        .count() == languages.length);
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            Optional.ofNullable(request).ifPresent(HttpRequestBase::releaseConnection);
        }

        return false;
    }
}

