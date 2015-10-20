/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.philippefichet.eslogfx;

import com.google.gson.Gson;
import fr.philippefichet.eslogfx.elasticsearch.Result;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

/**
 *
 * @author philippefichet
 */
public class ElasticSearchLogService extends ScheduledService<Result> {
    private CloseableHttpClient httpclient = null;
    private HttpClientContext context = HttpClientContext.create();
    private HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String string, SSLSession ssls) {
            return true;
        }
    };
    private String url;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    private SimpleDateFormat timestampToSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    private String fieldDate = null;
    private Date startAt = null;
    private Long timestamp = 0L;
    private int size = 1;
    private Config config;
    private String elasticSearchFilter = null;
    private boolean nextQueryWithElasticSearchFilter = false;

    public ElasticSearchLogService(Config config) {
        this.config = config;
        if (config.getDateFormat() != null) {
            if (Config.DATE_FORMAT_TIMESTAMP.equals(config.getDateFormat())) {
                sdf = null;
            } else {
                sdf = new SimpleDateFormat(config.getDateFormat());
            }
        }
        
        if (config.getDateFormatShow() != null) {
            timestampToSdf = new SimpleDateFormat(config.getDateFormatShow());
        }
        url = config.getUrl();
        fieldDate = config.getFieldDate();

        HttpClientBuilder builder = HttpClients.custom();
        try {
            // Autorisation
            if (config.getLogin() != null && config.getPassword() != null && config.getAuthScope() != null) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                    new AuthScope(config.getAuthScope(), 443),
                    new UsernamePasswordCredentials(config.getLogin(), config.getPassword())
                );
                context.setCredentialsProvider(credsProvider);
            }

            // Securité
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial((X509Certificate[] chain, String authType) -> {
                return true;
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                hostnameVerifier
            );
            builder.setSSLSocketFactory(sslsf);
        } catch(NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }
        httpclient = builder.build();
    }

    @Override
    protected Task<Result> createTask() {
        return new Task<Result>() {
            @Override
            protected Result call() throws Exception {
                updateProgress(-1, -1);
                if (fieldDate == null || startAt == null) {
                    size = 1;
                    if (fieldDate == null) {
                        updateMessage("Recherche d'information sur le mapping");
                        Result result = callElasticSearch(this::updateProgress);
                        result.getHits().getHits().stream().forEach((hit) -> {
                            hit.getSource().forEach((String k, String v) -> {
                                if (k.startsWith("_") == false) {
                                    try {
                                        if (sdf == null) {
                                            throw new IllegalStateException("Le champd date doit être remplis lors de l\'utilisattion d\'un format non standard de SimpleDateFormat");
                                        }
                                        sdf.parse(v);
                                        fieldDate = k;
                                        config.setFieldDate(fieldDate);
                                    } catch(ParseException ex) {}
                                }
                            });
                        });
                    }
                    startAt = new Date(System.currentTimeMillis() - config.getRewindSecond() * 1000);
                    size = 100;
                } else {
                    size = 100;
                }
                updateMessage("Demande de logs");
                Result result = callElasticSearch(this::updateProgress);
                updateMessage("Analyse des logs");
                result.getHits().getHits().stream().forEach((hit) -> {
                    if (sdf == null) {
                        if(Config.DATE_FORMAT_TIMESTAMP.equals(config.getDateFormat())) {
                            Long t = Long.parseLong(hit.getSource().get(fieldDate));
                            hit.getSource().put(fieldDate, timestampToSdf.format(new Date(t*1000)));
                            if (timestamp < t) {
                                timestamp = t;
                            }
                        } else {
                            throw new IllegalStateException("Format personnalité non autorisé : \"" + config.getDateFormat() + "\"");
                        }
                    } else {
                        try {
                            Date date = sdf.parse(hit.getSource().get(fieldDate));
                            if (startAt.before(date)) {
                                startAt = date;
                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(ElasticSearchLogService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                nextQueryWithElasticSearchFilter = false;
                updateMessage("Ajout des logs dans l'interface");
                return result;
            }
        };
    }

    /**
     * Création du corps de la requête elasticsearch
     * @return corps de la requête elasticsearch
     */
    protected String createBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
            sb.append("\"size\":").append(size);
            if (fieldDate != null) {
                sb.append(",\"sort\":[{\"").append(fieldDate).append("\":{\"order\":\"desc\"}}]");
            }
            sb.append(",\"query\":{");
                sb.append("\"filtered\":{");
                    // Filter
                    sb.append("\"filter\":{");
                    if (fieldDate != null && startAt != null && nextQueryWithElasticSearchFilter == false) {
                        sb.append("\"range\":{\"").append(fieldDate).append("\":{\"gt\":\"");
                        if (sdf == null) {
                            if (Config.DATE_FORMAT_TIMESTAMP.equals(config.getDateFormat())) {
                                sb.append(timestamp);
                            } else {
                                throw new IllegalStateException("Format personnalité non autorisé : \"" + config.getDateFormat() + "\"");
                            }
                        } else {
                            sb.append(sdf.format(startAt));
                        }
                        sb.append("\"}}");
                    }
                    sb.append("}");

                    // Query
                    sb.append(",\"query\":{");

                    if (nextQueryWithElasticSearchFilter) {
                        sb.append("match:{\"").append(config.getFieldMessage()).append("\":\"").append(elasticSearchFilter).append("\"}");
                    } else {
                        sb.append("\"match_all\":{}");
                    }
                    sb.append("}");
                sb.append("}");
            sb.append("}");
        sb.append("}");
        System.out.println("sb = " + sb.toString());
        return sb.toString();
    }

    /**
     * Execute un appel au serveur elasticsearch
     * @param update Mise à jour avec la taille télécharget et la taille total
     * @return Résultat de l'appel
     * @throws Exception Si une erreur est intervenue
     */
    protected Result callElasticSearch(BiConsumer<Long, Long> update) throws Exception {
        HttpPost elasticSearchPost = new HttpPost(url + "/_search?");
        elasticSearchPost.setEntity(new StringEntity(createBody()));
        CloseableHttpResponse response = httpclient.execute(elasticSearchPost, context);
        if(response.getStatusLine().getStatusCode() == 200) {
            long maxSize = response.getEntity().getContentLength();
            long sizeProgess = 0;
            update.accept(sizeProgess, maxSize);
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[2048];
            int bufferSize = 0;
            while((bufferSize = br.read(buffer)) > 0) {
                sb.append(buffer, 0, bufferSize);
                sizeProgess += bufferSize;
                update.accept(sizeProgess, maxSize);
            }
            Gson gson = new Gson();
            Result result = gson.fromJson(sb.toString(), Result.class);
            elasticSearchPost.completed();
            response.close();
            return result;
        } else {
            throw new Exception(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        }
    }

    public void nextQueryWithFilter(String filter) {
        nextQueryWithElasticSearchFilter = true;
        elasticSearchFilter = filter;
    }
}
