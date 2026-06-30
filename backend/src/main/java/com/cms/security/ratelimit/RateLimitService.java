package com.cms.security.ratelimit;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitService {

	private final Cache<String, Bucket> cache =
	        Caffeine.newBuilder()

	                .expireAfterAccess(
	                        Duration.ofHours(1)
	                )

	                .maximumSize(10000)

	                .build();

    public Bucket resolveBucket(
            String key,
            int capacity,
            Duration duration
    ) {

    	Bucket bucket = cache.getIfPresent(key);

    	if (bucket == null) {

    	    bucket = newBucket(
    	            capacity,
    	            duration
    	    );

    	    cache.put(
    	            key,
    	            bucket
    	    );
    	}

    	return bucket;
    }

    private Bucket newBucket(
            int capacity,
            Duration duration
    ) {

        Bandwidth limit =
                Bandwidth.builder()

                        .capacity(capacity)
                        .refillIntervally(
                                capacity,
                                duration
                        )
                        .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

}