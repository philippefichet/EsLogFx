/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

{"took":1336,"timed_out":false,"_shards":{"total":5,"successful":5,"failed":0},"hits":{"total":3634177,"max_score":1.0,"hits":[{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dlV0fU1lfW-YViq","_score":1.0,"_source":{"date":"2015-05-30 19:15:00,054","@timestamp":"2015-05-30T19:15:00.054+0200","level":"INFO","service":"ForkJoinPool.commonPool-worker-6","logger":"stdout","host":"sd-69122","message":"19:15:00.054 [ForkJoinPool.commonPool-worker-6] INFO  fr.feedreader.buisness.FeedBuisness - R??cup??ration du flux \"http://blogs.oracle.com/theaquarium/feed/entries/atom\" en : 48952010 ns."}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dlb0fU1lfW-YViv","_score":1.0,"_source":{"date":"2015-05-30 19:15:00,071","@timestamp":"2015-05-30T19:15:00.071+0200","level":"INFO","service":"ForkJoinPool.commonPool-worker-0","logger":"stdout","host":"sd-69122","message":"19:15:00.071 [ForkJoinPool.commonPool-worker-0] INFO  fr.feedreader.buisness.FeedBuisness - R??cup??ration du flux \"http://www.developpez.com/index/atom\" ..."}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dld0fU1lfW-YVix","_score":1.0,"_source":{"date":"2015-05-30 19:15:00,073","@timestamp":"2015-05-30T19:15:00.073+0200","level":"INFO","service":"ForkJoinPool.commonPool-worker-3","logger":"stdout","host":"sd-69122","message":"19:15:00.073 [ForkJoinPool.commonPool-worker-3] INFO  fr.feedreader.buisness.FeedBuisness - R??cup??ration du flux \"http://linuxfr.org/news.atom\" ..."}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dll0fU1lfW-YVi8","_score":1.0,"_source":{"date":"2015-05-30 19:15:00,166","@timestamp":"2015-05-30T19:15:00.166+0200","level":"INFO","service":"ForkJoinPool.commonPool-worker-3","logger":"stdout","host":"sd-69122","message":"19:15:00.166 [ForkJoinPool.commonPool-worker-3] INFO  fr.feedreader.buisness.FeedBuisness - R??cup??ration du flux \"http://linuxfr.org/news.atom\" en : 93604778 ns."}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dlr0fU1lfW-YVjD","_score":1.0,"_source":{"date":"2015-05-30 19:15:00,204","@timestamp":"2015-05-30T19:15:00.204+0200","level":"INFO","service":"EJB default - 8","logger":"stdout","host":"sd-69122","message":"19:15:00.204 [EJB default - 8] INFO  fr.feedreader.buisness.FeedBuisness - R??cup??ration du flux \"https://vaadin.com/blog/-/blogs/rss?_33_max=30\" ..."}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dlt0fU1lfW-YVjF","_score":1.0,"_source":{"date":"2015-05-30 19:15:00,288","@timestamp":"2015-05-30T19:15:00.288+0200","level":"INFO","service":"ForkJoinPool.commonPool-worker-1","logger":"stdout","host":"sd-69122","message":"19:15:00.288 [ForkJoinPool.commonPool-worker-1] INFO  fr.feedreader.buisness.FeedBuisness - R??cup??ration du flux \"http://lescastcodeurs.libsyn.com/rss\" ..."}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dlw0fU1lfW-YVjK","_score":1.0,"_source":{"date":"2015-05-30 19:15:00,399","@timestamp":"2015-05-30T19:15:00.399+0200","level":"INFO","service":"ForkJoinPool.commonPool-worker-3","logger":"stdout","host":"sd-69122","message":"19:15:00.398 [ForkJoinPool.commonPool-worker-3] INFO  fr.feedreader.buisness.FeedBuisness - R??cup??ration du flux \"http://www.infoq.com/feed/java\" en : 231789195 ns."}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dl00fU1lfW-YVjP","_score":1.0,"_source":{"date":"2015-05-30 19:15:01,167","@timestamp":"2015-05-30T19:15:01.167+0200","level":"INFO","service":"ForkJoinPool.commonPool-worker-6","logger":"stdout","host":"sd-69122","message":"19:15:01.167 [ForkJoinPool.commonPool-worker-6] INFO  fr.feedreader.buisness.FeedBuisness - R??cup??ration du flux \"http://blog.soat.fr/feed/\" en : 1112115223 ns."}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dl50fU1lfW-YVjV","_score":1.0,"_source":{"date":"2015-05-30 19:15:01,761","@timestamp":"2015-05-30T19:15:01.761+0200","level":"INFO","service":"EJB default - 8","logger":"stdout","host":"sd-69122","message":"19:15:01.760 [EJB default - 8] INFO  fr.feedreader.buisness.FeedBuisness - Article existant : http://www.developpez.com/actu/85919/PHP-7-0-sortira-en-version-stable-le-12-novembre-2015-la-premiere-Alpha-sera-publiee-le-11-juin-prochain/"}},{"_index":"wildfly","_type":"wildfly","_id":"AU2l0dl_0fU1lfW-YVjd","_score":1.0,"_source":{"date":"2015-05-30 19:15:01,798","@timestamp":"2015-05-30T19:15:01.798+0200","level":"INFO","service":"EJB default - 8","logger":"stdout","host":"sd-69122","message":"19:15:01.798 [EJB default - 8] INFO  fr.feedreader.buisness.FeedBuisness - Article existant : http://magazine.developpez.com/"}}]}}


 */
package fr.philippefichet.eslogfx.elasticsearch;

/**
 *
 * @author philippefichet
 */
public class Result {
    private Long took;
    private boolean timedOut;
    private Shard _shards;
    private Hits hits;

    public Long getTook() {
        return took;
    }

    public void setTook(Long took) {
        this.took = took;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    public Shard getShards() {
        return _shards;
    }

    public void setShards(Shard shards) {
        this._shards = shards;
    }

    public Hits getHits() {
        return hits;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }
}
